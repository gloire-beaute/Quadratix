package quadratix.simulatedannealing;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import quadratix.ElementaryFunction;
import quadratix.ISearch;
import quadratix.ListUtil;
import quadratix.NumberOperations;
import quadratix.stats.Counter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class SimulatedAnnealing<P, R extends Number> implements ISearch<P, R> {
	
	private int t0;
	private int n1;
	private int n2;
	private double mu;
	
	private Counter fitnessCall;
	
	public SimulatedAnnealing(int t0, int n1, int n2, double mu) {
		setT0(t0);
		setN1(n1);
		setN2(n2);
		setMu(mu);
		fitnessCall = new Counter();
	}
	public SimulatedAnnealing() {
		this(1, 10, 10, 0.5);
	}
	
	@Nullable
	@Override
	public P search(@NotNull final Function<P, R> f,
	                final P x0,
	                @NotNull final Function<P, HashMap<P, ElementaryFunction<P>>> V,
	                @NotNull final NumberOperations<R> rOperation) {
		return search(f, x0, V, rOperation, getT0(), getN1(), getN2(), getMu());
	}
	
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
	
	public static <P, R> double computeTemperature(final @NotNull Function<P, R> f,
	                                               @NotNull final Function<P, HashMap<P, ElementaryFunction<P>>> V,
	                                               @NotNull final NumberOperations<R> rOperation,
	                                               @NotNull final Function<Void, P> randomGenerator,
	                                               @NotNull final Function<R, Double> rToDouble,
	                                               final int nbIteration) {
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
	
	//region GETTERS & SETTERS
	
	public int getT0() {
		return t0;
	}
	
	public void setT0(int t0) {
		this.t0 = t0;
	}
	
	public int getN1() {
		return n1;
	}
	
	public void setN1(int n1) {
		this.n1 = n1;
	}
	
	public int getN2() {
		return n2;
	}
	
	public void setN2(int n2) {
		this.n2 = n2;
	}
	
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
