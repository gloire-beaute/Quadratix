package quadratix.neighborhood;

import quadratix.ElementaryFunction;
import quadratix.combination.Combination;

import java.util.HashMap;
import java.util.function.Function;

public abstract class NeighborhoodState {

    abstract Function<Combination, HashMap<Combination, ElementaryFunction<Combination>>> getNeighborhoods();
}
