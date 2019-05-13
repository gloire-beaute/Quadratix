package quadratix.assignement;

import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quadratix.combination.Combination;

import java.io.IOException;
import java.util.HashMap;

class AssignementProblemTest {

    private AssignementProblem assignementProblem = new AssignementProblem();

    @BeforeEach
    void setUp(){

        //////////////////////
        /////// Taillard /////
        //////////////////////

//        try {
//            assignementProblem.taillardInitializer("tai12.txt");
//            System.out.println(assignementProblem.getAssignmentData().toString());
//
//            Combination initialComb = new Combination();
//            for (int i = 0; i < assignementProblem.getAssignmentData().getLength(); i++) {
//                initialComb.add((long) i+1);
//            }
//            assignementProblem.setInCombination(initialComb);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //////////////////////
        /////// Exemple /////
        //////////////////////

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
        int fitness = assignementProblem.getF().apply(new Combination(1,3,4,5,2));
        System.out.println(assignementProblem.getAssignmentData().toString());
//        assertEquals(78, fitness);

        //////////////////////

        Combination initialComb = new Combination();
        for (int i = 0; i < assignementProblem.getAssignmentData().getLength(); i++) {
            initialComb.add((long) i+1);
        }
        assignementProblem.setInCombination(/*new Combination(1,3,4,5,2)*/ initialComb);
    }

    @Test
    void tabuAlgorithm(){
        assignementProblem.tabuAlgortihm();
    }

    @Test
    void recuitAlgorithm(){
        assignementProblem.recuitAlgortihm();
    }

    @Test
    void algorithms(){
        System.out.println("TABU algorithm");
        long debut = System.nanoTime();
        assignementProblem.tabuAlgortihm();
        System.out.println("Execution time : " + (System.nanoTime() - debut)/1000 + " microsec");
        System.out.println("\n");

        System.out.println("RECUIT algorithm");
        debut = System.nanoTime();
        assignementProblem.recuitAlgortihm();
        System.out.println("Execution time : " + (System.nanoTime() - debut)/1000 + " microsec");
        System.out.println("\n");
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
//        System.out.println( "8,1,6,2,11,10,3,5,9,7,12,4 : " +
//                assignementProblem.getF().apply(new Combination(8,1,6,2,11,10,3,5,9,7,12,4)));


    }

}