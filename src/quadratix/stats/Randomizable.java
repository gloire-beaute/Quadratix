package quadratix.stats;

/**
 * Interface that implements {@link #generateRandom()}.
 * @param <R> The class type that implements this interface.
 */
public interface Randomizable<R> {
	
	/**
	 * Generate a random instance of the class.
	 * @return A random instance.
	 */
	R generateRandom();
}
