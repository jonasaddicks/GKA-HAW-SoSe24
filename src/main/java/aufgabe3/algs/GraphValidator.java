package aufgabe3.algs;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import util.DisjointSetsComponents;
import util.NodeDegree;

import java.util.Optional;

/**
 * The GraphValidator provides an interface to check graphs on different conditions.
 */
public class GraphValidator {
    /**
     * Private constructor.
     */
    private GraphValidator() {}

    /**
     * This method checks if a specified graph is eulerian.
     * Every node in an eulerian graph must have an even degree and the graph has to be connected.
     *
     * @param eulerGraph the specified graph to check
     * @return true if the specified graph is eulerian
     */
    public static boolean isEulerian(Graph eulerGraph) {
        if (eulerGraph.getNodeCount() < 1) {return false;} //a graph must have at least one node
        DisjointSetsComponents<Node> disjointNodes = new DisjointSetsComponents<>(eulerGraph.getNodeCount());
        Optional<Node> node = eulerGraph.nodes()
                .peek(disjointNodes::add) //add all nodes to the disjointSet
                .filter(n -> NodeDegree.getDegree(n) % 2 != 0) //filter any node with an uneven degree
                .findFirst(); //get first node that breaks the condition if any exists
        if (node.isPresent()) { //if any node breaks the condition the graph is not eulerian
            return false;
        }
        eulerGraph.edges().forEach(e -> disjointNodes.union(e.getNode0(), e.getNode1())); //join every connected node in the disjointSet
        return disjointNodes.getComponents() == 1; //check if the graph is connected - if number of components > 1 the graph is not connected
    }
}
