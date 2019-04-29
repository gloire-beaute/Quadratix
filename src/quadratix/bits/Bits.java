package quadratix.bits;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import quadratix.ElementaryFunction;
import quadratix.NumberOperations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

/**
 * Class that represents an array of bits.
 */
public class Bits extends Number implements Comparable<Bits>, Serializable, Cloneable {
	
	private int length;
	
	/**
	 * The value of the bits as integer.
	 */
	private int value;
	
	public Bits(@NotNull String bits, int length) {
		setLength(length);
		setBits(bits);
	}
	public Bits(@NotNull String bits) {
		setLength(bits.length());
		setBits(bits);
	}
	public Bits(int value, int length) {
		setLength(length);
		setValue(value);
	}
	public Bits(int value) {
		setLength(Integer.toBinaryString(value).length());
		setValue(value);
	}
	public Bits(@NotNull Bits bits) {
		setLength(bits.getLength());
		setValue(bits.getValue());
	}
	
	public boolean getBitFromLeft(int i) {
		return getBits().charAt(i) == '1';
	}
	
	public boolean getBitFromRight(int i) {
		return getBitFromLeft(getBits().length() - i - 1);
	}
	
	public void putBitFromLeft(int i, boolean bit) {
		setBits(getBits().substring(0, i) + (bit ? "1" : "0") + getBits().substring(i+1));
	}
	
	public void putBitFromRight(int i, boolean bit) {
		putBitFromLeft(getBits().length() - i - 1, bit);
	}
	
	public void invertBitFromLeft(int i) {
		putBitFromLeft(i, !getBitFromLeft(i));
	}
	
	public void invertBitFromRight(int i) {
		invertBitFromLeft(getBits().length() - i - 1);
	}
	
	@NotNull
	@Contract("_, _ -> new")
	public static Bits plus(@NotNull Bits b1, @NotNull Bits b2) {
		return new Bits(b1.getValue() + b2.getValue());
	}
	
	@NotNull
	@Contract("_, _ -> new")
	public static Bits minus(@NotNull Bits b1, @NotNull Bits b2) {
		return new Bits(b1.getValue() - b2.getValue());
	}
	
	@NotNull
	@Contract("_, _ -> new")
	public static Bits multiply(@NotNull Bits b1, @NotNull Bits b2) {
		return new Bits(b1.getValue() * b2.getValue());
	}
	
	@NotNull
	@Contract("_, _ -> new")
	public static Bits divide(@NotNull Bits b1, @NotNull Bits b2) {
		return new Bits(b1.getValue() / b2.getValue());
	}
	
	@NotNull
	public Bits plus(@NotNull Bits bits) {
		return Bits.plus(this, bits);
	}
	
	@NotNull
	public Bits minus(@NotNull Bits bits) {
		return Bits.minus(this, bits);
	}
	
	@NotNull
	public Bits multiply(@NotNull Bits bits) {
		return Bits.multiply(this, bits);
	}
	
	@NotNull
	public Bits divideBy(@NotNull Bits bits) {
		return Bits.divide(this, bits);
	}
	
	@NotNull
	public Bits invertAllBits() {
		StringBuilder n = new StringBuilder();
		
		for (int i = 0; i < getLength(); i++)
			n.append(getBitFromLeft(i) ? "0" : "1");
		
		return new Bits(n.toString());
	}
	
	@NotNull
	@Contract(value = " -> new", pure = true)
	public static NumberOperations<Bits> getOperations() {
		return new NumberOperations<Bits>() {
			@Nullable
			@Override
			public Bits plus(@Nullable Bits t1, @Nullable Bits t2) {
				return Bits.plus(t1, t2);
			}
			
			@Nullable
			@Override
			public Bits minus(@Nullable Bits t1, @Nullable Bits t2) {
				return Bits.minus(t1, t2);
			}
			
			@Nullable
			@Override
			public Bits multiply(@Nullable Bits t1, @Nullable Bits t2) {
				return Bits.multiply(t1, t2);
			}
			
			@Nullable
			@Override
			public Bits divide(@Nullable Bits t1, @Nullable Bits t2) {
				return Bits.divide(t1, t2);
			}
			
			@Nullable
			@Override
			public Bits getZero() {
				return new Bits(0);
			}
			
			@Override
			public int compare(Bits o1, Bits o2) {
				if (o1 == null) {
					if (o2 == null)
						return 0;
					
					return o2.compareTo(o1);
				}
				return o1.compareTo(o2);
			}
		};
	}
	
	@NotNull
	public static ArrayList<Bits> generateAllPossibility(int numberOfBits) {
		ArrayList<Bits> possibilities = new ArrayList<>();
		
		/* Compute the maximum number as integer */
		
		// Make the higher number in binary with `numberOfBits`
		StringBuilder strb = new StringBuilder();
		
		for (int i = 0; i < numberOfBits; i++)
			strb.append("1");
		
		// Compute the max
		int max = Integer.parseInt(strb.toString(), 2);
		
		// Generate all bits
		for (int i = 0; i <= max; i++)
			possibilities.add(new Bits(i, numberOfBits));
		
		return possibilities;
	}
	
	public static Function<Bits, HashMap<Bits, ElementaryFunction<Bits>>> generateAllNeighbors(final int length) {
		if (length <= 0)
			throw new IllegalArgumentException("Invalid length: " + length);
		
		return bits -> {
			HashMap<Bits, ElementaryFunction<Bits>> map = new HashMap<>();
			
			for (int i = 0; i < length; i++) {
				Bits b = new Bits(bits);
				b.invertBitFromLeft(i);
				final int final_i = i;
				map.put(b, new ElementaryFunction<Bits>() {
					@Override
					public Bits apply(Bits bits) {
						bits.invertBitFromLeft(final_i);
						return bits;
					}
					
					@Override
					public @NotNull Function<Bits, Bits> invert() {
						return bits1 -> {
							bits1.invertBitFromLeft(final_i);
							return bits1;
						};
					}
				});
			}
			
			return map;
		};
	}
	
	/* NUMBER OVERRIDES */
	
	@Override
	public int intValue() {
		return getValue();
	}
	
	@Override
	public long longValue() {
		return (long) intValue();
	}
	
	@Override
	public float floatValue() {
		return (float) intValue();
	}
	
	@Override
	public double doubleValue() {
		return (double) intValue();
	}
	
	/* COMPARABLE OVERRIDE */
	
	@Override
	public int compareTo(@NotNull Bits o) {
		return Integer.compare(intValue(), o.intValue());
	}
	
	/* GETTERS & SETTERS */
	
	@Contract(pure = true)
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
		
		// Check the limit
		//noinspection ResultOfMethodCallIgnored
		getBits();
	}
	
	@NotNull
	@Contract(pure = true)
	public String getBits() {
		StringBuilder bits = new StringBuilder(Integer.toBinaryString(getValue()));
		
		if (bits.length() > getLength())
			throw new BitLengthExceededException(getLength(), bits.length());
		
		while (bits.length() < getLength())
			bits.insert(0, "0");
		
		return bits.toString();
	}
	
	public void setBits(@NotNull String bits) {
		setValue(Integer.parseInt(bits, 2));
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		if (length <= 0)
			throw new IllegalArgumentException("Length cannot be less or equal to 0.");
		
		this.length = length;
	}
	
	/* OVERRIDES */
	
	@Override
	@Contract(value = "null -> false", pure = true)
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Bits)) return false;
		Bits bits1 = (Bits) o;
		return Objects.equals(getBits(), bits1.getBits());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(intValue());
	}
	
	@Override
	public String toString() {
		return getBits();
	}
}
