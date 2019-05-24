package quadratix.data;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.stream.Collectors;

public class AssignmentData {

    private Integer length;
    private HashMap<Pair<Long, Long>, Long> weights;
    private HashMap<Pair<Long, Long>, Long> distances;

    public AssignmentData() {
        this.weights = new HashMap<>();
        this.distances = new HashMap<>();
    }

    public AssignmentData(@NotNull int length, @NotNull HashMap<Pair<Long, Long>, Long> weights,
                          @NotNull HashMap<Pair<Long, Long>, Long> distances) {
        if(weights.size() != distances.size())
            throw new AssignmentDataException("Weights and distances must have same size");
        this.length = length;

        this.weights = new HashMap<>();
        this.distances = new HashMap<>();
        weights.keySet().forEach(w -> this.addWeight(w, weights.get(w)));
        distances.keySet().forEach(d -> this.addDistance(d, distances.get(d)));
    }

    public void addWeight(Pair<Long, Long> pair, Long weight){
        if(!this.weights.containsKey(pair)){
            this.weights.put(pair,weight);
        }
    }

    public void addDistance(Pair<Long, Long> pair, Long distance){
        if(!this.distances.containsKey(pair)){
            this.distances.put(pair,distance);
        }
    }

    //GETTERS SETTERS


    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public HashMap<Pair<Long, Long>, Long> getWeights() {
        return weights;
    }

    public void setWeights(HashMap<Pair<Long, Long>, Long> weights) {
        this.weights = weights;
    }

    public HashMap<Pair<Long, Long>, Long> getDistances() {
        return distances;
    }

    public void setDistances(HashMap<Pair<Long, Long>, Long> distances) {
        this.distances = distances;
    }

    public String weightsToString(){
        return this.buildTab(this.weights,"WEIGHTS");
    }

    public String distanceToString(){
        return this.buildTab(this.distances,"DISTANCES");
    }

    public String buildTab(HashMap<Pair<Long, Long>, Long> hashMap, String name){

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        stringBuilder.append("[").append(name).append("]\n");
        stringBuilder.append("   |");
        for (int i = 0; i < this.length; i++) {
            stringBuilder.append(" ").append(i+1).append(" |");
        }
        stringBuilder.append("\n");
        for (int i = 1; i <= this.length; i++) {
            stringBuilder.append(" ").append(i).append(" |");
            int finalI = i;
            stringBuilder.append(hashMap.entrySet().stream().filter(el -> el.getKey().getKey() == finalI)
                    .map(e2 -> " " + e2.getValue())
                    .collect(Collectors.joining("  |")));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return this.weightsToString() +
                this.distanceToString();
    }
}