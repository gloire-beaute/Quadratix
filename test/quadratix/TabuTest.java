package quadratix;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quadratix.bits.Bits;
import quadratix.tabu.Tabu;

import java.util.HashMap;
import java.util.function.Function;

class TabuTest {
	
	static final int NB_BITS = 4;
	
	Tabu<Bits, Integer> tabu;
	Function<Bits, Integer> f;
	Function<Bits, HashMap<Bits, ElementaryFunction<Bits>>> V_bits;
	NumberOperations<Integer> intOps;
	
	@BeforeEach
	void setup() {
		V_bits = Bits.generateAllNeighbors(NB_BITS);
		
		intOps = NumberOperations.getIntegerOperations();
	}
	
	@Test
	void listNeighbors() {
		System.out.println("All neighbors in V_bits:");
		for (Bits b : Bits.generateAllPossibility(NB_BITS)) {
			System.out.println(b + ":");
			HashMap<Bits, ElementaryFunction<Bits>> vb = V_bits.apply(b);
			for (Bits k : vb.keySet())
				System.out.println("\t" + k);
		}
		System.out.println();
	}
	
	@Test
	void slideExercise() {
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
		
		f = bits -> mapFitness.getOrDefault(bits, 0);
		
		Bits b = tabu.search(f, new Bits(0, NB_BITS), V_bits, intOps, 5.0, 3);
		System.out.println("Result: f(" + b + ") = " + f.apply(b));
		System.out.println("(b = \"" + b.getBits() + "\"<2> = \"" + b.getValue() + "\"<10>)");
		
		/*assertEquals("1100", b.getBits());
		assertEquals(0, f.apply(b));*/
	}
	
	@Test
	void classExercise() {
		tabu = new Tabu<>();
		
		final HashMap<Bits, Integer> mapFitness = new HashMap<Bits, Integer>() {
			{
				put(new Bits(0, NB_BITS), 8);
				put(new Bits(1, NB_BITS), 2);
				put(new Bits(2, NB_BITS), 9);
				put(new Bits(3, NB_BITS), 3);
				put(new Bits(4, NB_BITS), 1);
				put(new Bits(5, NB_BITS), 0);
				put(new Bits(6, NB_BITS), 6);
				put(new Bits(7, NB_BITS), 4);
				put(new Bits(8, NB_BITS), 7);
				put(new Bits(9, NB_BITS), 9);
				put(new Bits(10, NB_BITS), 5);
				put(new Bits(11, NB_BITS), 6);
				put(new Bits(12, NB_BITS), 9);
				put(new Bits(13, NB_BITS), 4);
				put(new Bits(14, NB_BITS), 3);
				put(new Bits(15, NB_BITS), 2);
			}
		};
		
		f = bits -> mapFitness.getOrDefault(bits, 0);
		
		Bits b = tabu.search(f, new Bits(15, NB_BITS), V_bits, intOps, 0.5, 1);
		System.out.println("Result: f(" + b + ") = " + f.apply(b));
		System.out.println("(b = \"" + b.getBits() + "\"<2> = \"" + b.getValue() + "\"<10>)");
	}
}