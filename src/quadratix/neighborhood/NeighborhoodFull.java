package quadratix.neighborhood;

import quadratix.ElementaryFunction;
import quadratix.combination.Combination;

import java.util.HashMap;
import java.util.function.Function;

public class NeighborhoodFull extends NeighborhoodState {

    @Override
    public Function<Combination, HashMap<Combination, ElementaryFunction<Combination>>> getNeighborhoods() {
        return Combination.generateAllNeighbors();
    }
}