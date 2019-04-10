package quadratix;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

public class TabuList<P, R> extends ArrayList<Function<P, R>> implements Serializable, Cloneable, Iterable<Function<P, R>> {
	
	private int fixedSize;
	
	public TabuList(int fixedSize) {
		super(fixedSize);
		setFixedSize(fixedSize);
	}
	
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
	
	/* GETTERS & SETTERS */
	
	public int getFixedSize() {
		return fixedSize;
	}
	
	public void setFixedSize(int fixedSize) {
		this.fixedSize = fixedSize;
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
