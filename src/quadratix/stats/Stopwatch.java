package quadratix.stats;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.Observable;

/**
 * Class that creates a stopwatch to measure time, in millisecond or nanosecond.
 * @see System#currentTimeMillis()
 * @see System#nanoTime()
 */
public class Stopwatch extends Observable implements Serializable, Cloneable {
	
	private long beginMs = 0;
	private long beginNs = 0;
	private long endMs = 0;
	private long endNs = 0;
	private boolean activated = false;
	
	//region CONSTRUCTORS
	
	/**
	 * Constructor for stopwatch.
	 * @param activateNow If {@code true}, activate the stopwatch right after its creation. Default is {@code false}.
	 */
	public Stopwatch(boolean activateNow) {
		if (activateNow)
			start();
	}
	/**
	 * Constructor for stopwatch. Does not activate the stopwatch. See {@link #start()} to launch it.
	 * @see #start()
	 */
	public Stopwatch() {
		this(false);
	}
	
	//endregion
	
	//region METHODS
	
	/**
	 * Start the stopwatch by initializing {@code beginNs} and {@code beginMs}. To stop it, please see {@link #stop()}.
	 * @see #stop()
	 * @see #elapsedMs()
	 * @see #elapsedNs()
	 */
	public void start() {
		beginNs = System.nanoTime();
		beginMs = System.currentTimeMillis();
		setActivated(true);
	}
	
	/**
	 * Stop the stopwatch by initializing {@code endNs} and {@code endMs}.
	 * @return Return the time elapsed in millisecond (see {@link #elapsedMs()}).
	 * @see #start()
	 * @see #elapsedMs()
	 * @see #elapsedNs()
	 */
	public long stop() {
		endNs = System.nanoTime();
		endMs = System.currentTimeMillis();
		setActivated(false);
		notifyObservers();
		setChanged();
		return elapsedMs();
	}
	
	/**
	 * Compute the time elapsed in milliseconds between {@link #start()} and {@link #stop()}. If the stopwatch is still
	 * activated, the time between {@link #start()} and now is computed instead.
	 * @return Return the time elapsed in milliseconds.
	 */
	public long elapsedMs() {
		if (!isActivated())
			return getEndMs() - getBeginMs();
		else // If stopwatch still running, take the current ms as the 'end'
			return System.currentTimeMillis() - beginMs;
	}
	
	/**
	 * Compute the time elapsed in nanoseconds between {@link #start()} and {@link #stop()}. If the stopwatch is still
	 * activated, the time between {@link #start()} and now is computed instead.
	 * @return Return the time elapsed in nanoseconds.
	 */
	public long elapsedNs() {
		if (!isActivated())
			return getEndNs() - getBeginNs();
		else// If stopwatch still running, take the current ns as the 'end'
			return System.nanoTime() - beginNs;
	}
	
	//endregion
	
	//region STATIC METHODS
	
	/**
	 * Measure the time to getNeighborhoods {@code function}.
	 * @param function The function to measure.
	 * @param inMillisecond If {@code true}, return the result in milliseconds. Otherwise in nanoseconds.
	 * @return Return the elapsed time between the beginning of the function passed as argument and its end.
	 */
	public static long measure(@NotNull Runnable function, boolean inMillisecond) {
		Stopwatch stopwatch = new Stopwatch(true);
		function.run();
		stopwatch.stop();
		if (inMillisecond)
			return stopwatch.elapsedMs();
		else
			return stopwatch.elapsedNs();
	}
	
	/**
	 * Measure the time to getNeighborhoods {@code function}.
	 * @param function The function to measure.
	 * @return Return the elapsed time between the beginning of the function passed as argument and its end, in
	 * milliseconds.
	 */
	public static long measureMs(@NotNull Runnable function) {
		return measure(function, true);
	}
	
	/**
	 * Measure the time to getNeighborhoods {@code function}.
	 * @param function The function to measure.
	 * @return Return the elapsed time between the beginning of the function passed as argument and its end, in
	 * nanoseconds.
	 */
	public static long measureNs(@NotNull Runnable function) {
		return measure(function, false);
	}
	
	//endregion
	
	//region GETTERS & SETTERS
	
	public long getBeginMs() {
		return beginMs;
	}
	
	protected void setBeginMs(long beginMs) {
		this.beginMs = beginMs;
	}
	
	public long getBeginNs() {
		return beginNs;
	}
	
	protected void setBeginNs(long beginNs) {
		this.beginNs = beginNs;
	}
	
	public long getEndMs() {
		return endMs;
	}
	
	protected void setEndMs(long endMs) {
		this.endMs = endMs;
	}
	
	public long getEndNs() {
		return endNs;
	}
	
	protected void setEndNs(long endNs) {
		this.endNs = endNs;
	}
	
	public boolean isActivated() {
		return activated;
	}
	
	protected void setActivated(boolean activated) {
		this.activated = activated;
	}
	
	//endregion
	
	//region OBJECT OVERRIDES
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Stopwatch)) return false;
		Stopwatch stopwatch = (Stopwatch) o;
		return getBeginMs() == stopwatch.getBeginMs() &&
				getBeginNs() == stopwatch.getBeginNs() &&
				getEndMs() == stopwatch.getEndMs() &&
				getEndNs() == stopwatch.getEndNs() &&
				isActivated() == stopwatch.isActivated();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getBeginMs(), getBeginNs(), getEndMs(), getEndNs(), isActivated());
	}
	
	@Override
	public String toString() {
		return "Stopwatch{time elapsed in ms: " + elapsedMs() + ", in ns: " + elapsedNs() + "}";
	}
	
	//endregion
}
