package algs.aufgabe3;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import util.DisjointSetsComponents;
import util.NodeDegree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;


public class EulerCircuitTestHelper {

    public static boolean isEulerCircuit(ArrayList<Edge> eulerCircuit, Graph eulerGraph) {
        if (!isEulerian(eulerGraph) || Objects.isNull(eulerCircuit)) {return false;}

        Integer[] visited = new Integer[eulerGraph.getEdgeCount()];
        Arrays.fill(visited, 0);

        if (!eulerCircuit.isEmpty()) {
            Edge currentEdge = eulerCircuit.getFirst(), predEdge;
            visited[Integer.parseInt(currentEdge.getId())] += 1;

            for (int i = 1; i < eulerCircuit.size(); i++) {
                predEdge = eulerCircuit.get(i);
                visited[Integer.parseInt(predEdge.getId())] += 1;

                if (!hasCommonNode(currentEdge, predEdge)) {
                    return false;
                }
                currentEdge = predEdge;
            }
        }
        return Stream.of(visited).filter(i -> i != 1).findFirst().isEmpty();
    }

    private static boolean hasCommonNode(Edge edge1, Edge edge2) {
        Node edge1Node0 = edge1.getNode0();
        Node edge1Node1 = edge1.getNode1();
        Node edge2Node0 = edge2.getNode0();
        Node edge2Node1 = edge2.getNode1();

        return edge1Node0.equals(edge2Node0)
                || edge1Node1.equals(edge2Node1)
                || edge1Node0.equals(edge2Node1)
                || edge1Node1.equals(edge2Node0);
    }

    private static boolean isEulerian(Graph eulerGraph) {
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