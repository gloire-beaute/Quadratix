package quadratix.neighborhood;

import quadratix.ElementaryFunction;
import quadratix.combination.Combination;

import java.util.HashMap;
import java.util.function.Function;

/**
 * The third neighborhood type. It will generate all possible neighbors (like {@link NeighborhoodFull} but within
 * a swap distance of 2.
 */
public class NeighborhoodLimited extends NeighborhoodState {
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Combination, HashMap<Combination, ElementaryFunction<Combination>>> getNeighborhoods() {
        return Combination.generateAllNeighbors(2);
    }
}
