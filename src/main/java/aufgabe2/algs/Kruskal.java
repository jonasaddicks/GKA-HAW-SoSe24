package main.java.aufgabe2.algs;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Kruskal {
    /**
     * Input: Graph
     * 1. Schritt Nummerierung der Kanten nach aufsteigender Länge
     * 2. Schritt für i = 0 ... |E|, überprüfen, ob die neue Kante einen Kreis bildet, falls nicht, zu neuem Graph hinzufügen
     * Output: Graph (minimales Spanngerüst des eingegebenen Graphen)
     */
    public static synchronized Graph getMinimalSpanningTree (Graph graph){
        Graph minimalSpanningTree = new MultiGraph("minimalSpanningTree",false,false);

        // adding all the nodes to the spanningTree without the edges
        graph.nodes().forEach(node -> minimalSpanningTree.addNode(node.getId()));

        // sort the edges by their weight
        List<Edge> edgeList = graph.edges().sorted(Comparator.comparingInt(edge -> (int) edge.getAttribute("weight"))).toList();

        // first edge can be added because we start with an empty graph
        minimalSpanningTree.addEdge(edgeList.getFirst().getId(),edgeList.getFirst().getSourceNode(),edgeList.getFirst().getTargetNode());

        // iterate over the edgeList and check each for cycles, then add to spanningTree
        for (int i = 1; i < edgeList.size(); i++) {
            Edge workingEdge = edgeList.get(i);
            if (!checkForCycle(workingEdge,minimalSpanningTree)) minimalSpanningTree.addEdge(workingEdge.getId(), workingEdge.getSourceNode(), workingEdge.getTargetNode());

        }
        return minimalSpanningTree;
    }

    private static boolean checkForCycle(Edge workingEdge, Graph graph){
        // if edge is already in graph there is no need to add again
        if (graph.edges().toList().contains(workingEdge)) return true;

        // To check if adding the workingEdge to the graph produces cycles
        graph.addEdge(workingEdge.getId(), workingEdge.getSourceNode(), workingEdge.getTargetNode());

        // keeping track of all the nodes of the graph
        // to check the whole graph we would initialize empty sets but we already have
        Set <Node> visited = new HashSet<>();
        Set <Node> workingSet = new HashSet<>();

        for (Node node: graph){
            if (!visited.contains(node)){
                if (isCyclic(node, visited, workingSet)) return true;
            }
        }
        return false;
    }

    private static boolean isCyclic(Node node, Set<Node> visited, Set<Node> workingSet) {
        // If a node is found that is already in the workingSet, a cycle is detected.
        visited.add(node);
        workingSet.add(node);

        // Iterate over the edges of the node
        for (Edge edge: node.edges().toList()){
            Node neighbour = edge.getOpposite(node);
            if (!visited.contains(neighbour)){
                if (isCyclic(neighbour,visited,workingSet)) return true;
            }
            else if (workingSet.contains(neighbour)) return true;
        }
        workingSet.remove(node);
        return false;
    }
}