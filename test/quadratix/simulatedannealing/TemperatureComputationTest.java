package quadratix.simulatedannealing;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import quadratix.ElementaryFunction;
import quadratix.NumberOperations;
import quadratix.assignement.AssignementProblem;
import quadratix.bits.Bits;
import quadratix.combination.Combination;
import quadratix.data.AssignmentData;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static quadratix.SearchTestUtil.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TemperatureComputationTest {
	
	Function<Combination, Integer> f;
	Function<Combination, HashMap<Combination, ElementaryFunction<Combination>>> V;
	final NumberOperations<Integer> intOps = NumberOperations.getIntegerOperations();
	final int[] iterations = {1, 2, 3, 10, 20, 50, 100, 500, 1000, 2000, 5000, 10000, 100000};
	
	double computeTemperature(@NotNull String filename, int nbIteration) throws IOException {
		AssignementProblem problem = new AssignementProblem();
		problem.taillardInitializer(filename);
		f = problem.getF();
		V = problem.getV();
		return SimulatedAnnealing.computeTemperature(
				f,
				V,
				intOps,
				v -> Combination.generateRandom(problem.getAssignmentData().getLength()),
				i -> (double) i,
				1000
		);
	}
	
	void testHelper(@NotNull String filename) throws IOException {
		if (!filename.endsWith(".txt"))
			filename += ".txt";
		for (int iteration : iterations)
			computeTemperature(filename, iteration);
	}
	
	@Test
	void tai12() throws IOException {
		testHelper("tai12");
	}
}
