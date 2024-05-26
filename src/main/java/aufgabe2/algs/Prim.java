package aufgabe2.algs;

import org.graphstream.algorithm.util.FibonacciHeap;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import java.util.Objects;

public class Prim {

    public static synchronized Graph minimalSpanningTreePrim(Graph graph) {

        FibonacciHeap<Integer, Node> nodeHeap = new FibonacciHeap<>();

        Graph minimalSpanningTree = new MultiGraph("minimalSpanningTree", false, false);

        Node leafToBeAdded = graph.getNode("0");
        boolean[] containedInMinimalSpanningTree = new boolean[graph.getNodeCount()];
        FibonacciHeap.Node[] nodeHeapPointer = new FibonacciHeap.Node[graph.getNodeCount()];
        Node[] predecessorNodes = new Node[graph.getNodeCount()];
        predecessorNodes[0] = leafToBeAdded;

        Edge linkBetweenTreeAndLeafSourceGraph;
        Edge linkBetweenTreeAndLeafTargetGraph;

        nodeHeapPointer[0] = nodeHeap.add(0, leafToBeAdded);

        for (int nodeID = 1; nodeID < graph.getNodeCount(); nodeID++) {
            nodeHeapPointer[nodeID] = nodeHeap.add(Integer.MAX_VALUE, graph.getNode(String.valueOf(nodeID)));
        }

        while (!nodeHeap.isEmpty()) {
            leafToBeAdded = nodeHeap.extractMin();
            containedInMinimalSpanningTree[Integer.parseInt(leafToBeAdded.getId())] = true;

            minimalSpanningTree.addNode(leafToBeAdded.getId());
            if (leafToBeAdded != predecessorNodes[0]) {
                linkBetweenTreeAndLeafSourceGraph = leafToBeAdded.getEdgeBetween(predecessorNodes[Integer.parseInt(leafToBeAdded.getId())]);
                linkBetweenTreeAndLeafTargetGraph = minimalSpanningTree.addEdge(linkBetweenTreeAndLeafSourceGraph.getId(), linkBetweenTreeAndLeafSourceGraph.getNode0(), linkBetweenTreeAndLeafSourceGraph.getNode1(), false);
                linkBetweenTreeAndLeafTargetGraph.setAttribute("weight", linkBetweenTreeAndLeafSourceGraph.getAttribute("weight"));
                linkBetweenTreeAndLeafTargetGraph.setAttribute("nodeMarker", linkBetweenTreeAndLeafSourceGraph.getAttribute("nodeMarker"));
            }

            leafToBeAdded.neighborNodes()
                    .filter(n -> !containedInMinimalSpanningTree[Integer.parseInt(n.getId())])
                    .forEach(n -> {
                        //TODO
                    });
        }
        return null;
    }
}
