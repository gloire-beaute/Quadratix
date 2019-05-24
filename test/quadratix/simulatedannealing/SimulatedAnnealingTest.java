package quadratix.simulatedannealing;

import javafx.util.Pair;
import org.junit.jupiter.api.*;
import quadratix.ElementaryFunction;
import quadratix.NumberOperations;
import quadratix.bits.Bits;

import java.util.HashMap;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static quadratix.SearchTestUtil.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
	@Order(1)
	void slideExercise() {
		simulatedAnnealing = new SimulatedAnnealing<>();
		
		f = getSlideExerciseFitness();
		
		Bits b = simulatedAnnealing.search(f, new Bits(0, NB_BITS), V_bits, intOps, 5.0, 100, 100, 0.1);
		System.out.println("Result: f(" + b + ") = " + f.apply(b));
		System.out.println("        =>" + b.getValue() + " in decimal");
		
		assertEquals(12, b.intValue());
		assertEquals(0, f.apply(b));
	}
	
	@Test
	@Order(2)
	void tdExercise() {
		SimulatedAnnealing<Pair<Integer, Integer>, Double> simulatedAnnealing = new SimulatedAnnealing<>();
		
		Function<Pair<Integer, Integer>, Double> f = getTDExerciseFitness();
		Function<Pair<Integer, Integer>, HashMap<Pair<Integer, Integer>, ElementaryFunction<Pair<Integer, Integer>>>> V = getTDExerciseV();
		
		Pair<Integer, Integer> pair = simulatedAnnealing.search(f, new Pair<>(1, 1), V, NumberOperations.getDoubleOperations(), 5.0, 100, 100, 0.1);
		System.out.println("Result: f((" + pair.getKey() + " ; " + pair.getValue() + ")) = " + f.apply(pair));
		
		assertEquals(4, pair.getKey());
		assertEquals(4, pair.getValue());
		assertEquals(-20, f.apply(pair));
	}
	
	@Test
	@Order(3)
	void computeTemperature() {
		// Compute temperature for slide exercise
		double t0 = SimulatedAnnealing.computeTemperature(
				getSlideExerciseFitness(),
				V_bits,
				intOps,
				v -> Bits.generateRandom(NB_BITS),
				i -> (double) i,
				1000
		);
		System.out.println(String.format("t0 = %.4f°", t0));
		assertTrue(t0 > 0);
	}
}