package quadratix;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.function.Function;

/**
 * Interface that implements the `search` function. It must be used by all classes that use an optimisation search.
 * @param <P> Denotes the parameter type of the fitness function, that can be any elements (bits, combination, number,
 *           ...).
 * @param <R> represents the return type of the fitness function. It is often a number (integer or real).
 */
public interface ISearch<P, R> {
	
	/**
	 * Search the optimal point in a space of solutions.
	 * @param f The fitness function.
	 * @param x0 The starting point.
	 * @param V A function that maps an element `P` to a list of neighbors associated with their function to find it (x
	 *          -> x'), and the invert function (x' -> x).
	 * @param rOperation The operations we can apply on `R`.
	 * @return Return the optimal point if found.
	 */
	@Nullable
	P search(@NotNull final Function<P, R> f, final P x0, @NotNull final Function<P, HashMap<P, ElementaryFunction<P>>> V, @NotNull final NumberOperations<R> rOperation);
}
