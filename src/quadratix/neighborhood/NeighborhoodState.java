package quadratix.neighborhood;

import quadratix.ElementaryFunction;
import quadratix.combination.Combination;

import java.util.HashMap;
import java.util.function.Function;

/**
 * Abstract class representing a state for the Neighborhood automaton.
 */
public abstract class NeighborhoodState {

    abstract Function<Combination, HashMap<Combination, ElementaryFunction<Combination>>> getNeighborhoods();
}
