package quadratix.stats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StopwatchTest {
	
	Stopwatch stopwatch;
	
	@BeforeEach
	void setUp() {
		stopwatch = new Stopwatch();
	}
	
	@Test
	public synchronized void elapsed() throws InterruptedException {
		stopwatch.start();
		Thread.sleep(1000);
		stopwatch.stop();
		assertTrue(998L <= stopwatch.elapsedMs() && stopwatch.elapsedMs() <= 1002L);
		assertTrue(998000000L <= stopwatch.elapsedNs() && stopwatch.elapsedNs() <= 1002000000L);
	}
}