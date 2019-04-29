package quadratix;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
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
	
	//region STATIC METHODS
	
	@NotNull
	@Contract(value = " -> new", pure = true)
	static NumberOperations<Integer> getIntegerOperations() {
		return new NumberOperations<Integer>() {
			@Nullable
			@Override
			public Integer plus(@Nullable Integer t1, @Nullable Integer t2) {
				return t1 + t2;
			}
			
			@Nullable
			@Override
			public Integer minus(@Nullable Integer t1, @Nullable Integer t2) {
				return t1 - t2;
			}
			
			@Nullable
			@Override
			public Integer multiply(@Nullable Integer t1, @Nullable Integer t2) {
				return t1 * t2;
			}
			
			@Nullable
			@Override
			public Integer divide(@Nullable Integer t1, @Nullable Integer t2) {
				return t1 / t2;
			}
			
			@Nullable
			@Override
			public Integer getZero() {
				return 0;
			}
			
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		};
	}
	
	@NotNull
	@Contract(value = " -> new", pure = true)
	static NumberOperations<Double> getDoubleOperations() {
		return new NumberOperations<Double>() {
			@Nullable
			@Override
			public Double plus(@Nullable Double t1, @Nullable Double t2) {
				return t1 + t2;
			}
			
			@Nullable
			@Override
			public Double minus(@Nullable Double t1, @Nullable Double t2) {
				return t1 - t2;
			}
			
			@Nullable
			@Override
			public Double multiply(@Nullable Double t1, @Nullable Double t2) {
				return t1 * t2;
			}
			
			@Nullable
			@Override
			public Double divide(@Nullable Double t1, @Nullable Double t2) {
				return t1 / t2;
			}
			
			@Nullable
			@Override
			public Double getZero() {
				return 0.;
			}
			
			@Override
			public int compare(Double o1, Double o2) {
				return o1.compareTo(o2);
			}
		};
	}
	
	@NotNull
	@Contract(value = " -> new", pure = true)
	static NumberOperations<Float> getFloatOperations() {
		return new NumberOperations<Float>() {
			@Nullable
			@Override
			public Float plus(@Nullable Float t1, @Nullable Float t2) {
				return t1 + t2;
			}
			
			@Nullable
			@Override
			public Float minus(@Nullable Float t1, @Nullable Float t2) {
				return t1 - t2;
			}
			
			@Nullable
			@Override
			public Float multiply(@Nullable Float t1, @Nullable Float t2) {
				return t1 * t2;
			}
			
			@Nullable
			@Override
			public Float divide(@Nullable Float t1, @Nullable Float t2) {
				return t1 / t2;
			}
			
			@Nullable
			@Override
			public Float getZero() {
				return 0f;
			}
			
			@Override
			public int compare(Float o1, Float o2) {
				return o1.compareTo(o2);
			}
		};
	}
	
	@NotNull
	@Contract(value = " -> new", pure = true)
	static NumberOperations<Long> getLongOperations() {
		return new NumberOperations<Long>() {
			@Nullable
			@Override
			public Long plus(@Nullable Long t1, @Nullable Long t2) {
				return t1 + t2;
			}
			
			@Nullable
			@Override
			public Long minus(@Nullable Long t1, @Nullable Long t2) {
				return t1 - t2;
			}
			
			@Nullable
			@Override
			public Long multiply(@Nullable Long t1, @Nullable Long t2) {
				return t1 * t2;
			}
			
			@Nullable
			@Override
			public Long divide(@Nullable Long t1, @Nullable Long t2) {
				return t1 / t2;
			}
			
			@Nullable
			@Override
			public Long getZero() {
				return 0L;
			}
			
			@Override
			public int compare(Long o1, Long o2) {
				return o1.compareTo(o2);
			}
		};
	}
	
	//endregion
}
