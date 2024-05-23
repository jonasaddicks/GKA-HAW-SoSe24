package aufgabe2.algs;

import org.graphstream.algorithm.util.DisjointSets;
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
    private static Graph workingGraph;
    private static Graph MST;
    /**
     * Input: Graph
     * 1. Schritt Nummerierung der Kanten nach aufsteigender Länge
     * 2. Schritt für i = 0 ... |E|, überprüfen, ob die neue Kante einen Kreis bildet, falls nicht, zu neuem Graph hinzufügen
     * Output: Graph (minimales Spanngerüst des eingegebenen Graphen)
     */
    public static synchronized Graph getMinimalSpanningTree (Graph graph){
        Graph minimalSpanningTree = new MultiGraph("minimalSpanningTree",false,false);
        DisjointSets<Node> disjointNodes = new DisjointSets<>(graph.getNodeCount());


        // adding all the nodes to the spanningTree without the edges
        graph.nodes().forEach(node -> {
            minimalSpanningTree.addNode(node.getId());
            disjointNodes.add(node);
        });

        // sort the edges by their weight
        List<Edge> edgeList = graph.edges().sorted(Comparator.comparingInt(edge -> (int) edge.getAttribute("weight"))).toList();


        // first edge can be added because we start with an empty graph
        minimalSpanningTree.addEdge(edgeList.getFirst().getId(),edgeList.getFirst().getSourceNode(),edgeList.getFirst().getTargetNode());
        disjointNodes.union(edgeList.getFirst().getSourceNode(),edgeList.getFirst().getTargetNode());

        // iterate over the edgeList and check each for cycles, then add to spanningTree
        for (int i = 1; i < edgeList.size(); i++) {
            Edge workingEdge = edgeList.get(i);
            // To check if adding the workingEdge to the graph produces cycles
            // In the beginning no nodes are connected, so each node is found in a one-element-disjointSet
            if (!disjointNodes.inSameSet(workingEdge.getSourceNode(), workingEdge.getTargetNode())) {
                disjointNodes.union(workingEdge.getSourceNode(), workingEdge.getTargetNode());
                minimalSpanningTree.addEdge(workingEdge.getId(), workingEdge.getSourceNode(), workingEdge.getTargetNode());
            }

        }
        return minimalSpanningTree;
    }

}