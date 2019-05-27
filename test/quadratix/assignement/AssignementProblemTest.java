package quadratix.assignement;

import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quadratix.ElementaryFunction;
import quadratix.combination.Combination;
import quadratix.data.CombinationGenerator;
import quadratix.data.AssignmentData;
import quadratix.stats.Stopwatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class AssignementProblemTest {

    private AssignementProblem assignementProblem = new AssignementProblem();
    
    private static final int MAX_ITERATION_COMPUTE_T0 = 10000;

    @BeforeEach
    void setUp(){
        try {
            assignementProblem.taillardInitializer("tai12.txt");
//            System.out.println(assignementProblem.getAssignmentData().toString());

            Combination initialComb = new Combination();
            for (int i = 0; i < assignementProblem.getAssignmentData().getLength(); i++) {
                initialComb.add((long) i+1);
            }
            assignementProblem.setInCombination(initialComb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void tabuAlgorithm(){
        try {
            float sum = 0;
            CombinationGenerator combinationGenerator = new CombinationGenerator(assignementProblem.getAssignmentData().getLength());
            ArrayList<Combination> combinationArrayList = combinationGenerator.readFile();

            for (int i = 0; i < combinationArrayList.size(); i++) {
                assignementProblem.setInCombination(combinationArrayList.get(i));
                assignementProblem.tabuAlgortihm();
                sum += assignementProblem.getF().apply(assignementProblem.getOutCombination());
            }

            System.out.println("\n");
            System.out.println("Average tabu " + sum/combinationArrayList.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void recuitAlgorithm(){
        try {
            float sum = 0;
            CombinationGenerator combinationGenerator = new CombinationGenerator(assignementProblem.getAssignmentData().getLength());
            ArrayList<Combination> combinationArrayList = combinationGenerator.readFile();

            for (int i = 0; i < combinationArrayList.size(); i++) {
                assignementProblem.setInCombination(combinationArrayList.get(i));
                assignementProblem.recuitAlgortihm(null);
                sum += assignementProblem.getF().apply(assignementProblem.getOutCombination());
            }

            System.out.println("\n");
            System.out.println("Average recuit " + sum/combinationArrayList.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void algorithms(){
        System.out.println("TABU algorithm");
        Stopwatch stopwatch = new Stopwatch(true);
        assignementProblem.tabuAlgortihm();
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
        Function<Combination, HashMap<Combination, ElementaryFunction<Combination>>> neighborsGenerator =  Combination.generateAllNeighbors();
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
        double t0 = - avg/Math.log(0.8);
        System.out.println(String.format("t0 = %.4f°", t0));
    }

    @Test
    public void tabuSizeTest(){
        try {
            assignementProblem.taillardInitializer("tai12.txt");
            assignementProblem.setInCombination(Combination.generateRandom(assignementProblem.getAssignmentData().getLength()));
            assignementProblem.tabuAlgortihm();

            assignementProblem.taillardInitializer("tai15.txt");
            assignementProblem.setInCombination(Combination.generateRandom(assignementProblem.getAssignmentData().getLength()));
            assignementProblem.tabuAlgortihm();

            assignementProblem.taillardInitializer("tai17.txt");
            assignementProblem.setInCombination(Combination.generateRandom(assignementProblem.getAssignmentData().getLength()));
            assignementProblem.tabuAlgortihm();

            assignementProblem.taillardInitializer("tai20.txt");
            assignementProblem.setInCombination(Combination.generateRandom(assignementProblem.getAssignmentData().getLength()));
            assignementProblem.tabuAlgortihm();

            assignementProblem.taillardInitializer("tai25.txt");
            assignementProblem.setInCombination(Combination.generateRandom(assignementProblem.getAssignmentData().getLength()));
            assignementProblem.tabuAlgortihm();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}