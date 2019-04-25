package data;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.stream.Collectors;

public class AssignementData {

    private Integer length;
    private HashMap<Pair<Integer, Integer>, Integer> weights;
    private HashMap<Pair<Integer, Integer>, Integer> distances;

    AssignementData() {
        this.weights = new HashMap<>();
        this.distances = new HashMap<>();
    }

    public void addWeight(Pair<Integer, Integer> pair, Integer weight){
        Pair<Integer, Integer> revertedPair = new Pair<>(pair.getValue(),pair.getKey());
        if(!this.weights.containsKey(pair) && !this.weights.containsKey(revertedPair)){
            this.weights.put(pair,weight);
            this.weights.put(revertedPair,weight);
        }
    }

    public void addDistance(Pair<Integer, Integer> pair, Integer distance){
        Pair<Integer, Integer> revertedPair = new Pair<>(pair.getValue(),pair.getKey());
        if(!this.distances.containsKey(pair) && !this.distances.containsKey(revertedPair)){
            this.distances.put(pair,distance);
            this.distances.put(revertedPair,distance);
        }
    }

    //GETTERS SETTERS

    public HashMap<Pair<Integer, Integer>, Integer> getWeights() {
        return weights;
    }

    public void setWeights(HashMap<Pair<Integer, Integer>, Integer> weights) {
        this.weights = weights;
    }

    public HashMap<Pair<Integer, Integer>, Integer> getDistances() {
        return distances;
    }

    public void setDistances(HashMap<Pair<Integer, Integer>, Integer> distances) {
        this.distances = distances;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String weightsToString(){
        return this.buildTab(this.weights,"WEIGHTS");
    }

    public String distanceToString(){
        return this.buildTab(this.distances,"DISTANCES");
    }

    public String buildTab(HashMap<Pair<Integer,Integer>, Integer> hashMap, String name){

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

