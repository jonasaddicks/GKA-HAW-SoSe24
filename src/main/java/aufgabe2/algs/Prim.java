package aufgabe2.algs;

import org.graphstream.algorithm.util.FibonacciHeap;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

public class Prim {

    private Prim() {}

    public static synchronized Graph minimalSpanningTreePrim(Graph graph) throws IllegalArgumentException {

        FibonacciHeap<Integer, Node> nodeHeap = new FibonacciHeap<>();

        Graph minimalSpanningTree = new MultiGraph("minimalSpanningTree", false, false);

        Node leafToBeAdded = graph.getNode("0"); //starting node
        boolean[] containedInMinimalSpanningTree = new boolean[graph.getNodeCount()]; //NodeID = Index, if a node is already in the minimalSpanningTree then containedInMinimalSpanningTree[node.nodeID] = true
        FibonacciHeap.Node[] nodeHeapPointer = new FibonacciHeap.Node[graph.getNodeCount()]; //NodeID = Index, points to the corresponding node in the fibonacciHeap of a node
        Node[] predecessorNodes = new Node[graph.getNodeCount()]; //NodeID = Index, contains the predecessor node (which is already contained in the minimalSpanningTree) of a node with the currently lowest cost to add to the tree

        predecessorNodes[0] = leafToBeAdded; //starting node predecessor = starting node
        nodeHeapPointer[0] = nodeHeap.add(0, leafToBeAdded); //fibonacciHeap node of the starting node

        Edge linkBetweenTreeAndLeafSourceGraph;
        Edge linkBetweenTreeAndLeafTargetGraph;

        for (int nodeID = 1; nodeID < graph.getNodeCount(); nodeID++) { //add all remaining nodes to the heap
            nodeHeapPointer[nodeID] = nodeHeap.add(Integer.MAX_VALUE, graph.getNode(String.valueOf(nodeID))); //mark all remaining nodes as 'unreachable' (Integer.MAX_VALUE)
        }

        float startTime = System.nanoTime();
        while (!nodeHeap.isEmpty()) {
            if (nodeHeap.getMinKey() == Integer.MAX_VALUE) {
                throw new IllegalArgumentException(String.format("graph %s is not connected", graph.getId()));
            }
            leafToBeAdded = nodeHeap.extractMin(); //the currently lowest valued node

            containedInMinimalSpanningTree[Integer.parseInt(leafToBeAdded.getId())] = true; //mark the currently lowest valued node as contained in the tree
            Node addedLeaf = minimalSpanningTree.addNode(leafToBeAdded.getId()); //add the lowest valued node to the tree
            addedLeaf.setAttribute("nodeMarker", leafToBeAdded.getAttribute("nodeMarker"));
            addedLeaf.setAttribute("ui.label", leafToBeAdded.getAttribute("ui.label")); //copy node label

            if (leafToBeAdded != predecessorNodes[0]) { //add edge between the currently lowest valued node and its predecessor and copy its attributes
                linkBetweenTreeAndLeafSourceGraph = leafToBeAdded.getEdgeBetween(predecessorNodes[Integer.parseInt(leafToBeAdded.getId())]);
                linkBetweenTreeAndLeafTargetGraph = minimalSpanningTree.addEdge(linkBetweenTreeAndLeafSourceGraph.getId(), linkBetweenTreeAndLeafSourceGraph.getNode0().getId(), linkBetweenTreeAndLeafSourceGraph.getNode1().getId(), false);
                linkBetweenTreeAndLeafTargetGraph.setAttribute("weight", linkBetweenTreeAndLeafSourceGraph.getAttribute("weight"));
                linkBetweenTreeAndLeafTargetGraph.setAttribute("edgeMarker", linkBetweenTreeAndLeafSourceGraph.getAttribute("edgeMarker"));
                linkBetweenTreeAndLeafTargetGraph.setAttribute("ui.label", linkBetweenTreeAndLeafSourceGraph.getAttribute("ui.label")); //copy edge label
            }

            Node finalLeafToBeAdded = leafToBeAdded;
            leafToBeAdded.neighborNodes() //evaluate neighboring nodes and their distance to the tree
                    .filter(n -> !containedInMinimalSpanningTree[Integer.parseInt(n.getId())]) //filter nodes which are not yet contained in the minimalSpanningTree
                    .forEach(n -> {
                        int weight = (int) finalLeafToBeAdded.getEdgeBetween(n).getAttribute("weight"); //get weight of edge between newly added node and n
                        if (weight < (int) nodeHeapPointer[Integer.parseInt(n.getId())].getKey()) { //if new weight is smaller than current weight -> overwrite
                            nodeHeap.decreaseKey(nodeHeapPointer[Integer.parseInt(n.getId())], weight); //decrease key (weight) in fibonacciHeap
                            predecessorNodes[Integer.parseInt(n.getId())] = finalLeafToBeAdded; //update predecessor
                        }
                    });
        }

        float finishTime = System.nanoTime();
        float elapsedTime = (finishTime - startTime) / 1000 / 1000 / 1000;

        System.out.printf("elapsed time for prim: %fs%n", elapsedTime);

        return minimalSpanningTree;
    }
}
