package quadratix;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

class TabuTest {
	
	static final int NB_BITS = 4;
	
	Tabu<Bits, Integer> tabu;
	Function<Bits, Integer> f;
	Function<Bits, HashMap<Bits, ElementaryFunction<Bits>>> V;
	NumberOperations<Integer> intOps;
	
	@BeforeEach
	void setUp() {
		tabu = new Tabu<>();
		
		final HashMap<Bits, Integer> mapFitness = new HashMap<Bits, Integer>() {
			{
				put(new Bits(0, NB_BITS), 12);
				put(new Bits(1, NB_BITS), 5);
				put(new Bits(2, NB_BITS), 9);
				put(new Bits(3, NB_BITS), 2);
				put(new Bits(4, NB_BITS), 11);
				put(new Bits(5, NB_BITS), 3);
				put(new Bits(6, NB_BITS), 8);
				put(new Bits(7, NB_BITS), 7);
				put(new Bits(8, NB_BITS), 6);
				put(new Bits(9, NB_BITS), 10);
				put(new Bits(10, NB_BITS), 6);
				put(new Bits(11, NB_BITS), 4);
				put(new Bits(12, NB_BITS), 0);
				put(new Bits(13, NB_BITS), 11);
				put(new Bits(14, NB_BITS), 1);
				put(new Bits(15, NB_BITS), 5);
			}
		};
		final HashMap<Integer, Set<Bits>> reversedMapFitness = new HashMap<Integer, Set<Bits>>() {
			{
				for (Entry<Bits, Integer> entry : mapFitness.entrySet()) {
					Set<Bits> set = getOrDefault(entry.getKey(), new HashSet<>());
					set.add(entry.getKey());
					put(entry.getValue(), set);
				}
			}
		};
		
		f = bits -> mapFitness.getOrDefault(bits, 0);
		
		V = bits -> {
			HashMap<Bits, ElementaryFunction<Bits>> map = new HashMap<>();
			
			for (int i = 0; i < NB_BITS; i++) {
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
		
		intOps = new NumberOperations<Integer>() {
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
				return 0;
			}
		};
	}
	
	@Test
	void search() {
		Bits b = tabu.search(f, new Bits(0, NB_BITS), V, intOps, 5.0);
		System.out.println("Result b: " + b);
	}
}