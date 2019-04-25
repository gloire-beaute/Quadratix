package quadratix;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface MathFunction<P, R> extends Function<P, R> {
	
	/**
	 * Return the inverse of the function defined by `apply()`.
	 * @return The function $apply^{-1}$).
	 */
	@NotNull
	Function<R, P> invert();
}
