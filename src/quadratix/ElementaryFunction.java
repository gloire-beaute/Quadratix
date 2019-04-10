package quadratix;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface ElementaryFunction<P> extends MathFunction<P, P> {
	
	@NotNull
	@Contract(value = " -> new", pure = true)
	static <P> ElementaryFunction<P> identity() {
		return new ElementaryFunction<P>() {
			@Override
			public P apply(P p) {
				return p;
			}
			
			@Override
			public @NotNull Function<P, P> invert() {
				return p -> p;
			}
		};
	}
}
