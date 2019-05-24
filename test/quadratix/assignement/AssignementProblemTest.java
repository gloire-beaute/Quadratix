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
            System.out.println(assignementProblem.getAssignmentData().toString());

            Combination initialComb = new Combination();
            for (int i = 0; i < assignementProblem.getAssignmentData().getLength(); i++) {
                initialComb.add((long) i+1);
            }
            assignementProblem.setInCombination(initialComb);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Combination initialComb = new Combination();
        for (int i = 0; i < assignementProblem.getAssignmentData().getLength(); i++) {
            initialComb.add((long) i+1);
        }
        assignementProblem.setInCombination(initialComb);
    }

    @Test
    void setupTD(){

        HashMap<Pair<Long, Long>, Long> weights = new HashMap<>();
        weights.put(new Pair<>(1L,1L),0L);
        weights.put(new Pair<>(1L,2L),12L);
        weights.put(new Pair<>(1L,3L),4L);
        weights.put(new Pair<>(1L,4L),3L);
        weights.put(new Pair<>(1L,5L),7L);
        weights.put(new Pair<>(2L,2L),0L);
        weights.put(new Pair<>(2L,3L),0L);
        weights.put(new Pair<>(2L,4L),6L);
        weights.put(new Pair<>(2L,5L),1L);
        weights.put(new Pair<>(3L,3L),0L);
        weights.put(new Pair<>(3L,4L),0L);
        weights.put(new Pair<>(3L,5L),2L);
        weights.put(new Pair<>(4L,4L),0L);
        weights.put(new Pair<>(4L,5L),0L);
        weights.put(new Pair<>(5L,5L),0L);

        HashMap<Pair<Long, Long>, Long> distances = new HashMap<>();
        distances.put(new Pair<>(1L,1L),0L);
        distances.put(new Pair<>(1L,2L),2L);
        distances.put(new Pair<>(1L,3L),1L);
        distances.put(new Pair<>(1L,4L),2L);
        distances.put(new Pair<>(1L,5L),3L);
        distances.put(new Pair<>(2L,2L),0L);
        distances.put(new Pair<>(2L,3L),3L);
        distances.put(new Pair<>(2L,4L),2L);
        distances.put(new Pair<>(2L,5L),1L);
        distances.put(new Pair<>(3L,3L),0L);
        distances.put(new Pair<>(3L,4L),1L);
        distances.put(new Pair<>(3L,5L),2L);
        distances.put(new Pair<>(4L,4L),0L);
        distances.put(new Pair<>(4L,5L),1L);
        distances.put(new Pair<>(5L,5L),0L);

        assignementProblem.customInitializer(5,weights,distances);
        System.out.println(assignementProblem.getAssignmentData().toString());
        assignementProblem.setInCombination(new Combination(1,3,4,5,2) );
        System.out.println(assignementProblem.getF().apply(new Combination(3,1,4,5,2)));
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
        assignementProblem.recuitAlgortihm();
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
    void fitness(){
        //only for custom exemple
//        System.out.println( "3,1,4,5,2 " + assignementProblem.getF().apply(new Combination(3,1,4,5,2)));
//        System.out.println( "4,3,1,5,2 " + assignementProblem.getF().apply(new Combination(4,3,1,5,2)));
//        System.out.println( "5,3,4,1,2 " + assignementProblem.getF().apply(new Combination(5,3,4,1,2)));
//        System.out.println( "2,3,4,5,1 " + assignementProblem.getF().apply(new Combination(2,3,4,5,1)));
//        System.out.println( "1,4,3,5,2 " + assignementProblem.getF().apply(new Combination(1,4,3,5,2)));
//        System.out.println( "1,5,4,3,2 " + assignementProblem.getF().apply(new Combination(1,5,4,3,2)));
//        System.out.println( "1,2,4,5,3 " + assignementProblem.getF().apply(new Combination(1,2,4,5,3)));
//        System.out.println( "1,3,5,4,2 " + assignementProblem.getF().apply(new Combination(1,3,5,4,2)));
//        System.out.println( "1,3,5,5,4 " + assignementProblem.getF().apply(new Combination(1,3,5,5,4)));
//        System.out.println( "1,3,4,2,5 " + assignementProblem.getF().apply(new Combination(1,3,4,2,5)));
//        System.out.println( "1,2,3,4,5 " + assignementProblem.getF().apply(new Combination(1,2,3,4,5)));

        //TAI 30
//        System.out.println( "4 8 11 15 17 20 21 5 14 30 2 13 6 29 10 26 27 24 28 22 12 9 7 23 19 18 25 16 1 3 : " +
//                assignementProblem.getF().apply(new Combination(4,8, 11, 15, 17, 20, 21, 5, 14, 30, 2, 13, 6, 29, 10, 26, 27, 24, 28, 22, 12, 9, 7, 23, 19, 18, 25, 16, 1, 3)));
//     TAI 12
//        System.out.println( "(8,1,6,2,11,10,3,5,9,7,12,4)  : " +
//                assignementProblem.getF().apply(new Combination(8,1,6,2,11,10,3,5,9,7,12,4)));


    }

}