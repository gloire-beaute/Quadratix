package quadratix.tabu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

/**
 * Class that inherits from
 * `ArrayList<Function<P, R>>`. It is a list with a fixed size, defined when it is constructed: When the list is at full
 * capacity, and a new element is added, it will overwrite the first element. The elements listed are only functions
 * (through the interface `Function`). Be careful not to get confuse betweenb`size()` which is the actual size of the
 * list (exactly like in `List`), and `getFixedSize()`,bwhich is the fixed length of the list. `size()` ≤
 * `getFixedSize()`.
 * @param <P> Denotes the parameter type of the fitness function, that can be any elements (bits, combination, number,
 *           ...).
 * @param <R> represents the return type of the fitness function. It is often a number (integer or real).
 */
public class TabuList<P, R> extends ArrayList<Function<P, R>> implements Serializable, Cloneable, Iterable<Function<P, R>> {
	
	/**
	 * The fixed size of the list. Can be changed dynamically.
	 */
	private int fixedSize;
	
	/**
	 * Construct a tabu list.
	 * @param fixedSize The fixed size of the list.
	 */
	public TabuList(final int fixedSize) {
		super(fixedSize);
		setFixedSize(fixedSize);
	}
	
	/**
	 * Construct a tabu list. The fixed size of the list is equal to the size of the given collection.
	 */
	public TabuList(@NotNull Collection<? extends Function<P, R>> c) {
		super(c);
		setFixedSize(c.size());
	}
	
	/* LIST */
	
	@NotNull
	@Override
	public Object[] toArray() {
		return Arrays.copyOf(super.toArray(), getFixedSize());
	}
	
	@NotNull
	@Override
	public <T> T[] toArray(@NotNull T[] a) {
		return Arrays.copyOf(super.toArray(a), getFixedSize());
	}
	
	/**
	 * Appends the specified function to the end of this list. If the list is at maximum capacity (`size` ==
	 * `fixedSize`), then the element overwrite the first one.
	 * @param x function to be appended to this list.
	 * @return <tt>true</tt> (as specified by {@link Collection#add}).
	 */
	@Override
	public boolean add(@NotNull Function<P, R> x) {
		if (size() == getFixedSize()) {
			// Remove the first element, and shift everything
			remove(0);
			return super.add(x);
		}
		else
			return super.add(x);
	}
	
	@NotNull
	public Function<P, R> getFirst() {
		return get(0);
	}
	
	@NotNull
	public Function<P, R> getLast() {
		return get(size()-1);
	}
	
	/* GETTERS & SETTERS */
	
	public int getFixedSize() {
		return fixedSize;
	}
	
	public void setFixedSize(int fixedSize) {
		this.fixedSize = fixedSize;
		
		// If the size if greater than the new fixed size, remove all elements from index fixed size.
		if (size() > getFixedSize())
			for (int i = getFixedSize(); i < size();)
				remove(i);
	}
	
	/* OVERRIDE */
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TabuList)) return false;
		if (!super.equals(o)) return false;
		TabuList<?, ?> tabuList = (TabuList<?, ?>) o;
		return getFixedSize() == tabuList.getFixedSize();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getFixedSize());
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
