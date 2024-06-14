package util;

import org.graphstream.graph.Graph;

public class weightSum {

    public static synchronized int graphWeightSum(Graph graph) {
        return graph.edges().map(e -> (int) e.getAttribute("weight")).reduce(0, Integer::sum);
    }
}
