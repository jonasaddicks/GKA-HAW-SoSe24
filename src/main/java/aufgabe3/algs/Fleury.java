package aufgabe3.algs;

import org.graphstream.algorithm.util.DisjointSets;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import util.DisjointSetsComponents;

import static aufgabe1.algs.BreadthFirstSearch.shortestPathBFS;

public class Fleury {
    private static boolean checkEuler(Graph graph) {
        HashSet<Node> visited = new HashSet<>();
        //check degree
        //Begin at any arbitrary node of the graph G.
        // Proceed from that node using either depth-first or breadth-first search,
        // counting all nodes reached. Once the graph has been entirely traversed,
        // if the number of nodes counted is equal to the number of nodes of G,
        // the graph is connected; otherwise it is disconnected.
        for (Node node: graph) {
            if (node.getDegree() % 2 != 0) {
                return false;
            }
            node.neighborNodes().forEach(visited::add);
        }
        return visited.size() == graph.getNodeCount();
    }

    public static ArrayList<Edge> eulerCircleFleury(Graph graph) throws IllegalArgumentException {
        if (!checkEuler(graph)) throw new IllegalArgumentException("The graph is not an euler graph.");

        ArrayList<Edge> eulerCircle = new ArrayList<>();
        Node v0 = graph.getNode(getRandomIndex(graph.getNodeCount()));


        while (v0.getOutDegree() > 0){
            Edge e0 = v0.getEdge(getRandomIndex(v0.getOutDegree()));

            if (!isBridge(e0, graph)){
                graph.removeEdge(e0);
                eulerCircle.add(e0);
                v0 = e0.getTargetNode();
            }
        }

        return eulerCircle;
    }

    private static boolean isBridge(Edge e0, Graph graph) {
        DisjointSetsComponents<Node> disjointNodes = new DisjointSetsComponents<>(graph.getNodeCount());
        graph.removeEdge(e0);
        graph.edges().forEach(e -> disjointNodes.union(e.getNode0(), e.getNode1()));
        return disjointNodes.getComponents() == 1;
    }

    private static int getRandomIndex(int range) {
        Random rand = new Random();
        return rand.nextInt(range);
    }

    /**
     * @return  -ArrayList containing all edges in order
     *          -empty ArrayList if the graph is eulerian even though it has no edges
     *          -null if no circuit has been found
     */
}
