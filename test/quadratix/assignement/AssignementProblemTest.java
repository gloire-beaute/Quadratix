package quadratix.assignement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quadratix.ElementaryFunction;
import quadratix.SearchTestUtil;
import quadratix.combination.Combination;
import quadratix.data.CombinationGenerator;
import quadratix.stats.Stopwatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AssignementProblemTest {

    private AssignementProblem assignementProblem = new AssignementProblem();

    private static final String TAILLARD = "tai12.txt";
    private static final int NEIGHBORHOOD_TYPE = 0;
    /*
            0 : toutes les permutations entre 2 élements
            1 : n permutations aléatoires ou n = taille de taillard
            2 : toutes les permutations entre 2 élements à moins de d de distance ou d à fixer
     */

    // RECUIT
    private static final int MAX_ITERATION_COMPUTE_T0 = 10;
    private static final Integer INITIAL_TO = null;

    // TABU
    private static final int TABU_SIZE = 3;

    /**
     * Setup assignement problem with data from taillard instance
     */
    @BeforeEach
    void setUp() {
        try {
            assignementProblem.taillardInitializer(TAILLARD);
            Combination initialComb = new Combination();
            for (int i = 0; i < assignementProblem.getAssignmentData().getLength(); i++) {
                initialComb.add((long) i + 1);
            }
            assignementProblem.setInCombination(initialComb);
            assignementProblem.setNeighborsFunction(NEIGHBORHOOD_TYPE, assignementProblem.getAssignmentData().getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test 100 iterations for tabu algorithm. Compute average solution.
     */
    @Test
    void tabuAlgorithm() {
        try {
            float sum = 0;
            int min = Integer.MAX_VALUE;

            CombinationGenerator combinationGenerator = new CombinationGenerator(assignementProblem.getAssignmentData().getLength());
            ArrayList<Combination> combinationArrayList = combinationGenerator.readFile();

            for (Combination combination : combinationArrayList) {
                assignementProblem.setInCombination(combination);
                assignementProblem.tabuAlgortihm();
                assignementProblem.printOutput();
                int output = assignementProblem.getF().apply(assignementProblem.getOutCombination());
                sum += output;
                if (output < min) min = output;
            }

            System.out.println("\n");
            System.out.println("Average tabu " + sum / combinationArrayList.size());
            System.out.println("Minimum found " + min);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test 100 iterations for recuit algorithm. Compute average solution.
     */
    @Test
    void recuitAlgorithm() {
        try {
            float sum = 0;
            int min = Integer.MAX_VALUE;

            CombinationGenerator combinationGenerator = new CombinationGenerator(assignementProblem.getAssignmentData().getLength());
            ArrayList<Combination> combinationArrayList = combinationGenerator.readFile();

            for (Combination combination : combinationArrayList) {
                assignementProblem.setInCombination(combination);
                assignementProblem.recuitAlgortihm(null);
                sum += assignementProblem.getF().apply(assignementProblem.getOutCombination());
                int output = assignementProblem.getF().apply(assignementProblem.getOutCombination());
                sum += output;
                if (output < min) min = output;
            }

            System.out.println("\n");
            System.out.println("Average recuit " + sum / combinationArrayList.size());
            System.out.println("Minimum found " + min);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test 2 algorithms for current file
     */
    @Test
    void algorithms() {
        System.out.println(assignementProblem.getAssignmentData().toString());

        System.out.println("TABU algorithm");
        Stopwatch stopwatch = new Stopwatch(true);
        assignementProblem.tabuAlgortihm();
        assignementProblem.printOutput();
        stopwatch.stop();
        System.out.println("Execution time : " + stopwatch.elapsedMs() + " microsec");
        System.out.println("\n");

        System.out.println("RECUIT algorithm");
        stopwatch = new Stopwatch(true);
        assignementProblem.recuitAlgortihm();
        stopwatch.stop();
        System.out.println("Execution time : " + stopwatch.elapsedMs() + " microsec");
        System.out.println("\n");
    }

    @Test
    void computeTemperature() {
        ArrayList<Integer> deltaFs = new ArrayList<>(MAX_ITERATION_COMPUTE_T0);
        Function<Combination, HashMap<Combination, ElementaryFunction<Combination>>> neighborsGenerator = Combination.generateAllNeighbors();
        Function<Combination, Integer> f = assignementProblem.getF();

        for (int i = 0; i < MAX_ITERATION_COMPUTE_T0; i++) {
            // Generate randomly a combination
            Combination c = Combination.generateRandom(assignementProblem.getAssignmentData().getLength());

            // Generate all its neighbors
            ArrayList<Combination> neighbors = new ArrayList<>(neighborsGenerator.apply(c).keySet());

            if (!neighbors.isEmpty()) {
                // Select the worst neighbor (s.t. f(neighbor) is the biggest)
                Combination worstNeighbor = neighbors.get(0);
                for (Combination neighbor : neighbors) {
                    if (f.apply(neighbor) > f.apply(worstNeighbor))
                        worstNeighbor = neighbor;
                }
                // Compute deltaF
                deltaFs.add(Math.abs(f.apply(c) - f.apply(worstNeighbor)));
            }
        }

        assertTrue(deltaFs.size() > 1);
        System.out.println("Average of Δf size: " + deltaFs.size());

        // Compute the average of deltaF's
        double avg = deltaFs.stream().mapToDouble(a -> a).average().getAsDouble();

        // Compute t0 with a probability to accept neighbors of 80%
        double t0 = -avg / Math.log(0.8);
        System.out.println(String.format("t0 = %.4f°", t0));
    }

    /**
     * Test tabu list size impact
     */
    @Test
    public void tabuSizeTest() {
        try {

            for (int i = 0; i < /*SearchTestUtil.taillardFilenames.length*/ 4; i++) {
                System.out.println("-----------------");
                System.out.println("Run#" + i + " " + SearchTestUtil.taillardFilenames[i]);
                System.out.println("-----------------");
                assignementProblem.taillardInitializer(SearchTestUtil.taillardFilenames[i]);

                for (int j = 1; j < Math.pow(assignementProblem.getAssignmentData().getLength(), 2); j = j + 9) {
                    System.out.println("Tabu size " + j);
                    assignementProblem.setTabuSize(j);

                    assignementProblem.setInCombination(Combination.generateRandom(assignementProblem.getAssignmentData().getLength()));

                    assignementProblem.tabuAlgortihm();

                    assignementProblem.tabuAlgortihm(assignementProblem.getF().apply(assignementProblem.getOutCombination()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compute all results for each taillard instance
     */
    @Test
    public void taillardTest() {
        try {

            for (int i = 0; i < /*SearchTestUtil.taillardFilenames.length*/ 9; i++) {
                System.out.println("Run#" + SearchTestUtil.taillardFilenames[i]);
                assignementProblem.taillardInitializer(SearchTestUtil.taillardFilenames[i]);

                assignementProblem.setInCombination(Combination.generateRandom(assignementProblem.getAssignmentData().getLength()));

                //TABU
                Stopwatch stopwatch = new Stopwatch(true);
                assignementProblem.tabuAlgortihm();
                stopwatch.stop();
                System.out.print("Tabu ");
                System.out.print(" Best " + assignementProblem.getF().apply(assignementProblem.getOutCombination()));
                System.out.println(" | Time " + stopwatch.elapsedMs() + " ms");

                //RECUIT
                stopwatch = new Stopwatch(true);
                assignementProblem.recuitAlgortihm();
                stopwatch.stop();
                System.out.print("Recuit ");
                System.out.print(" Best " + assignementProblem.getF().apply(assignementProblem.getOutCombination()));
                System.out.println(" | Time " + stopwatch.elapsedMs() + " ms");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}