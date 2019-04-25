package quadratix;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

/**
 * Class that represents a combination of number.
 */
public class Combination extends Vector<Long> implements Comparable<Combination>, Serializable, Cloneable {
	
	public Combination(@NotNull Long... elements) {
		super(elements.length);
		this.addAll(Arrays.asList(elements));
	}
	public Combination(@NotNull Integer... elements) {
		super(elements.length);
		this.addAll(Arrays.stream(elements).map(Long::new).collect(Collectors.toList()));
	}
	public Combination(int length) {
		super(length);
	}
	public Combination() {
		super(0);
	}
	public Combination(@NotNull Collection<? extends Long> collection) {
		super(collection);
	}
	
	/**
	 * Swap two element at indexes `i` and `j`.
	 * @param i Index of the first element to swap.
	 * @param j Index of the second element to swap.
	 */
	public void swap(int i, int j) {
		Long li = get(i);
		set(i, get(j));
		set(j, li);
	}
	/**
	 * Swap two element at index `i` and a random index `j`.
	 * @param i Index of the first element to swap.
	 */
	public void swap(int i) {
		if (size() > 1) {
			// Make a list of available indexes (all indexes but i)
			ArrayList<Integer> indexes = new ArrayList<>(size()-1);
			for (int j = 0; j < size(); j++)
				if (j != i)
					indexes.add(j);
			
			int j = indexes.get(new Random().nextInt(indexes.size()));
			swap(i, j);
		}
	}
	
	/**
	 * Swap two elements randomly.
	 */
	public void swap() {
		if (size() > 1) {
			// Make a list of available indexes (all indexes but i)
			ArrayList<Integer> indexes = new ArrayList<>(size());
			for (int i = 0; i < size(); i++)
				indexes.add(i);
			
			int index = new Random().nextInt(indexes.size());
			int i = indexes.get(index);
			indexes.remove(index);
			index = new Random().nextInt(indexes.size());
			int j = indexes.get(index);
			swap(i, j);
		}
	}
	
	//region LIST OVERRIDES
	
	@Override
	public int compareTo(@NotNull Combination o) {
		throw new NotImplementedException();
	}
	
	@NotNull
	@Contract(value = " -> new", pure = true)
	public static NumberOperations<Combination> getOperations() {
		return new NumberOperations<Combination>() {
			@Nullable
			@Override
			public Combination plus(@Nullable Combination t1, @Nullable Combination t2) {
				throw new NotImplementedException();
			}
			
			@Nullable
			@Override
			public Combination minus(@Nullable Combination t1, @Nullable Combination t2) {
				throw new NotImplementedException();
			}
			
			@Nullable
			@Override
			public Combination multiply(@Nullable Combination t1, @Nullable Combination t2) {
				throw new NotImplementedException();
			}
			
			@Nullable
			@Override
			public Combination divide(@Nullable Combination t1, @Nullable Combination t2) {
				throw new NotImplementedException();
			}
			
			@NotNull
			@Override
			public Combination getZero() {
				return new Combination();
			}
			
			@Override
			public int compare(Combination o1, Combination o2) {
				if (o1 == null) {
					if (o2 == null)
						return 0;
					
					//noinspection ConstantConditions
					return o2.compareTo(o1);
				}
				return o1.compareTo(o2);
			}
		};
	}
	
	//endregion
	
	//region GETTER & SETTER
	
	
	
	//endregion
	
	//region OBJECT OVERRIDE
	
	@Override
	public synchronized String toString() {
		return String.join(", ", () -> new Iterator<CharSequence>() {
				
				private int i;
				
				@Override
				public boolean hasNext() {
					return i < size();
				}
				
				@Override
				public CharSequence next() {
					return Long.toString(get(i++));
				}
			});
	}
	
	//endregion
}
