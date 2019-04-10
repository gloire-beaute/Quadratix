package quadratix;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface MathFunction<P, R> extends Function<P, R> {
	
	/**
	 * Return the inverse of f ($f^{-1}$).
	 * @return The function $f^{-1}$).
	 */
	@NotNull
	Function<R, P> invert();
}
