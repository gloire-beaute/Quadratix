package quadratix.simulatedannealing;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import quadratix.ElementaryFunction;
import quadratix.ISearch;
import quadratix.ListUtil;
import quadratix.NumberOperations;

import java.util.HashMap;
import java.util.Random;
import java.util.function.Function;

public class SimulatedAnnealing<P, R extends Number> implements ISearch<P, R> {
	
	@Nullable
	@Override
	public P search(@NotNull Function<P, R> f,
	                P x0,
	                @NotNull Function<P, HashMap<P, ElementaryFunction<P>>> V,
	                @NotNull NumberOperations<R> rOperation,
	                double t0) {
		return search(f, x0, V, rOperation, t0, 10, 10, 0.5);
	}
	
	@Nullable
	public P search(@NotNull Function<P, R> f,
	                P x0,
	                @NotNull Function<P, HashMap<P, ElementaryFunction<P>>> V,
	                @NotNull NumberOperations<R> rOperation,
	                double t0,
	                int n1,
	                int n2,
	                double mu) {
		P xmin = x0;
		P xi = x0;
		double tk = t0;
		R fmin = f.apply(xmin);
		int i = 0;
		
		for (int k = 0; k < n1; k++) {
			for (int l = 0; l < n2; l++) {
				// Randomly select y ∈ V(xi)
				HashMap<P, ElementaryFunction<P>> elemFuns = V.apply(xi);
				P y = ListUtil.pickRandomly(elemFuns.keySet());
				
				// Compute delta f
				R f_xi = f.apply(xi);
				R deltaF = rOperation.minus(f.apply(y), f_xi);
				
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
}
