package algs.aufgabe3;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static aufgabe3.algs.GraphValidator.isEulerian;


public class EulerCircuitTestHelper {

    /**
     * This method verifies that a specified eulerian circuit is valid on a specified graph.
     * The eulerian circuit must contain every edge of the graph exactly once and two following edges have to be connected.
     *
     * @param eulerCircuit the specified eulerian circuit on the specified graph
     * @param eulerGraph the specified graph on which the eulerian circuit has been calculated
     * @return true if the eulerian circuit is valid
     */
    public static boolean validEulerCircuit(ArrayList<Edge> eulerCircuit, Graph eulerGraph) {
        if (!isEulerian(eulerGraph) || Objects.isNull(eulerCircuit)) {return false;}

        Integer[] visited = new Integer[eulerGraph.getEdgeCount()]; //array marking how often an edge has been referenced
        Arrays.fill(visited, 0); //in the beginning no edge has been referenced yet

        if (!eulerCircuit.isEmpty()) {
            Edge currentEdge = eulerCircuit.getFirst(), predEdge;
            visited[Integer.parseInt(currentEdge.getId())] += 1;

            for (int i = 1; i < eulerCircuit.size(); i++) {
                predEdge = eulerCircuit.get(i);
                visited[Integer.parseInt(predEdge.getId())] += 1;

                if (!hasCommonNode(currentEdge, predEdge)) { //check if edge1 and its followup edge2 are connected
                    return false;
                }
                currentEdge = predEdge;
            }
        }
        boolean isClosed = true, containsAllEdges = Stream.of(visited).filter(i -> i != 1).findFirst().isEmpty(); //check if every edge has been referenced exactly once
        if (eulerCircuit.size() > 2) {
            isClosed = hasCommonNode(eulerCircuit.getFirst(), eulerCircuit.getLast()); //check if the circuit is closed
        }
        return isClosed && containsAllEdges;
    }

    /**
     * This method checks if two specified edges have at least one common node and are therefore connected.
     *
     * @param edge1 edge1
     * @param edge2 edge2
     * @return true if the two specified edges are connected
     */
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
}
