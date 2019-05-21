package quadratix;

public class Stopwatch {
	
	private long begin = 0;
	private long end = 0;
	private boolean activated = false;
	
	public Stopwatch(boolean activateNow) {
		if (activateNow)
			start();
	}
	public Stopwatch() {
		this(false);
	}
	
	//region METHODS
	
	public void start() {
		setActivated(true);
		setBegin(System.currentTimeMillis());
	}
	
	public long stop() {
		setEnd(System.currentTimeMillis());
		setActivated(false);
		return elapsed();
	}
	
	public long elapsed() {
		return getEnd() - getBegin();
	}
	
	//endregion
	
	//region GETTERS & SETTERS
	
	public long getBegin() {
		return begin;
	}
	
	protected void setBegin(long begin) {
		this.begin = begin;
	}
	
	public long getEnd() {
		return end;
	}
	
	protected void setEnd(long end) {
		this.end = end;
	}
	
	public boolean isActivated() {
		return activated;
	}
	
	protected void setActivated(boolean activated) {
		this.activated = activated;
	}
	
	//endregion
}
