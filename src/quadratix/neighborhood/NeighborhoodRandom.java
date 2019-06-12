package quadratix.neighborhood;

import quadratix.ElementaryFunction;
import quadratix.combination.Combination;

import java.util.HashMap;
import java.util.function.Function;

/**
 * The second neighborhood type. It will generate all random neighbors for a given combination.
 */
public class NeighborhoodRandom extends NeighborhoodState {

    private Integer neighSize;
    
    /**
     * Default constructor for {@link NeighborhoodRandom}.
     * @param neighSize The size of the neighborhood to generate.
     */
    public  NeighborhoodRandom(Integer neighSize){
        this.neighSize = neighSize;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Combination, HashMap<Combination, ElementaryFunction<Combination>>> getNeighborhoods() {
        return Combination.generateRandomNeighborhood(neighSize);
    }
}