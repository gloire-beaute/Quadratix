package quadratix.tabu;

import org.jetbrains.annotations.NotNull;
import quadratix.ElementaryFunction;
import quadratix.ISearch;
import quadratix.NumberOperations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.function.Function;

/**
 * Class that implement the tabu search. The functions `search()` and its overload search the optimal point in the space
 * of solution.
 * @param <P> Denotes the parameter type of the fitness function, that can be any elements (bits, combination, number,
 *           ...).
 * @param <R> represents the return type of the fitness function. It is often a number (integer or real).
 */
public class Tabu<P, R> implements ISearch<P, R> {
	
	/**
	 * The maximum number of iterations allow in the search.
	 */
	public static final int MAX_ITERATION = 1000;
	public int fitnessCall;
	
	public Tabu() {
		setFitnessCall(0);
	}
	
	/**
	 * Search the optimal point in a space of solutions.
	 * @param f The fitness function.
	 * @param x0 The starting point.
	 * @param V A function that maps an element `P` to a list of neighbors associated with their function to find it (x
	 *          -> x'), and the invert function (x' -> x).
	 * @param rOperation The operations we can apply on `R`.
	 * @param t0 The starting temperature.
	 * @return Return the optimal point if found.
	 */
	@Override
	public P search(@NotNull final Function<P, R> f, final P x0, @NotNull final Function<P, HashMap<P, ElementaryFunction<P>>> V, @NotNull final NumberOperations<R> rOperation, final double t0) {
		return search(f, x0, V, rOperation, t0, 1);
	}
	
	/**
	 * Search the optimal point in a space of solutions.
	 * @param f The fitness function.
	 * @param x0 The starting point.
	 * @param V A function that maps an element `P` to a list of neighbors associated with their function to find it (x
	 *          -> x'), and the invert function (x' -> x).
	 * @param rOperation The operations we can apply on `R`.
	 * @param t0 The starting temperature.
	 * @param tabuSize The fixed size of the tabu list. By default, the fixed size is 1.
	 * @return Return the optimal point if found.
	 */
	public P search(@NotNull final Function<P, R> f, final P x0, @NotNull final Function<P, HashMap<P, ElementaryFunction<P>>> V, @NotNull final NumberOperations<R> rOperation, final double t0, final int tabuSize) {
		TabuList<P, P> T = new TabuList<>(tabuSize);
		
		/**
		 * Elementary neighborhood
		 */
		HashSet<P> C = new HashSet<>();
		P xmin = x0;
		Vector<P> x = new Vector<>();
		x.add(x0);
		R fmin = f.apply(xmin);
		fitnessCall++;
		int i = 0;
		
		do {
			HashMap<P, ElementaryFunction<P>> elemFuns = V.apply(x.get(i));
			C.clear();
			C.addAll(elemFuns.keySet());
			// At this point, C = V(xi).
			//System.out.println("Number of neighbors : " + C.size());
			// Now, {m(xi) | m∈T} must be removed from it.
			for (Function<P, P> m : T)
				C.remove(m.apply(x.get(i)));
			
			if (!C.isEmpty()) {
				/* Choose y in C s.t. f(y) = min({f(z) | z∈C}) */
				
				// Take the first element in C
				P y = C.iterator().next();
				
				// The default `m` is identity
				ElementaryFunction<P> m = elemFuns.get(y)/*getOrDefault(y, ElementaryFunction.identity())*/;
				
				// Store the value of f(y) in order to minimize fitness call.
				R fy = f.apply(y);
				fitnessCall++;
				
				for (P z : C) {
					fitnessCall++;
					if (rOperation.compare(f.apply(z), fy) < 0) {
						y = z;
						// Recompute f(y) as `y` changed
						fy = f.apply(y);
						fitnessCall++;
						// TODO: Use `getOrDefault` when the debugging is done. The default value should be `m`.
						m = elemFuns.get(y);
					}
				}
				
				// Compute the fitness variation (f.apply(x.get(i)) == fmin?)
				R deltaF = rOperation.minus(fy, f.apply(x.get(i)));
				
				if (rOperation.compare(deltaF, rOperation.getZero()) >= 0) {// TODO: Is it <= or >= ? According to the two PDF in claco and Spiral, it should be >=
					// BUG DETECTED: When `>=` is used, TabuTest::slideExercise never finish, and loop indefinitely...
					// Put m^-1 in T
					if (m != null)
						T.add(m.invert());
				}
				if (rOperation.compare(fy, fmin) < 0) {//else
					fmin = fy;
					xmin = y;
				}

				if(x.contains(y)) //come back to chosen solution
					break;
				
				if (x0.getClass().isAssignableFrom(quadratix.bits.Bits.class) && fmin.getClass().isAssignableFrom(java.lang.Integer.class))
					System.out.println(String.format("Iteration %4d: xi=%s; xmin=%s; fmin=%2d", i, x.get(i), xmin, fmin));
				
				// xi becomes y
				x.add(y);
				
				// Increment the number of iteration
				i++;
			}
		} while (i < MAX_ITERATION && !C.isEmpty());
		
		// TODO: DEBUG
		if (i == MAX_ITERATION)
			System.out.print("Reached maximum number of iteration.");
		else
			System.out.print("No item in C any longer.");
		
		System.out.println(" i = " + i + ", |C| = " + C.size());
		
		if (C.size() <= 10)
			System.out.println("C = " + C.toString());
		
		return xmin;
	}

	//region GETTER & SETTER
	
	public int getFitnessCall() {
		return fitnessCall;
	}
	
	private void setFitnessCall(int fitnessCall) {
		this.fitnessCall = fitnessCall;
	}
	
	//endregion
}
