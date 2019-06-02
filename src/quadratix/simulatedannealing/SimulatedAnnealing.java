package quadratix.simulatedannealing;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import quadratix.ElementaryFunction;
import quadratix.ISearch;
import quadratix.ListUtil;
import quadratix.NumberOperations;
import quadratix.stats.Counter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

/**
 * Class that implements the simulated annealing
 * @param <P> Denotes the parameter type of the fitness function, that can be any elements (bits, combination, number,
 *            ...).
 * @param <R> represents the return type of the fitness function. It is often a number (integer or real).
 */
public class SimulatedAnnealing<P, R extends Number> implements ISearch<P, R> {
	
	/**
	 * Initial temperature
	 */
	private int t0;
	
	/**
	 * Maximum number of iteration for first loop.
	 */
	private int n1;
	
	/**
	 * Maximum number of iteration for second loop.
	 */
	private int n2;
	
	/**
	 * Factor to decrease temperature at each iteration.
	 */
	private double mu;
	
	/**
	 * Counter for the number of call to the fitness function.
	 */
	private Counter fitnessCall;
	
	/**
	 * Constructor with parameters.
	 * @param t0 Initial temperature
	 * @param n1 Maximum number of iteration for first loop.
	 * @param n2 Maximum number of iteration for second loop.
	 * @param mu Factor to decrease temperature at each iteration.
	 */
	public SimulatedAnnealing(int t0, int n1, int n2, double mu) {
		setT0(t0);
		setN1(n1);
		setN2(n2);
		setMu(mu);
		fitnessCall = new Counter();
	}
	
	/**
	 * Default constructor. The default values are :
	 * * t0 = 1
	 * n1 = 10
	 * n2 = 10
	 * mu = 0.5
	 */
	public SimulatedAnnealing() {
		this(1, 10, 10, 0.5);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Nullable
	@Override
	public P search(@NotNull final Function<P, R> f,
	                final P x0,
	                @NotNull final Function<P, HashMap<P, ElementaryFunction<P>>> V,
	                @NotNull final NumberOperations<R> rOperation) {
		return search(f, x0, V, rOperation, getT0(), getN1(), getN2(), getMu());
	}
	
	/**
	 * Search the optimal point in a space of solutions.
	 * @param f The fitness function.
	 * @param x0 The starting point.
	 * @param V A function that maps an element `P` to a list of neighbors associated with their function to find it (x
	 *          -&gt; x'), and the invert function (x' -&gt; x).
	 * @param rOperation The operations we can apply on `R`.
	 * @param t0 Initial temperature
	 * @param n1 Maximum number of iteration for first loop.
	 * @param n2 Maximum number of iteration for second loop.
	 * @param mu Factor to decrease temperature at each iteration.
	 * @return Return the optimal point if found.
	 */
	@Nullable
	public P search(final @NotNull Function<P, R> f,
	                final P x0,
	                @NotNull final Function<P, HashMap<P, ElementaryFunction<P>>> V,
	                @NotNull final NumberOperations<R> rOperation,
	                final double t0,
	                final int n1,
	                final int n2,
	                final double mu) {
		fitnessCall.reset();
		P xmin = x0;
		P xi = x0;
		double tk = t0;
		R fmin = f.apply(xmin);
		fitnessCall.increment();
		int i = 0;
		
		for (int k = 0; k < n1; k++) {
			for (int l = 0; l < n2; l++) {
				// Randomly select y ∈ V(xi)
				HashMap<P, ElementaryFunction<P>> elemFuns = V.apply(xi);
				P y = ListUtil.pickRandomly(elemFuns.keySet());
				
				// Compute delta f
				R f_xi = f.apply(xi);
				R deltaF = rOperation.minus(f.apply(y), f_xi);
				fitnessCall.increment(2);
				
				if (rOperation.compare(deltaF, rOperation.getZero()) <= 0) {
					xi = y;
					
					if (rOperation.compare(f_xi, fmin) < 0) {
						xmin = xi;
						fmin = f_xi;
					}
				}
				else {
					// Randomly draw p ∈ [0 ; 1] according to uniform distribution
					double p = new Random().nextDouble();
					if (p <= Math.exp(- deltaF.doubleValue() / tk))
						xi = y;
				}
			}
			tk = mu * tk;
		}

		return xmin;
	}
	
	/**
	 * Compute an approximation of the most optimal initial temperature t0.
	 * @param f The fitness function.
	 * @param V A function that maps an element `P` to a list of neighbors associated with their function to find it (x
	 *          -&gt; x'), and the invert function (x' -&gt; x).
	 * @param rOperation The operations we can apply on `R`.
	 * @param randomGenerator A function that return a random element of `P` at each call.
	 * @param rToDouble A function that convert an element of type `R` to a real number ({@code double}).
	 * @param nbIteration Number of iterations. The larger, the more accurate the computation will be.
	 * @param <P> Denotes the parameter type of the fitness function, that can be any elements (bits, combination, number,
	 *            ...).
	 * @param <R> represents the return type of the fitness function. It is often a number (integer or real).
	 * @return Return t0.
	 * @see #search(Function, Object, Function, NumberOperations, double, int, int, double)
	 * @see java.util.stream.Stream
	 * @see java.util.stream.Stream#mapToDouble(ToDoubleFunction)
	 * @see java.util.stream.DoubleStream
	 * @see java.util.stream.DoubleStream#average()
	 */
	public static <P, R> double computeTemperature(final @NotNull Function<P, R> f,
	                                               @NotNull final Function<P, HashMap<P, ElementaryFunction<P>>> V,
	                                               @NotNull final NumberOperations<R> rOperation,
	                                               @NotNull final Function<Void, P> randomGenerator,
	                                               @NotNull final Function<R, Double> rToDouble,
	                                               final int nbIteration) {

		ArrayList<R> deltaFs = computeDeltaTemperatures(f, V, rOperation, randomGenerator, nbIteration);

		// Compute the average of deltaF's
		double avg;
		try {
			avg = deltaFs.stream().mapToDouble(rToDouble::apply).average().getAsDouble();
		} catch (NoSuchElementException ex) {
			throw new IllegalArgumentException("Cannot compute the average for t0.", ex);
		}
		
		// Compute t0 with a probability to accept neighbors of 80%
		return - avg/Math.log(0.8);
	}

	/**
	 * Compute an approximation of the most optimal initial n1 with t0.
	 * @param f The fitness function.
	 * @param V A function that maps an element `P` to a list of neighbors associated with their function to find it (x
	 *          -&gt; x'), and the invert function (x' -&gt; x).
	 * @param rOperation The operations we can apply on `R`.
	 * @param randomGenerator A function that return a random element of `P` at each call.
	 * @param rToDouble A function that convert an element of type `R` to a real number ({@code double}).
	 * @param nbIteration Number of iterations. The larger, the more accurate the computation will be.
	 * @param t0 Initial temperature
	 * @param mu Decrease factor of temperature t0
	 * @param <P> Denotes the parameter type of the fitness function, that can be any elements (bits, combination, number,
	 *            ...).
	 * @param <R> represents the return type of the fitness function. It is often a number (integer or real).
	 * @return Return t0.
	 * @see #search(Function, Object, Function, NumberOperations, double, int, int, double)
	 * @see java.util.stream.Stream
	 * @see java.util.stream.Stream#mapToDouble(ToDoubleFunction)
	 * @see java.util.stream.DoubleStream
	 * @see java.util.stream.DoubleStream#average()
	 */
	public static <P, R> Integer computeN1(final @NotNull Function<P, R> f,
	                                               @NotNull final Function<P, HashMap<P, ElementaryFunction<P>>> V,
	                                               @NotNull final NumberOperations<R> rOperation,
	                                               @NotNull final Function<Void, P> randomGenerator,
	                                               @NotNull final Function<R, Double> rToDouble,
	                                               final double t0,
	                                               final double mu,
	                                               final int nbIteration) throws IllegalArgumentException {
		ArrayList<R> deltaFs = computeDeltaTemperatures(f, V, rOperation, randomGenerator, nbIteration);

		// Compute the average of deltaF's
		double avg;
		try {
			avg = deltaFs.stream().mapToDouble(rToDouble::apply).average().getAsDouble();
		} catch (NoSuchElementException ex) {
			throw new IllegalArgumentException("Cannot compute the average for n1.", ex);
		}

		// Compute n1 with a probability to accept same solution of 1%
        double n = Math.log(-avg/(t0 * Math.log(0.01))) / Math.log(mu);
		return (int) Math.ceil(n);
	}
	
	/**
	 * Compute multiple Δf for the temperatures computed from random elements.
	 * @param f The fitness function.
	 * @param V A function that maps an element `P` to a list of neighbors associated with their function to find it (x
	 *          -&gt; x'), and the invert function (x' -&gt; x).
	 * @param rOperation The operations we can apply on `R`.
	 * @param randomGenerator A function that return a random element of `P` at each call.
	 * @param nbIteration Number of iterations. The larger, the more accurate the computation will be.
	 * @param <P> Denotes the parameter type of the fitness function, that can be any elements (bits, combination, number,
	 *            ...).
	 * @param <R> represents the return type of the fitness function. It is often a number (integer or real).
	 * @return Return a list containing multiple Δf.
	 */
	@NotNull
	private static <P, R> ArrayList<R> computeDeltaTemperatures(@NotNull Function<P, R> f,
	                                                            @NotNull Function<P, HashMap<P, ElementaryFunction<P>>> V,
	                                                            @NotNull NumberOperations<R> rOperation,
	                                                            @NotNull Function<Void, P> randomGenerator,
	                                                            int nbIteration) {
		ArrayList<R> deltaFs = new ArrayList<>(nbIteration);

		for (int i = 0; i < nbIteration; i++) {
			// Generate randomly a combination
			P x = randomGenerator.apply(null);

			// Generate all its neighbors
			ArrayList<P> neighbors = new ArrayList<>(V.apply(x).keySet());

			if (!neighbors.isEmpty()) {
				// Select the worst neighbor (s.t. f(neighbor) is the biggest)
				P worstNeighbor = neighbors.get(0);
				for (P neighbor : neighbors) {
					if (rOperation.compare(f.apply(neighbor), f.apply(worstNeighbor)) > 0)
						worstNeighbor = neighbor;
				}
				// Compute deltaF
				deltaFs.add(rOperation.abs(rOperation.minus(f.apply(x), f.apply(worstNeighbor))));
			}
		}
		return deltaFs;
	}


	//region GETTERS & SETTERS
	
	@Contract(pure = true)
	public int getT0() {
		return t0;
	}
	
	public void setT0(int t0) {
		this.t0 = t0;
	}
	
	@Contract(pure = true)
	public int getN1() {
		return n1;
	}
	
	public void setN1(int n1) {
		this.n1 = n1;
	}
	
	@Contract(pure = true)
	public int getN2() {
		return n2;
	}
	
	public void setN2(int n2) {
		this.n2 = n2;
	}
	
	@Contract(pure = true)
	public double getMu() {
		return mu;
	}
	
	public void setMu(double mu) {
		this.mu = mu;
	}
	
	@Contract(pure = true)
	public int getFitnessCall() {
		return fitnessCall.get();
	}
	
	//endregion GETTERS & SETTERS
}
