package quadratix.stats;

import java.io.Serializable;
import java.util.Objects;
import java.util.Observable;

/**
 * This class might be the most scientific component of this project. It carries the huge responsibility to count.
 * Count what? Well, that's up to you! It can count anything! But with determination! And guess what? It's thread-safe!
 */
public class Counter extends Observable implements Serializable, Cloneable {
	
	public int c;
	
	//region CONSTRUCTORS
	
	/**
	 * Constructor.
	 * @param initialValue Initialize the counter with this value.
	 */
	public Counter(int initialValue) {
		set(initialValue);
	}
	
	/**
	 * Constructor. The default value of the counter is 0.
	 */
	public Counter() {
		this(0);
	}
	
	//endregion
	
	//region METHODS
	
	/**
	 * Increment the counter of {@code step}.
	 * @param step The number of step to add to the counter.
	 * @return Return the current counter (after incrementing it)
	 */
	public synchronized int increment(int step) {
		return c += step;
	}
	/**
	 * Increment the counter of 1.
	 * @return Return the current counter (after incrementing it)
	 */
	public int increment() {
		return increment(1);
	}
	
	/**
	 * Decrement the counter of {@code step}.
	 * @param step The number of step to remove from the counter.
	 * @return Return the current counter (after decrementing it)
	 */
	public synchronized int decrement(int step) {
		return c -= step;
	}
	/**
	 * Decrement the counter of 1.
	 * @return Return the current counter (after decrementing it)
	 */
	public int decrement() {
		return decrement(1);
	}
	
	//endregion
	
	//region GETTERS & SETTERS
	
	/**
	 * @return Return the current value of the counter.
	 */
	public synchronized int get() {
		return c;
	}
	
	/**
	 * Set the counter to a new value.
	 * @param c The new value.
	 */
	public synchronized void set(int c) {
		this.c = c;
	}
	
	//endregion
	
	//region OBJECT OVERRIDES
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Counter)) return false;
		Counter counter = (Counter) o;
		return c == counter.c;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(c);
	}
	
	@Override
	public String toString() {
		return Integer.toString(c);
	}
	
	//endregion
}
