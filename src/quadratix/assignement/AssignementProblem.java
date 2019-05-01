package quadratix.assignement;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import quadratix.ElementaryFunction;
import quadratix.NumberOperations;
import quadratix.combination.Combination;
import quadratix.data.AssignmentData;
import quadratix.data.TaillardReader;
import quadratix.tabu.Tabu;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.Function;

public class AssignementProblem {

    private AssignmentData assignmentData = new AssignmentData();
    private Combination inCombination = new Combination();
    private Combination outCombination = new Combination();

    private Tabu<Combination, Integer> tabu;
    private Function<Combination, Integer> f; //fitness
    private Function<Combination, HashMap<Combination, ElementaryFunction<Combination>>> V_combination;
    private NumberOperations<Integer> intOps;

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

    public void setInCombination(Combination inCombination) {
        this.inCombination = inCombination;
    }

    public Function<Combination, Integer> getF() {
        return f;
    }

    public void tabuAlgortihm() {
        tabu = new Tabu<>();

        outCombination = tabu.search(f, inCombination, V_combination, intOps, 5.0, 3);
        System.out.println("Result: f(" + outCombination + ") = " + f.apply(outCombination));
    }

    private void setFitnessFunction() {
        f = Combination -> {
            int result = 0;
            for (int i = 1; i <= assignmentData.getLength(); i++) {
                for (int j = i + 1; j <= assignmentData.getLength(); j++) {
                    result += assignmentData.getWeights().get(new Pair<>(Combination.get(i-1), Combination.get(j-1)))
                            * assignmentData.getDistances().get(new Pair<>((long) i, (long) j));
                }
            }
            return result;
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
