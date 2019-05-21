package quadratix;

import javafx.util.Pair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import quadratix.bits.Bits;
import quadratix.tabu.Tabu;

import java.util.HashMap;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static quadratix.SearchTestUtil.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TabuTest {
	
	Tabu<Bits, Integer> tabu;
	Function<Bits, Integer> f;
	Function<Bits, HashMap<Bits, ElementaryFunction<Bits>>> V_bits;
	NumberOperations<Integer> intOps;
	
	@BeforeEach
	void setup() {
		V_bits = Bits.generateAllNeighbors();
		intOps = NumberOperations.getIntegerOperations();
	}
	
	@Test
	@Order(1)
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
	@Order(2)
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
		System.out.println("        =>" + b.getValue() + " in decimal");
	}
	
	@Test
	@Order(3)
	void slideExercise() {
		tabu = new Tabu<>();
		
		f = getSlideExerciseFitness();
		
		Bits b = tabu.search(f, new Bits(0, NB_BITS), V_bits, intOps, 5.0, 3);
		System.out.println("Result: f(" + b + ") = " + f.apply(b));
		System.out.println("        =>" + b.getValue() + " in decimal");
		
		assertEquals("1100", b.getBits());
		assertEquals(0, f.apply(b));
	}
	
	@Test
	@Order(4)
	void tdExercise() {
		Tabu<Pair<Integer, Integer>, Double> tabu = new Tabu<>();
		
		Function<Pair<Integer, Integer>, Double> f = pair -> (double) - 3 * pair.getKey() - 2 * pair.getValue();
		
		// Create the neighborhood generator
		Function<Pair<Integer, Integer>, HashMap<Pair<Integer, Integer>, ElementaryFunction<Pair<Integer, Integer>>>> V =
				(final Pair<Integer, Integer> x) -> {
					HashMap<Pair<Integer, Integer>, ElementaryFunction<Pair<Integer, Integer>>> v = new HashMap<>();
					
					// Create a neighbor, check if it's in the feasible region, and add it to the map with its function
					// (x-1 ; y)
					Pair<Integer, Integer> p = new Pair<>(x.getKey()-1, x.getValue());
					if (tdExerciseCheckDataInRegion(p)) {
						v.put(p, new ElementaryFunction<Pair<Integer, Integer>>() {
							@Override
							public Pair<Integer, Integer> apply(Pair<Integer, Integer> pair) {
								return new Pair<>(pair.getKey()-1, pair.getValue());
							}
							
							@Override
							public @NotNull Function<Pair<Integer, Integer>, Pair<Integer, Integer>> invert() {
								return pair -> new Pair<>(pair.getKey()+1, pair.getValue());
							}
						});
					}
					
					// (x+1 ; y)
					p = new Pair<>(x.getKey()+1, x.getValue());
					if (tdExerciseCheckDataInRegion(p)) {
						v.put(p, new ElementaryFunction<Pair<Integer, Integer>>() {
							@Override
							public Pair<Integer, Integer> apply(Pair<Integer, Integer> pair) {
								return new Pair<>(pair.getKey()+1, pair.getValue());
							}
							
							@Override
							public @NotNull Function<Pair<Integer, Integer>, Pair<Integer, Integer>> invert() {
								return pair -> new Pair<>(pair.getKey()-1, pair.getValue());
							}
						});
					}
					
					// (x ; y-1)
					p = new Pair<>(x.getKey(), x.getValue()-1);
					if (tdExerciseCheckDataInRegion(p)) {
						v.put(p, new ElementaryFunction<Pair<Integer, Integer>>() {
							@Override
							public Pair<Integer, Integer> apply(Pair<Integer, Integer> pair) {
								return new Pair<>(pair.getKey(), pair.getValue()-1);
							}
							
							@Override
							public @NotNull Function<Pair<Integer, Integer>, Pair<Integer, Integer>> invert() {
								return pair -> new Pair<>(pair.getKey(), pair.getValue()-1);
							}
						});
					}
					
					// (x ; y+1)
					p = new Pair<>(x.getKey(), x.getValue()+1);
					if (tdExerciseCheckDataInRegion(p)) {
						v.put(p, new ElementaryFunction<Pair<Integer, Integer>>() {
							@Override
							public Pair<Integer, Integer> apply(Pair<Integer, Integer> pair) {
								return new Pair<>(pair.getKey(), pair.getValue()+1);
							}
							
							@Override
							public @NotNull Function<Pair<Integer, Integer>, Pair<Integer, Integer>> invert() {
								return pair -> new Pair<>(pair.getKey(), pair.getValue()+1);
							}
						});
					}
					
					return v;
				};
		
		Pair<Integer, Integer> pair = tabu.search(f, new Pair<>(1, 1), V, NumberOperations.getDoubleOperations(), 0.5, 1, 100);
		System.out.println("Result: f(" + pair.getKey() + " ; " + pair.getValue() + ") = " + f.apply(pair));
		assertEquals(4, pair.getKey());
		assertEquals(4, pair.getValue());
		assertEquals(-20, f.apply(pair));
	}
	
	@Contract(pure = true)
	private boolean tdExerciseCheckDataInRegion(final int x, final int y) {
		return (2 * x + y <= 12) &&
				(2 * y <= 8 + x) &&
				(2 * x <= 8 + y) &&
				(x + y >= 2);
	}
	private boolean tdExerciseCheckDataInRegion(@NotNull final Pair<Integer, Integer> pair) {
		return tdExerciseCheckDataInRegion(pair.getKey(), pair.getValue());
	}
}