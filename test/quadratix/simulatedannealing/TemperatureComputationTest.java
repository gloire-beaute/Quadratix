package quadratix.simulatedannealing;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import quadratix.ElementaryFunction;
import quadratix.NumberOperations;
import quadratix.assignement.AssignementProblem;
import quadratix.combination.Combination;
import quadratix.stats.Stopwatch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

import static quadratix.SearchTestUtil.taillardFilenames;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TemperatureComputationTest {
	
	private Function<Combination, Integer> f;
	private Function<Combination, HashMap<Combination, ElementaryFunction<Combination>>> V;
	private final NumberOperations<Integer> intOps = NumberOperations.getIntegerOperations();
	private final int[] iterations = {1, 2, 3, 10, 20, 50, 100, 500, 1000/*, 2000, 5000, 10000, 100000*/};
	
	private double computeTemperature(@NotNull final String filename, final int nbIteration) throws IOException {
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
				nbIteration
		);
	}
	
	
	/**
	 * Test the taillard instance by computing several time
	 * {@link SimulatedAnnealing#computeTemperature(Function, Function, NumberOperations, Function, Function, int)} with
	 * different numbers of iterations.
	 *
	 * @param filename The filename where the taillard instance is stored.
	 * @return Return a map from the number of iterations to the result and the execution time (in milliseconds).
	 * @throws IOException Throw when the taillard instance could not be parsed.
	 */
	@NotNull
	private HashMap<Integer, Pair<Double, Long>> testHelper(@NotNull String filename) throws IOException {
		HashMap<Integer, Pair<Double, Long>> iterResult = new HashMap<>();
		Stopwatch time = new Stopwatch();
		if (!filename.endsWith(".txt"))
			filename += ".txt";
		for (int iteration : iterations) {
			time.start();
			double result = computeTemperature(filename, iteration);
			time.stop();
			iterResult.put(iteration, new Pair<>(result, time.elapsedMs()));
		}
		
		return iterResult;
	}
	
	@Test
	void testAllTaillards() throws IOException {
		HashMap<Integer, Pair<Double, Long>> map;
		StringBuilder results = new StringBuilder();
		StringBuilder times = new StringBuilder();
		
		results.append(',');
		times.append(',');
		System.out.print(String.format("%11s", "|"));
		for (int iteration : iterations) {
			results.append(iteration).append(',');
			times.append(iteration).append(',');
			System.out.print(String.format("%13d|", iteration));
		}
		results.append('\n');
		times.append('\n');
		System.out.println();
		
		try {
			for (String filename : taillardFilenames) {
				results.append(filename).append(',');
				times.append(filename).append(',');
				System.out.print(String.format("%10s|", filename));
				map = testHelper(filename);
				for (int iteration : iterations) {
					results.append(map.get(iteration).getKey()).append(',');
					times.append(map.get(iteration).getValue()).append(',');
					System.out.print(String.format("%13s|", String.format("%.5f", map.get(iteration).getKey())));
				}
				results.append('\n');
				times.append('\n');
				System.out.println();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println();
		System.out.println("results.csv");
		System.out.println(results.toString());
		System.out.println();
		System.out.println("times.csv");
		System.out.println(times.toString());
		Files.write(Paths.get("out/results.csv"), Arrays.asList(results.toString().split("\\n")));
		Files.write(Paths.get("out/times.csv"), Arrays.asList(times.toString().split("\\n")));
	}
}
