package aufgabe2.algs;

import org.graphstream.graph.Graph;

public class weightSum {

    public static synchronized int graphWeightSum(Graph graph) {
        return graph.edges().map(n -> (int) n.getAttribute("weight")).reduce(0, Integer::sum);
    }
}
