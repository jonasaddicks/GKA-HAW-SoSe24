package util;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.Optional;

public class GraphValidater {

    public static boolean isEulerian(Graph eulerGraph) {
        if (eulerGraph.getNodeCount() < 1) {return false;}
        DisjointSetsComponents<Node> disjointNodes = new DisjointSetsComponents<>(eulerGraph.getNodeCount());
        Optional<Node> node = eulerGraph.nodes()
                .peek(disjointNodes::add)
                .filter(n -> NodeDegree.getDegree(n) % 2 != 0)
                .findFirst();
        if (node.isPresent()) {
            return false;
        }
        eulerGraph.edges().forEach(e -> disjointNodes.union(e.getNode0(), e.getNode1()));
        return disjointNodes.getComponents() == 1;
    }
}
