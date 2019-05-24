package quadratix.stats;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class Irregular {
	
	@NotNull
	private static Random generator = new Random(System.currentTimeMillis());
	
	@Nullable
	public static <T> Object range(Class<T> clazz, double min, boolean minIncluded, double max, boolean maxIncluded) {
		if (!minIncluded)
			min = Math.ceil(++min);
		
		if (!maxIncluded)
			max = Math.floor(--max);
		
		if (min > max)
			throw new IllegalArgumentException("min must be lesser than max");
		
		if (min == max)
			return null;
		
		Object random = null;
		
		if (clazz.isAssignableFrom(int.class) || clazz.isAssignableFrom(Integer.class))
			random = (int) ((Math.abs(generator.nextInt()) % (((int) max) + 1 - ((int) min))) + ((int) min));
		else if (clazz.isAssignableFrom(short.class) || clazz.isAssignableFrom(Short.class))
			random = (short) ((Math.abs(generator.nextInt()) % (((short) max) + 1 - ((short) min))) + ((short) min));
		else if (clazz.isAssignableFrom(byte.class) || clazz.isAssignableFrom(Byte.class))
			random = (byte) ((Math.abs(generator.nextInt()) % (((byte) max) + 1 - ((byte) min))) + ((byte) min));
		else if (clazz.isAssignableFrom(long.class) || clazz.isAssignableFrom(Long.class))
			random = (long) ((Math.abs(generator.nextLong()) % (((long) max) + 1L - ((long) min))) + ((long) min));
		else if (clazz.isAssignableFrom(char.class) || clazz.isAssignableFrom(Character.class))
			random = (char) ((Math.abs(generator.nextLong()) % (((char) max) + 1 - ((char) min))) + ((char) min));
		else if (clazz.isAssignableFrom(boolean.class) || clazz.isAssignableFrom(Boolean.class))
			random = (boolean) generator.nextBoolean();
		else if (clazz.isAssignableFrom(float.class) || clazz.isAssignableFrom(Float.class))
			random = (float) ((Math.abs(generator.nextFloat()) % (((float) max) + 1f - ((float) min))) + ((float) min));
		else if (clazz.isAssignableFrom(double.class) || clazz.isAssignableFrom(Double.class))
			random = (double) ((Math.abs(generator.nextDouble()) % (((double) max) + 1d - ((double) min))) + ((double) min));
		
		
		return random;
	}
	
	public static int rangeInt(int min, boolean minIncluded, int max, boolean maxIncluded) {
		Object o_result = range(int.class, min, minIncluded, max, maxIncluded);
		int result;
		
		if (o_result == null)
			result = min;
		else
			result = (int) o_result;
		
		return result;
	}
	
	public static short rangeShort(short min, boolean minIncluded, short max, boolean maxIncluded) {
		Object o_result = range(short.class, min, minIncluded, max, maxIncluded);
		short result;
		
		if (o_result == null)
			result = min;
		else
			result = (short) o_result;
		
		return result;
	}
	
	public static byte rangeByte(byte min, boolean minIncluded, byte max, boolean maxIncluded) {
		Object o_result = range(byte.class, min, minIncluded, max, maxIncluded);
		byte result;
		
		if (o_result == null)
			result = min;
		else
			result = (byte) o_result;
		
		return result;
	}
	
	public static long rangeLong(long min, boolean minIncluded, long max, boolean maxIncluded) {
		Object o_result = range(long.class, min, minIncluded, max, maxIncluded);
		long result;
		
		if (o_result == null)
			result = min;
		else
			result = (long) o_result;
		
		return result;
	}
	
	public static char rangeChar(char min, boolean minIncluded, char max, boolean maxIncluded) {
		Object o_result = range(char.class, min, minIncluded, max, maxIncluded);
		char result;
		
		if (o_result == null)
			result = min;
		else
			result = (char) o_result;
		
		return result;
	}
	
	public static boolean rangeBoolean() {
		Object o_result = range(char.class, 0, true, 1, true);
		boolean result;
		
		if (o_result == null)
			result = false;
		else
			result = ((char) o_result) != 0;
		
		return result;
	}
	
	public static float rangeFloat(float min, boolean minIncluded, float max, boolean maxIncluded) {
		Object o_result = range(float.class, min, minIncluded, max, maxIncluded);
		float result;
		
		if (o_result == null)
			result = min;
		else
			result = (float) o_result;
		
		return result;
	}
	
	public static double rangeDouble(double min, boolean minIncluded, double max, boolean maxIncluded) {
		Object o_result = range(double.class, min, minIncluded, max, maxIncluded);
		double result;
		
		if (o_result == null)
			result = min;
		else
			result = (double) o_result;
		
		return result;
	}
	
	public static @NotNull String rangeString(int minLength, int maxLength, boolean includeNonAlphabeticalCharacter, boolean includeNonPrintableCharacter) {
		int length = rangeInt(minLength, true, maxLength, true);
		
		StringBuilder build = new StringBuilder();
		
		for (int i = 0; i < length; i++) {
			char min = 65;
			char max = 90;
			
			if (includeNonAlphabeticalCharacter) {
				min = 32;
				max = 126;
			}
			
			if (includeNonPrintableCharacter) {
				min = 0;
				max = 127;
			}
			
			char randomChar = rangeChar(min, true, max, true);
			
			// If randomChar c ['A' ; 'Z'], there is a chance of 1/2 to turn it into a lowercase character
			if ('A' <= randomChar && randomChar <= 'Z' && rangeBoolean())
				randomChar = Character.toLowerCase(randomChar);
			
			build.append(randomChar);
		}
		
		return build.toString();
	}
	
	public static void setSeed(long seed) {
		generator.setSeed(seed);
	}
}
