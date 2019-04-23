package quadratix;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.function.Function;

public class Tabu<P, R> implements ISearch<P, R> {
	
	public static final int MAX_ITERATION = 1000000;
	
	@Override
	public P search(@NotNull Function<P, R> f, P x0, @NotNull Function<P, HashMap<P, ElementaryFunction<P>>> V, @NotNull NumberOperations<R> rOperation, double t0) {
		return search(f, x0, V, rOperation, t0, 1);
	}
	public P search(@NotNull Function<P, R> f, P x0, @NotNull Function<P, HashMap<P, ElementaryFunction<P>>> V, @NotNull NumberOperations<R> rOperation, double t0, int tabuSize) {
		TabuList<P, P> T = new TabuList<>(tabuSize);
		
		/**
		 * Elementary neighborhood
		 */
		HashSet<P> C = new HashSet<>();
		P xmin = x0;
		Vector<P> xi = new Vector<>();
		xi.add(x0);
		R fmin = f.apply(xmin);
		int i = 0;
		
		do {
			HashMap<P, ElementaryFunction<P>> elemFuns = V.apply(xi.lastElement());
			C.clear();
			C.addAll(elemFuns.keySet());
			// At this point, C = V(xi).
			
			// Now, {m(xi) | m∈T} must be removed from it.
			for (Function<P, P> m : T)
				C.remove(m.apply(xi.lastElement()));
			
			if (!C.isEmpty()) {
				/* Choose y in C s.t. f(y) = min({f(z) | z in C}) */
				
				// Take the first element in C
				P y = C.iterator().next();
				if (i < 10)// TODO: DEBUG
					System.out.println("y0 = " + y);
				
				// The default `m` is identity
				ElementaryFunction<P> m = elemFuns.getOrDefault(y, ElementaryFunction.identity());
				
				for (P z : C) {
					if (rOperation.compare(f.apply(z), f.apply(y)) < 0) {
						y = z;
						m = elemFuns.getOrDefault(y, m);
					}
				}
				if (i < 10)// TODO: DEBUG
					System.out.println("ymin = " + y);
				
				// Compute f(y) and save the result in `fy`
				R fy = f.apply(y);
				
				// Compute the fitness variation
				R deltaF = rOperation.minus(fy, f.apply(xi.lastElement()));
				
				if (rOperation.compare(deltaF, rOperation.getZero()) <= 0) {
					// Put m^-1 in T
					if (m != null)
						T.add(m.invert());
				}
				if (rOperation.compare(fy, fmin) < 0) {
					fmin = fy;
					xmin = y;
				}
				
				xi.add(y);
			}
			
			i++;
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
}
