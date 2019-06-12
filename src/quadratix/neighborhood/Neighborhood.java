package quadratix.neighborhood;

import org.jetbrains.annotations.Nullable;
import quadratix.ElementaryFunction;
import quadratix.combination.Combination;

import java.util.HashMap;
import java.util.function.Function;

/**
 * Class that represents the controller for {@link NeighborhoodState}. It manages an automaton in order to set the
 * adequate neighborhood type.
 * @see NeighborhoodState
 * @see quadratix.assignement.AssignementProblem
 */
public class Neighborhood {
    
    /**
     * The current state.
     */
    private NeighborhoodState state;
    
    /**
     * Default {@link Neighborhood} constructor.
     * @param state The initial state of the automaton. It will be set as the current state.
     * @see NeighborhoodFull
     * @see NeighborhoodLimited
     * @see NeighborhoodRandom
     */
    public Neighborhood(NeighborhoodState state) {
        this.state = state;
    }
    
    /**
     * Set the current state of the automaton.
     * @param state The new current state.
     */
    private void changeState(NeighborhoodState state) {
        this.state = state;
    }
    
    /**
     * Automatically change the state according to the given neighborhood type.
     * @param stateType The neighborhood type.
     * @param param An optional parameter for the new instance of the state. Default value is {@code null}.
     */
    public void switchState(int stateType, @Nullable Integer param) {
        /*
            0 : toutes les permutations entre 2 élements
            1 : n permutations aléatoires ou n = taille de taillard
            2 : toutes les permutations entre 2 élements à moins de d de distance ou d à fixer
        */
        switch (stateType) {
            case 1:
                this.changeState(new NeighborhoodRandom(param));
                break;
            case 2:
                this.changeState(new NeighborhoodLimited());
                break;
            default:
                this.changeState(new NeighborhoodFull());
                break;
        }
    }
    
    /**
     * Automatically change the state according to the given neighborhood type.
     * @param stateType The neighborhood type.
     */
    private void switchState(int stateType){
        this.switchState(stateType, null);
    }
    
    /**
     * Generate all the neighborhood according to the current state.
     * @return Return a neighborhood generator.
     */
    public Function<Combination, HashMap<Combination, ElementaryFunction<Combination>>> getAllNeighborhoods() {
        return state.getNeighborhoods();
    }
}
