package quadratix.graphic;

import org.jetbrains.annotations.NotNull;
import quadratix.combination.Combination;
import quadratix.data.AssignementData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GraphvizGenerator {

    private static String DOT_PATH = "res/graph/graph.dot";
    private static String IMG_PATH = "res/graph/graph.png";

    /* Template
        graph G {
            graph [bgcolor=coral1, splines=true, label="Quadratic assignement solution - Tabu", fontname="Verdana", fontcolor="white"];
            edge [color=white, fontcolor=white,penwidth=4.0];
            node [color=white, style=filled, shape=record, sides=6, fontname="Verdana", fontcolor=coral1];
            A1 [label="[a,1]"];
            A2 [label="[b,2]"];
            A3 [label="[c,3]"];
            A1 -- A3 [ label="12" ];
            A3 -- A2 [ label="45" ];
            A1 -- A2
          }
     */
    @NotNull
    public static String buildGraph(@NotNull Combination combination, @NotNull AssignementData assignementData) {
        StringBuilder stringBuilder = new StringBuilder();

        // style initialisation
        stringBuilder.append("graph Assignement {")
                .append("\tgraph [bgcolor=coral1, splines=true, label=\"Quadratic assignement solution - Tabu\", fontname=\"Verdana\", fontcolor=\"white\"];\n")
                .append("\tedge [color=white, fontcolor=white,penwidth=4.0];")
                .append("\nnode [color=white, style=filled, shape=record, sides=6, fontname=\"Verdana\", fontcolor=coral1];");

        for (int i = 1; i <= assignementData.getLength(); i++) {
            // nodes
            stringBuilder.append("\n\t")
                    .append(i)
                    .append(" [label=\"[p")
                    .append(i)
                    .append(",m")
                    .append(combination.get(i-1)).append("]\"];");
        }

        // edges
        assignementData.getWeights().keySet().forEach(w -> {
                    String edge = w.getKey() + "--" + w.getValue();
                    String invertedEdge = w.getValue() + "--" + w.getKey();
                    if(!stringBuilder.toString().contains(edge) && !stringBuilder.toString().contains(invertedEdge)){
                        stringBuilder.append("\n\t")
                                .append(edge)
                                .append(" [ label=\"")
                                .append(assignementData.getWeights().get(w)).append("\" ];");
                    }
                }
        );

        stringBuilder.append("\n}");
        return stringBuilder.toString();
    }

    public static void generateGraphFile(String graph) throws IOException {
        FileWriter fw = new FileWriter(DOT_PATH);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(graph);

        bw.close();
        fw.close();
    }

    public static void generateGraphImage() throws IOException {
        Runtime rtime = Runtime.getRuntime();
        String command = "dot -Tpng " + DOT_PATH + " -o " + IMG_PATH;
        System.out.println(command);
        Process child = rtime.exec(command);
        child.destroy();
    }
}
