package quadratix.assignement;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import quadratix.ElementaryFunction;
import quadratix.NumberOperations;
import quadratix.combination.Combination;
import quadratix.data.AssignmentData;
import quadratix.data.TaillardReader;
import quadratix.neighborhood.Neighborhood;
import quadratix.neighborhood.NeighborhoodFull;
import quadratix.simulatedannealing.SimulatedAnnealing;
import quadratix.tabu.Tabu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public class AssignementProblem {

    private AssignmentData assignmentData = new AssignmentData();
    private Combination inCombination = new Combination();
    private Combination outCombination = new Combination();
    private Neighborhood neighborhood = new Neighborhood(new NeighborhoodFull());

    private Function<Combination, Integer> f; //fitness
    private Function<Combination, HashMap<Combination, ElementaryFunction<Combination>>> V_combination;
    private NumberOperations<Integer> intOps = NumberOperations.getIntegerOperations();

    public AssignementProblem() {}

    public void setup(){
        this.setFitnessFunction();
        this.setNeighborsFunction(0, 15);
    }

    // Initializer
    // region
    public void taillardInitializer(@NotNull String filename) throws IOException {
        TaillardReader taillardReader = new TaillardReader(filename);
        this.assignmentData = taillardReader.createAssignementData();
        this.setup();
    }

    public void customInitializer(@NotNull Integer length, @NotNull HashMap<Pair<Long, Long>, Long> weights,
                                  @NotNull HashMap<Pair<Long, Long>, Long> distance) {
        this.assignmentData = new AssignmentData(length, weights, distance);
        this.setup();
    }

    //endregion

    // Getters - Setters
    // region
    public AssignmentData getAssignmentData() {
        return assignmentData;
    }

    Combination getOutCombination() {
        return outCombination;
    }

    void setInCombination(Combination inCombination) {
        this.inCombination = inCombination;
    }

    public Function<Combination, Integer> getF() {
        return f;
    }
    
    public Function<Combination, HashMap<Combination, ElementaryFunction<Combination>>> getV() {
        return V_combination;
    }

    //endregion

    // Algorithms
    // region
    public void tabuAlgortihm(@Nullable Integer optima, Integer tabuSize) {
        Tabu<Combination, Integer> tabu = new Tabu<>();
        outCombination = tabu.search(f, inCombination, V_combination, intOps, tabuSize,1000*this.assignmentData.getLength(), optima);
    }

    public void tabuAlgortihm() {
       this.tabuAlgortihm(null, this.assignmentData.getLength());
    }

    public void recuitAlgortihm(@Nullable Double t0) {
        SimulatedAnnealing<Combination, Integer> simulatedAnnealing = new SimulatedAnnealing<>();
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
                        10)
                : t0,
                100,
                100,
                0.1);
    }

    public void recuitAlgortihm() {
        recuitAlgortihm(null);
    }

    public void printOutput(){
        System.out.println("Result: f(" + outCombination + ") = " + f.apply(outCombination));
    }

    //endregion

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

    void setNeighborsFunction(int type, int param){
        neighborhood.switchState(type, param);
        V_combination = neighborhood.getAllNeighborhoods();
    }

}
