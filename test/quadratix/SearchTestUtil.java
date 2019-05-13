package quadratix;

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
}
