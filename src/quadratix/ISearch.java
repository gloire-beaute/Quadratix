package quadratix;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Function;

public interface ISearch<P, R> {
	
	P search(@NotNull Function<P, R> f, P x0, @NotNull Function<P, Set<P>> V, @NotNull NumberOperations<R> rOperation, double t0);
}
