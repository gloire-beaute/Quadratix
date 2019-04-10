package quadratix;

import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

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
