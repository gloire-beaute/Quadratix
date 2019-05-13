package quadratix.simulatedannealing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quadratix.ElementaryFunction;
import quadratix.NumberOperations;
import quadratix.bits.Bits;

import java.util.HashMap;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static quadratix.SearchTestUtil.*;

class SimulatedAnnealingTest {
	
	SimulatedAnnealing<Bits, Integer> simulatedAnnealing;
	Function<Bits, Integer> f;
	Function<Bits, HashMap<Bits, ElementaryFunction<Bits>>> V_bits;
	NumberOperations<Integer> intOps;
	
	@BeforeEach
	void setup() {
		V_bits = Bits.generateAllNeighbors();
		intOps = NumberOperations.getIntegerOperations();
	}
	
	@Test
	void slideExercise() {
		simulatedAnnealing = new SimulatedAnnealing<>();
		
		f = getSlideExerciseFitness();
		
		Bits b = simulatedAnnealing.search(f, new Bits(0, NB_BITS), V_bits, intOps, 5.0, 100, 100, 0.1);
		System.out.println("Result: f(" + b + ") = " + f.apply(b));
		System.out.println("(b = \"" + b.getBits() + "\"<2> = \"" + b.getValue() + "\"<10>)");
		
		assertEquals(12, b.intValue());
		assertEquals(0, f.apply(b));
	}
}