package quadratix.neighborhood;

import org.jetbrains.annotations.Nullable;
import quadratix.ElementaryFunction;
import quadratix.combination.Combination;

import java.util.HashMap;
import java.util.function.Function;

public class Neighborhood {

    private NeighborhoodState state;

    public Neighborhood(NeighborhoodState state) {
        this.state = state;
    }

    private void changeState(NeighborhoodState state) {
        this.state = state;
    }

    public void switchState(int stateType, @Nullable Integer param) {
        /*
            0 : toutes les permutations entre 2 élements
            1 : n permutations aléatoires ou n = taille de taillard
            2 : toutes les permutations entre 2 élements à moins de d de distance ou d à fixer
        */
        switch (stateType) {
            case 0:
                this.changeState(new NeighborhoodFull());
                break;
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

    private void switchState(int stateType){
        this.switchState(stateType, null);
    }

    public Function<Combination, HashMap<Combination, ElementaryFunction<Combination>>> getAllNeighborhoods() {
        return state.getNeighborhoods();
    }
}
