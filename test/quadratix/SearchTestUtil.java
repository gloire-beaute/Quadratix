package quadratix;

import javafx.util.Pair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import quadratix.bits.Bits;

import java.util.HashMap;
import java.util.function.Function;

public class SearchTestUtil {
	
	public static final int NB_BITS = 4;
	
	@NotNull
	@Contract(" -> new")
	public static HashMap<Bits, Integer> getSlideExerciseMapFitness() {
		return new HashMap<Bits, Integer>() {
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
	}
	
	@NotNull
	public static Function<Bits, Integer> getSlideExerciseFitness() {
		final HashMap<Bits, Integer> mapFitness = getSlideExerciseMapFitness();
		return bits -> mapFitness.getOrDefault(bits, 0);
	}
	
	@Contract(pure = true)
	public static Function<Pair<Integer, Integer>, Double> getTDExerciseFitness() {
		return pair -> (double) - 3 * pair.getKey() - 2 * pair.getValue();
	}
	
	public static Function<Pair<Integer, Integer>, HashMap<Pair<Integer, Integer>, ElementaryFunction<Pair<Integer, Integer>>>> getTDExerciseV() {
		return (final Pair<Integer, Integer> x) -> {
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
	}
	
	@Contract(pure = true)
	private static boolean tdExerciseCheckDataInRegion(final int x, final int y) {
		return (2 * x + y <= 12) &&
				(2 * y <= 8 + x) &&
				(2 * x <= 8 + y) &&
				(x + y >= 2);
	}
	private static boolean tdExerciseCheckDataInRegion(@NotNull final Pair<Integer, Integer> pair) {
		return tdExerciseCheckDataInRegion(pair.getKey(), pair.getValue());
	}
}
