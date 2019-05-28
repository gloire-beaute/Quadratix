package quadratix.neighborhood;

import quadratix.ElementaryFunction;
import quadratix.combination.Combination;

import java.util.HashMap;
import java.util.function.Function;

public class NeighborhoodRandom extends NeighborhoodState {

    private Integer neighSize;

    public  NeighborhoodRandom(Integer neighSize){
        this.neighSize = neighSize;
    }

    @Override
    public Function<Combination, HashMap<Combination, ElementaryFunction<Combination>>> getNeighborhoods() {
        return Combination.generateRandomNeighborhood(neighSize);
    }
}