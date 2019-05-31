package quadratix.assignement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quadratix.ElementaryFunction;
import quadratix.SearchTestUtil;
import quadratix.combination.Combination;
import quadratix.combination.CombinationGenerator;
import quadratix.data.LogFileHandler;
import quadratix.stats.Stopwatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AssignementProblemTest {

    private AssignementProblem assignementProblem = new AssignementProblem();

    private static final String TAILLARD_FILENAME = "tai12.txt";
    private static final int NEIGHBORHOOD_TYPE = 1;
    /*
            0 : toutes les permutations entre 2 élements
            1 : n permutations aléatoires ou n = taille de taillard
            2 : toutes les permutations entre 2 élements à moins de d de distance ou d à fixer
     */

    // RECUIT
    private static final int MAX_ITERATION_COMPUTE_T0 = 100;
    private static final Double INITIAL_TO = null; // if null => t0 will be auto-computed by recuit algorithm
    private static final Double MU = 0.1;

    // TABU
    private static final int TABU_SIZE = 3;

    /**
     * Setup assignement problem with data from taillard instance
     */
    @BeforeEach
    void setUp() {
        try {
            assignementProblem.taillardInitializer(TAILLARD_FILENAME);
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
     * Test 100 iterations for tabu algorithm. Compute average solution, best solution and time.
     */
    @Test
    void tabuAlgorithm() {
        System.out.println("\nTabu algorithm test on 100 solutions\n");
        try {
            float sum = 0;
            int min = Integer.MAX_VALUE;
            int optimumReached = 0;
            ArrayList<Integer> outputs = new ArrayList<>();

            CombinationGenerator combinationGenerator = new CombinationGenerator(assignementProblem.getAssignmentData().getLength());
            ArrayList<Combination> combinationArrayList = combinationGenerator.readFile();

            Stopwatch stopwatch = new Stopwatch(true);
            for (Combination combination : combinationArrayList) {
                assignementProblem.setInCombination(combination);
                assignementProblem.tabuAlgortihm();
                assignementProblem.printOutput();
                int output = assignementProblem.getF().apply(assignementProblem.getOutCombination());
                sum += output;
                if (output < min) min = output;
                if (output == SearchTestUtil.taillardOptima.get(TAILLARD_FILENAME)) optimumReached++;
                outputs.add(output);
            }
            stopwatch.stop();

            LogFileHandler fileHandler = new LogFileHandler(assignementProblem.getAssignmentData().getLength());
            fileHandler.writeLogs(outputs);

            System.out.println("\n");
            System.out.println("Average tabu " + sum / combinationArrayList.size());
            System.out.println("Minimum found " + min);
            System.out.println("Optimum " + SearchTestUtil.taillardOptima.get(TAILLARD_FILENAME) + " found " + optimumReached + " times");
            System.out.println("Execution time " + stopwatch.elapsedMs() + " microsec");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test 100 iterations for recuit algorithm. Compute average solution.
     */
    @Test
    void recuitAlgorithm() {
        System.out.println("\nRecuit algorithm test on 100 solutions\n");
        try {
            float sum = 0;
            int min = Integer.MAX_VALUE;
            int optimumReached = 0;
            ArrayList<Integer> outputs = new ArrayList<>();

            CombinationGenerator combinationGenerator = new CombinationGenerator(assignementProblem.getAssignmentData().getLength());
            ArrayList<Combination> combinationArrayList = combinationGenerator.readFile();

            Stopwatch stopwatch = new Stopwatch(true);
            for (Combination combination : combinationArrayList) {
                assignementProblem.setInCombination(combination);
                assignementProblem.recuitAlgortihm(INITIAL_TO, MU);
                assignementProblem.printOutput();
                int output = assignementProblem.getF().apply(assignementProblem.getOutCombination());
                sum += output;
                if (output < min) min = output;
                if (output == SearchTestUtil.taillardOptima.get(TAILLARD_FILENAME)) optimumReached++;
                outputs.add(output);
            }
            stopwatch.stop();

            LogFileHandler fileHandler = new LogFileHandler(assignementProblem.getAssignmentData().getLength());
            fileHandler.writeLogs(outputs);

            System.out.println("\n");
            System.out.println("Average recuit " + sum / combinationArrayList.size());
            System.out.println("Minimum found " + min);
            System.out.println("Optimum " + SearchTestUtil.taillardOptima.get(TAILLARD_FILENAME) + " found " + optimumReached + " times");
            System.out.println("Execution time " + stopwatch.elapsedMs() + " microsec");

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
        assignementProblem.recuitAlgortihm(INITIAL_TO, MU);
        assignementProblem.printOutput();
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
     * Compute all results for each taillard instance
     */
    @Test
    public void taillardTest() {
        System.out.println("\nRecuit and tabu algorithms on each Taillard\n");
        try {

            for (int i = 0; i < /*SearchTestUtil.taillardFilenames.length*/ 5; i++) {
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
                assignementProblem.recuitAlgortihm(INITIAL_TO, MU);
                stopwatch.stop();
                System.out.print("Recuit ");
                System.out.print(" Best " + assignementProblem.getF().apply(assignementProblem.getOutCombination()));
                System.out.println(" | Time " + stopwatch.elapsedMs() + " ms");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Test tabu list size impact
     */
    @Test
    public void tabuSizeTest() {
        try {

            for (int i = 0; i < /*SearchTestUtil.taillardFilenames.length*/ 2; i++) {
                System.out.println("-----------------");
                System.out.println("Run#" + i + " " + SearchTestUtil.taillardFilenames[i]);
                System.out.println("-----------------");
                assignementProblem.taillardInitializer(SearchTestUtil.taillardFilenames[i]);

                assignementProblem.setInCombination(Combination.generateRandom(assignementProblem.getAssignmentData().getLength()));

                for (int j = 1; j < Math.pow(assignementProblem.getAssignmentData().getLength(), 2); j = j + 9) {
                    System.out.println("Tabu size " + j);
                    assignementProblem.setTabuSize(j);

                    assignementProblem.tabuAlgortihm();

                    assignementProblem.tabuAlgortihm(assignementProblem.getF().apply(assignementProblem.getOutCombination()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test temperature variation impact
     */
    @Test
    public void muVariationTest() {
        try {

            for (double i = 0; i <= 1; i = i + 0.1) {
                System.out.println("\n#Run mu = " + i);
                float sum = 0;
                int min = Integer.MAX_VALUE;
                int optimumReached = 0;
                ArrayList<Integer> outputs = new ArrayList<>();

                CombinationGenerator combinationGenerator = new CombinationGenerator(assignementProblem.getAssignmentData().getLength());
                ArrayList<Combination> combinationArrayList = combinationGenerator.readFile();

                Stopwatch stopwatch = new Stopwatch(true);
                for (Combination combination : combinationArrayList) {
                    assignementProblem.setInCombination(combination);
                    assignementProblem.recuitAlgortihm(INITIAL_TO, i); // mu variation
                    int output = assignementProblem.getF().apply(assignementProblem.getOutCombination());
                    sum += output;
                    if (output < min) min = output;
                    if (output == SearchTestUtil.taillardOptima.get(TAILLARD_FILENAME)) optimumReached++;
                }
                stopwatch.stop();

                System.out.println("Average recuit " + sum / combinationArrayList.size());
                System.out.println("Minimum found " + min);
                System.out.println("Optimum " + SearchTestUtil.taillardOptima.get(TAILLARD_FILENAME) + " found " + optimumReached + " times");
                System.out.println("Execution time " + stopwatch.elapsedMs() + " microsec");


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}