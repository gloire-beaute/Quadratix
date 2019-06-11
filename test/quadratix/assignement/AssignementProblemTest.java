package quadratix.assignement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quadratix.ElementaryFunction;
import quadratix.SearchTestUtil;
import quadratix.combination.Combination;
import quadratix.data.CombinationGenerator;
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

    /**
     * 0 : toutes les permutations entre 2 élements
     * 1 : n permutations aléatoires ou n = taille de taillard
     * 2 : toutes les permutations entre 2 élements à moins de d de distance ou d à fixer
     */
    private static final int NEIGHBORHOOD_TYPE = 1; // 0,1,2

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
            assignementProblem.taillardInitializer(TAILLARD_FILENAME);
            assignementProblem.setNeighborsFunction(NEIGHBORHOOD_TYPE, assignementProblem.getAssignmentData().getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void tabuAlgorithmOnRangeOfValues() {
        executeAlgoRangeOfValues(
                SearchTestUtil.ALGO.TABU,
                this.assignementProblem,
                TABU_SIZE,
                SearchTestUtil.taillardOptima.get(TAILLARD_FILENAME));
    }

    @Test
    void recuitAlgorithmOnRangeOfValues() {
        executeAlgoRangeOfValues(
                SearchTestUtil.ALGO.RECUIT,
                this.assignementProblem,
                null,
                SearchTestUtil.taillardOptima.get(TAILLARD_FILENAME));
    }

    /**
     * Test 2 algorithms for current file
     */
    @Test
    void algorithms() {

        Combination initialComb = new Combination();
        for (int i = 0; i < assignementProblem.getAssignmentData().getLength(); i++) {
            initialComb.add((long) i + 1);
        }
        assignementProblem.setInCombination(initialComb);
        executeAlgo(SearchTestUtil.ALGO.TABU);
        executeAlgo(SearchTestUtil.ALGO.RECUIT);
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
    void tabuSizeTest() {
        for (String taillardFilename : SearchTestUtil.taillardFilenames) {
            System.out.println("-----------------");
            System.out.println("Run " + taillardFilename);
            System.out.println("-----------------");
            tabuSizeTestWith(taillardFilename);
        }
    }

    @Test
    void tabuSizeTestCurrentTaillard() {
        ArrayList<Integer> optima = new ArrayList<>(tabuSizeTestWith(TAILLARD_FILENAME).keySet());
    
        double average = optima.stream().mapToInt(Integer::intValue).average().orElse(0d);
        System.out.println("Average: " + average);
        System.out.println("Standard Deviation: " + Math.sqrt(optima.stream().mapToDouble(x -> Math.pow(Math.abs(x - average), 2)).sum() / (double) optima.size()));
    }
    
    /**
     * Execute the tabu algorithm with different tabu list size.
     * @param taillardFilename The taillard filename.
     * @return Return a map where the keys are the tabu sizes and the values are the optima found.
     */
    private HashMap<Integer, Integer> tabuSizeTestWith(String taillardFilename) {
        Combination inComb = Combination.generateRandom(assignementProblem.getAssignmentData().getLength());
        assignementProblem.setInCombination(inComb);
        
        int[] sizes = {1, 2, 3, 10, 100, 1000, 10000};
        HashMap<Integer, Integer> optima = new HashMap<>(sizes.length);
        for (int size : sizes) {
            System.out.println("Tabu size " + size);
            assignementProblem.tabuAlgortihm(SearchTestUtil.taillardOptima.get(TAILLARD_FILENAME), size); //parameter optima, to know the convergence
            optima.put(size, assignementProblem.getF().apply(assignementProblem.getOutCombination()));
            assignementProblem.printOutput();
            System.out.println();
        }
        
        return optima;
    }

    /**
     * Compute all results for each taillard instance
     */
    @Test
    void taillardTest() {
        try {

            for (int i = 0; i < SearchTestUtil.taillardFilenames.length; i++) {
                System.out.println("Run#" + SearchTestUtil.taillardFilenames[i]);
                assignementProblem.taillardInitializer(SearchTestUtil.taillardFilenames[i]);
                assignementProblem.setNeighborsFunction(NEIGHBORHOOD_TYPE, assignementProblem.getAssignmentData().getLength());
                assignementProblem.setInCombination(Combination.generateRandom(assignementProblem.getAssignmentData().getLength()));
                executeAlgo(SearchTestUtil.ALGO.TABU);
                executeAlgo(SearchTestUtil.ALGO.RECUIT);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compute all results for each taillard instance
     */
    @Test
    void taillardTestMoreIterations() {
        try {

            for (int i = 0; i < SearchTestUtil.taillardFilenames.length; i++) {
                int output;
                float sumTabu = 0;
                int minTabu = Integer.MAX_VALUE;
                float sumRecuit = 0;
                int minRecuit = Integer.MAX_VALUE;
                System.out.println("Run#" + SearchTestUtil.taillardFilenames[i]);
                assignementProblem.taillardInitializer(SearchTestUtil.taillardFilenames[i]);
                assignementProblem.setNeighborsFunction(NEIGHBORHOOD_TYPE, assignementProblem.getAssignmentData().getLength());
                for (int j = 0; j < 10; j++) {
                    assignementProblem.setInCombination(Combination.generateRandom(assignementProblem.getAssignmentData().getLength()));

                    System.out.println("\n \t Test#"+j);
                    executeAlgo(SearchTestUtil.ALGO.TABU);
                    output = assignementProblem.getF().apply(assignementProblem.getOutCombination());
                    sumTabu += output;
                    if (output < minTabu) minTabu = output;

                    executeAlgo(SearchTestUtil.ALGO.RECUIT);
                    output = assignementProblem.getF().apply(assignementProblem.getOutCombination());
                    sumRecuit += output;
                    if (output < minRecuit) minRecuit = output;
                }


                System.out.println("\tAverage tabu " + sumTabu / 10);
                System.out.println("\tMinimum found " + minTabu);

                System.out.println("\tAverage recuit " + sumRecuit / 10);
                System.out.println("\tMinimum found " + minRecuit);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Execute choosen algorithm on current taillard instance
     *
     * @param algo enum algo recuit or tabu
     */
    private void executeAlgo(SearchTestUtil.ALGO algo) {
        Stopwatch stopwatch = new Stopwatch(true);
        if (algo.equals(SearchTestUtil.ALGO.RECUIT)) assignementProblem.recuitAlgortihm();
        else assignementProblem.tabuAlgortihm();
        stopwatch.stop();
        System.out.print("\t" + algo.toString());
        System.out.print(" Best " + assignementProblem.getF().apply(assignementProblem.getOutCombination()));
        System.out.println(" | Time " + stopwatch.elapsedMs() + " ms");
    }

    /**
     * Test 100 iterations for choosen algorithm. Compute average solution, best solution and time.
     * Save result in log file
     * @param algo enum algo recuit or tabu.
     * @param assignementProblem The problem to resolve.
     * @param tabuSize The size of the {@link quadratix.tabu.TabuList}.
     * @param optima The optimum of the function for statistic purposes.
     * @see quadratix.tabu.TabuList
     */
    private void executeAlgoRangeOfValues(SearchTestUtil.ALGO algo, AssignementProblem assignementProblem, Integer tabuSize, Integer optima) {
        try {
            float sum = 0;
            int min = Integer.MAX_VALUE;
            int optimumReached = 0;
            ArrayList<Integer> outputs = new ArrayList<>();

            //Range of values
            CombinationGenerator combinationGenerator = new CombinationGenerator(assignementProblem.getAssignmentData().getLength());
            ArrayList<Combination> combinationArrayList = combinationGenerator.readFile();

            Stopwatch stopwatch = new Stopwatch(true);
            for (Combination combination : combinationArrayList) {
                assignementProblem.setInCombination(combination);
                if (algo.equals(SearchTestUtil.ALGO.RECUIT)) assignementProblem.recuitAlgortihm();
                else assignementProblem.tabuAlgortihm(optima, tabuSize); //parameter optima, to know the convergence
                assignementProblem.printOutput();
                int output = assignementProblem.getF().apply(assignementProblem.getOutCombination());
                sum += output;
                if (output < min) min = output;
                if (output == optima) optimumReached++;
                outputs.add(output);
            }
            stopwatch.stop();

            LogFileHandler fileHandler = new LogFileHandler(assignementProblem.getAssignmentData().getLength());
            fileHandler.writeLogs(outputs);

            System.out.println("\n");
            System.out.println("Average " + algo.toString() + " " + sum / combinationArrayList.size());
            System.out.println("Minimum found " + min);
            System.out.println("Optimum " + optima + " found at " + (optimumReached*100)/ combinationArrayList.size() + "%");
            System.out.println("Execution time " + stopwatch.elapsedMs() + " microsec");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}