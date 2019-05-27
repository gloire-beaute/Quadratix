package quadratix.tabu;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import quadratix.ElementaryFunction;
import quadratix.ISearch;
import quadratix.NumberOperations;
import quadratix.stats.Counter;

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
	public static final int DEFAULT_MAX_ITERATION = 1000;
	private Counter fitnessCall;
	private int tabuSize;
	
	public Tabu(int tabuSize) {
		fitnessCall = new Counter();
		setTabuSize(tabuSize);
	}
	public Tabu() {
		this(1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public P search(@NotNull final Function<P, R> f, final P x0, @NotNull final Function<P, HashMap<P, ElementaryFunction<P>>> V, @NotNull final NumberOperations<R> rOperation) {
		return search(f, x0, V, rOperation, getTabuSize(), DEFAULT_MAX_ITERATION);
	}
	
	/**
	 * Search the optimal point in a space of solutions.
	 * @param f The fitness function.
	 * @param x0 The starting point.
	 * @param V A function that maps an element `P` to a list of neighbors associated with their function to find it (x
	 *          -&gt; x'), and the invert function (x' -&gt; x).
	 * @param rOperation The operations we can apply on `R`.
	 * @param tabuSize The fixed size of the tabu list. By default, the fixed size is 1.
	 * @return Return the optimal point if found.
	 */
	public P search(@NotNull final Function<P, R> f, final P x0, @NotNull final Function<P, HashMap<P, ElementaryFunction<P>>> V, @NotNull final NumberOperations<R> rOperation, final int tabuSize) {
		return search(f, x0, V, rOperation, tabuSize, DEFAULT_MAX_ITERATION);
	}
	
	/**
	 * Search the optimal point in a space of solutions.
	 * @param f The fitness function.
	 * @param x0 The starting point.
	 * @param V A function that maps an element `P` to a list of neighbors associated with their function to find it (x
	 *          -&gt; x'), and the invert function (x' -&gt; x).
	 * @param rOperation The operations we can apply on `R`.
	 * @param tabuSize The fixed size of the tabu list. By default, the fixed size is 1.
	 * @param maxIteration The maximum number of iterations the algorithm can do.
	 * @return Return the optimal point if found.
	 */
	public P search(@NotNull final Function<P, R> f, final P x0, @NotNull final Function<P, HashMap<P, ElementaryFunction<P>>> V, @NotNull final NumberOperations<R> rOperation, final int tabuSize, final int maxIteration) {
		fitnessCall.reset();
		TabuList<P, P> T = new TabuList<>(tabuSize);
		
		/**
		 * Elementary neighborhood
		 */
		HashSet<P> C = new HashSet<>();
		P xmin = x0;
		Vector<P> x = new Vector<>();
		x.add(x0);
		R fmin = f.apply(xmin);
		fitnessCall.increment();
		int i = 0;
		
		do {
			HashMap<P, ElementaryFunction<P>> elemFuns = V.apply(x.get(i));
			C.clear();
			C.addAll(elemFuns.keySet());
			// At this point, C = V(xi).
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
				fitnessCall.increment();
				
				for (P z : C) {
					fitnessCall.increment();
					if (rOperation.compare(f.apply(z), fy) < 0) {
						y = z;
						// Recompute f(y) as `y` changed
						fy = f.apply(y);
						fitnessCall.increment();
						m = elemFuns.get(y);
					}
				}
				
				// Compute the fitness variation
				R deltaF = rOperation.minus(fy, f.apply(x.get(i)));
				fitnessCall.increment();
				
				if (rOperation.compare(deltaF, rOperation.getZero()) >= 0) {
					// Put m^-1 in T
					if (m != null)
						T.add(m.invert());
				}
				if (rOperation.compare(fy, fmin) < 0) {
					fmin = fy;
					xmin = y;
				}
				
				// xi becomes y
				x.add(y);
				
				// Increment the number of iteration
				i++;
			}
		} while (i < maxIteration && !C.isEmpty());

		System.out.println("Number of iterations " + i);
		return xmin;
	}

	//region GETTER & SETTER
	
	@Contract(pure = true)
	public int getFitnessCall() {
		return fitnessCall.get();
	}
	
	@Contract(pure = true)
	public int getTabuSize() {
		return tabuSize;
	}
	
	public void setTabuSize(int tabuSize) {
		if (tabuSize < 0)
			throw new IllegalArgumentException("The size cannot be negative.");
		
		this.tabuSize = tabuSize;
	}
	
	//endregion
}
