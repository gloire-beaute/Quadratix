package quadratix;

import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

/**
 * Interface that implements methods to use an object as a number, with different operations
 * @param <T> Represents the type of the number-like object.
 */
public interface NumberOperations<T> extends Comparator<T> {
	
	@Nullable
	T plus(@Nullable T t1, @Nullable T t2);
	
	@Nullable
	T minus(@Nullable T t1, @Nullable T t2);
	
	@Nullable
	T multiply(@Nullable T t1, @Nullable T t2);
	
	@Nullable
	T divide(@Nullable T t1, @Nullable T t2);
	
	@Nullable
	T getZero();
}
