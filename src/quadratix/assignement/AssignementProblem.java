package quadratix.assignement;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import quadratix.ElementaryFunction;
import quadratix.NumberOperations;
import quadratix.bits.Bits;
import quadratix.combination.Combination;
import quadratix.data.AssignmentData;
import quadratix.data.TaillardReader;
import quadratix.simulatedannealing.SimulatedAnnealing;
import quadratix.tabu.Tabu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssignementProblem {

    private AssignmentData assignmentData = new AssignmentData();
    private Combination inCombination = new Combination();
    private Combination outCombination = new Combination();

    private Tabu<Combination, Integer> tabu;
    private SimulatedAnnealing<Combination, Integer> simulatedAnnealing;
    private Function<Combination, Integer> f; //fitness
    private Function<Combination, HashMap<Combination, ElementaryFunction<Combination>>> V_combination;
    private NumberOperations<Integer> intOps;

    private ArrayList<Combination> initValuesToTest = new ArrayList<>();

    public AssignementProblem() {

    }

    public void setup(){
        this.setFitnessFunction();
        this.setNeighborsFunction();
        this.setOperations();
    }

    public void taillardInitializer(@NotNull String filename) throws IOException {
        TaillardReader taillardReader = new TaillardReader(filename);
        this.assignmentData = taillardReader.createAssignementData();
        this.setup();
    }

    public void customInitializer(@NotNull int length, @NotNull HashMap<Pair<Long, Long>, Long> weights,
                                  @NotNull HashMap<Pair<Long, Long>, Long> distance) {
        this.assignmentData = new AssignmentData(length, weights, distance);
        this.setup();
    }

    public AssignmentData getAssignmentData() {
        return assignmentData;
    }

    public Combination getOutCombination() {
        return outCombination;
    }

    public void setInCombination(Combination inCombination) {
        this.inCombination = inCombination;
    }

    public Function<Combination, Integer> getF() {
        return f;
    }

    public void tabuAlgortihm() {
        tabu = new Tabu<>();

        System.out.println("\nTABU");
        outCombination = tabu.search(f, inCombination, V_combination, intOps, 3);
        System.out.println("Result: f(" + outCombination + ") = " + f.apply(outCombination));
        System.out.println("Fitness call: " + tabu.getFitnessCall());
    }

    public void recuitAlgortihm(@Nullable Double t0) {
        simulatedAnnealing = new SimulatedAnnealing<>();
        System.out.println("\nRECUIT");
        outCombination = simulatedAnnealing.search(
                f,
                inCombination,
                V_combination,
                intOps,
                t0 == null ?
                SimulatedAnnealing.computeTemperature(
                        f,
                        V_combination,
                        intOps,
                        v -> Combination.generateRandom(getAssignmentData().getLength()),
                        i -> (double) i,
                        1000)
                : t0,
                100,
                100,
                0.1);
        System.out.println("Result: f(" + outCombination + ") = " + f.apply(outCombination));
//        System.out.println("Fitness call: " + simulatedAnnealing.getFitnessCall());
    }
    public void recuitAlgortihm() {
        recuitAlgortihm(null);
    }

    private void setFitnessFunction() {
        f = (final Combination c) -> {
            int result = 0;
            for (int i = 1; i <= assignmentData.getLength(); i++) {
                for (int j = i + 1; j <= assignmentData.getLength(); j++) {
                    result += assignmentData.getWeights().get(new Pair<>(c.get(i-1), c.get(j-1)))
                            * assignmentData.getDistances().get(new Pair<>((long) i, (long) j));
                }
            }
            return 2*result;
        };
    }

    private void setNeighborsFunction() {
        V_combination = combination -> {
            HashMap<Combination, ElementaryFunction<Combination>> map = new HashMap<>();

            for (int i = 0; i < assignmentData.getLength(); i++) {
                for (int j = i+1; j < assignmentData.getLength(); j++) {
                    Combination c = new Combination(combination);
                    c.swap(i, j);
                    final int final_i = i;
                    final int final_j = j;
                    map.put(c, new ElementaryFunction<Combination>() {
                        @Override
                        public Combination apply(Combination combination1) {
                            combination1.swap(final_i, final_j);
                            return combination1;
                        }

                        @Override
                        public @NotNull Function<Combination, Combination> invert() {
                            return combination1 -> {
                                combination1.swap(final_i, final_j);
                                return combination1;
                            };
                        }
                    });
                }
            }
            return map;
        };
    }

    private void setOperations() {
        //TODO change operations ?
        intOps = new NumberOperations<Integer>() {
            @Nullable
            @Override
            public Integer plus(@Nullable Integer t1, @Nullable Integer t2) {
                return t1 + t2;
            }

            @Nullable
            @Override
            public Integer minus(@Nullable Integer t1, @Nullable Integer t2) {
                return t1 - t2;
            }

            @Nullable
            @Override
            public Integer multiply(@Nullable Integer t1, @Nullable Integer t2) {
                return t1 * t2;
            }

            @Nullable
            @Override
            public Integer divide(@Nullable Integer t1, @Nullable Integer t2) {
                return t1 / t2;
            }

            @Nullable
            @Override
            public Integer getZero() {
                return 0;
            }

            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        };
    }
}
