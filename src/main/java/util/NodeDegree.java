package util;

import org.graphstream.graph.Node;

public class NodeDegree {

    public static int getDegree(Node node) {
        return node.getDegree() + node.edges()
                .filter(e -> e.getNode0().equals(e.getNode1()))
                .map(e -> 1)
                .reduce(0, Integer::sum);
    }
}
