package quadratix.neighborhood;

import quadratix.ElementaryFunction;
import quadratix.combination.Combination;

import java.util.HashMap;
import java.util.function.Function;

/**
 * The first neighborhood. It will generate all possible neighbors from a combination.
 */
public class NeighborhoodFull extends NeighborhoodState {
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Combination, HashMap<Combination, ElementaryFunction<Combination>>> getNeighborhoods() {
        return Combination.generateAllNeighbors();
    }
}
