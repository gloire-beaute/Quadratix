package quadratix;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
				put(new Bits(0), 12);
				put(new Bits(1), 5);
				put(new Bits(2), 9);
				put(new Bits(3), 2);
				put(new Bits(4), 11);
				put(new Bits(5), 3);
				put(new Bits(6), 8);
				put(new Bits(7), 7);
				put(new Bits(8), 6);
				put(new Bits(9), 10);
				put(new Bits(10), 6);
				put(new Bits(11), 4);
				put(new Bits(12), 0);
				put(new Bits(13), 11);
				put(new Bits(14), 1);
				put(new Bits(15), 5);
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
		
		V = new Function<Bits, HashMap<Bits, ElementaryFunction<Bits>>>() {
			@Override
			public HashMap<Bits, ElementaryFunction<Bits>> apply(Bits bits) {
				HashMap<Bits, ElementaryFunction<Bits>> map = new HashMap<>();
				for (int i = 0; i < NB_BITS; i++) {
				
				}
				return null;
			}
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
		tabu.search(f, Bits.getOperations().getZero(), null, intOps, 5.0);
	}
}