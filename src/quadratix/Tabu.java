package quadratix;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class Tabu<P, R> implements ISearch<P, R> {
	
	public static final int MAX_ITERATION = 10000;
	
	@Override
	public P search(@NotNull Function<P, R> f, P x0, @NotNull Function<P, Set<P>> V, @NotNull NumberOperations<R> rOperation, double t0) {
		return search(f, x0, V, rOperation, t0, 1);
	}
	public P search(@NotNull Function<P, R> f, P x0, @NotNull Function<P, Set<P>> V, @NotNull NumberOperations<R> rOperation, double t0, int tabuSize) {
		TabuList<P, P> T = new TabuList(tabuSize);
		
		/**
		 * Voisinage élémentaire
		 */
		Set<P> C;
		P xmin = x0;
		P xi = x0;
		R fmin = f.apply(xmin);
		int i = 0;
		
		do {
			C = new HashSet<>(V.apply(xi));
			Set<Function<P, P>> forbiddenOperations = new HashSet<>();
			for (Function<P, P> function : T)
				C.remove(function.apply(xi));
			
			if (!C.isEmpty()) {
				// Choose y in C s.t. f(y) = min({f(z) | z in C})
				P y;
				for (P z : C) {
					if (rOperation.compare(f.apply(z), f.apply(y)) < 0)
						y = z;
				}
				
				// Compute f(y) and save the result in `fy`
				R fy = f.apply(y);
				
				// Compute the fitness variation
				R deltaF = rOperation.minus(fy, f.apply(xi));
				
				if (rOperation.compare(deltaF, rOperation.getZero()) >= 0) {
					// Put m^-1 in T
				}
				if (rOperation.compare(fy, fmin) < 0) {
					fmin = fy;
					xmin = y;
				}
				
				xi = y;
				
				i++;
			}
			
		} while (i == MAX_ITERATION || C.isEmpty());
		
		return xmin;
	}
}
