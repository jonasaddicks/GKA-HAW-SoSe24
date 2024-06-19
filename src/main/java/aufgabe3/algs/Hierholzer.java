package aufgabe3.algs;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import java.util.*;

import static aufgabe3.algs.GraphValidator.isEulerian;

/**
 * This class which is not instantiable provides an interface to apply Hierholzer's algorithm on a specified graph.
 */
public class Hierholzer {

    private static HashSet<Integer>[] crosspoints; //index = nodeID, contains all the circuitIDs a node is part of, any node with a set containing more than one element is a crosspoint between at least two circuits
    private static ArrayList<Circuit> circuits; //contains all the calculated circuits
    private static boolean[] edgeContained; //marks every edge of the graph whether its already in the eulerian circuit or not

    /**
     * This method calculates the eulerian circuit on a specified graph.
     *
     * @param graph the specified graph to calculate the eulerian circuit on
     * @return List containing all edges of the specified graph in order of the eulerian circuit,
     * //      null if there is no eulerian circuit on the specified graph
     */
    public static ArrayList<Edge> eulerCircuitHierholzer(Graph graph) {
        if (Objects.isNull(graph) || !isEulerian(graph)) {return null;} //test if graph is eulerian

        Node startingNode = graph.getNode(0);
        ArrayList<Node> eulerCircuitNodes = new ArrayList<>();
        boolean[] contained = new boolean[graph.getEdgeCount()]; //index = edgeID, marks every edge once its contained in the eulerian circuit

        _eulerCircuitHierholzer(startingNode, eulerCircuitNodes, contained); //calculate circuit
        return nodeCircuitToEdges(eulerCircuitNodes, graph); //translate from node circuit to edge circuit and return the eulerian circuit
    }

    /**
     * This method carries out the actual recursive calculation of the eulerian circuit.
     *
     * @param startingNode node at which the calculation of the circuit will begin
     * @param circuit list that will be filled with the edges of the eulerian circuit
     * @param contained array marking every edge once its contained in the circuit
     */
    private static void _eulerCircuitHierholzer(Node startingNode, ArrayList<Node> circuit, boolean[] contained) {
        Stack<Node> stack = new Stack<>(); //stack holding the traversed nodes
        Node currentNode = startingNode;
        Edge edge;
        boolean currentNodeHasUnmarkedEdge = currentNode.edges().anyMatch(e -> !contained[e.getIndex()]); //does the reviewed node have any edges not contained in the eulerian circuit?

        while (!stack.isEmpty() || currentNodeHasUnmarkedEdge) {

            if (currentNodeHasUnmarkedEdge) { //if false - a circuit has been closed
                stack.push(currentNode);

                edge = currentNode.edges().filter(e -> !contained[e.getIndex()]).findFirst().get(); //get a neighboring edge not yet contained in the eulerian circuit
                contained[edge.getIndex()] = true; //mark edge as contained
                currentNode = edge.getOpposite(currentNode);
            } else {
                circuit.add(currentNode);
                currentNode = stack.pop(); //traverse the circuit backwards, open a new circuit if a crosspoint has been found (currentNodeHasUnmarkedEdges == true)
            }
            currentNodeHasUnmarkedEdge = currentNode.edges().anyMatch(e -> !contained[e.getIndex()]); //evaluate the currentNode
        }
        circuit.add(currentNode); //add the first node again to close the eulerian Circuit
    }

    /**
     * This method translates a list of nodes into the corresponding list of edges between these nodes.
     *
     * @param nodes list of nodes to be translated
     * @param graph graph containing the specified nodes and edges
     * @return list containing the edges between the nodes of the specified list of nodes
     */
    private static ArrayList<Edge> nodeCircuitToEdges(ArrayList<Node> nodes, Graph graph) {
        ArrayList<Edge> edges = new ArrayList<>();
        boolean[] edgeContained = new boolean[graph.getEdgeCount()];
        Node node1 = nodes.getFirst(), node2;
        Optional<Edge> edge;

        for (int i = 1; i < nodes.size(); i++) {
            node2 = nodes.get(i); //get followup node of node1
            Node finalNode = node2;
            if (node1.getIndex() == node2.getIndex()) { //if next edge is a loop - use a stream and filter edges my mark in edgeContained[] to get all the different edges between two nodes (multigraph)
                edge = node1.edges().filter(e -> (e.getNode0().getIndex() == finalNode.getIndex() && e.getNode1().getIndex() == finalNode.getIndex()) && !edgeContained[Integer.parseInt(e.getId())]).findFirst();
            } else {
                edge = node1.edges().filter(e -> (e.getNode0().getIndex() == finalNode.getIndex() || e.getNode1().getIndex() == finalNode.getIndex()) && !edgeContained[Integer.parseInt(e.getId())]).findFirst();
            }

            edgeContained[edge.get().getIndex()] = true; //mark edge as contained
            edges.add(edge.get());
            node1 = node2;
        }
        return edges;
    }

//    /**
//     * Calculates and returns the eulerian circuit on the specified graph.
//     *
//     * @param graph graph on which an eulerian circuit is to be calculated
//     * @return List containing all edges of the specified graph in order of the eulerian circuit,
//     *         null if there is no eulerian circuit on the specified graph
//     */
//    public static synchronized ArrayList<Edge> eulerCircuitHierholzer(Graph graph) {
//        if (Objects.isNull(graph) || !isEulerian(graph)) {return null;} //test if graph is eulerian
//        if (graph.getEdgeCount() == 0) {return new ArrayList<>();}
//
//        Graph graphClone = cloneGraph(graph); //create a clone of the specified graph to work on
//        Node currentNode = null, successorNode;
//        Edge eulerEdge;
//
//        circuits = new ArrayList<>();
//        crosspoints = new HashSet[graph.getNodeCount()];
//        edgeContained = new boolean[graph.getEdgeCount()];
//        int circuitCount = 0;
//        int edgeCount = graphClone.getEdgeCount();
//
//        while (edgeCount > 0) { //while the cloned graph != null graph
//            if (Objects.isNull(currentNode)) { //new circuit is to be created
//                currentNode = graphClone.nodes().filter(n -> n.getDegree() > 0).findFirst().get(); //any node with a degree >= 2
//                circuits.add(new Circuit(graph.getNode(currentNode.getId()), circuitCount));
//            }
//
//            successorNode = currentNode.neighborNodes().findFirst().get(); //any node connected to the currentNode
//
//            int nodeID = Integer.parseInt(successorNode.getId());
//            if (Objects.isNull(crosspoints[nodeID])) {
//                crosspoints[nodeID] = new HashSet<>();
//            }
//            crosspoints[nodeID].add(circuitCount); //assign the node the circuit containing the node
//
//            eulerEdge = currentNode.getEdgeBetween(successorNode);
//            circuits.get(circuitCount).nodes.add(graph.getNode(successorNode.getId())); //add the next node on the path to the circuit
//            graphClone.removeEdge(eulerEdge); //remove the edge from the cloned graph, so it doesn't have to be taken into account any longer
//            edgeCount--;
//
//            if (Objects.equals(successorNode.getId(), circuits.get(circuitCount).nodes.getFirst().getId())) { //if the circuit has been closed
//                currentNode = null; //currentNode = null to mark the creation of a new circuit in the next iteration
//                circuitCount++;
//            } else {
//                currentNode = successorNode;
//            }
//        }
//
//        ArrayList<Edge> eulerCircuit = new ArrayList<>();
//        traverseCircuits(circuits.getFirst(), 0, new HashSet<>(), eulerCircuit); //initial call of the method calculating the eulerian circuit
//
//        return eulerCircuit;
//    }

//    /**
//     * Traverses and builds the eulerian circuit recursively using the circuits calculated beforehand.
//     *
//     * @param circuit circuit to traverse
//     * @param nodeCircuitIndex starting point in the specified circuit
//     * @param openCircuits circuits that are (partially) contained in the eulerian circuit
//     * @param eulerCircuit eulerian circuit combining all the specified circuits
//     */
//    private static void traverseCircuits(Circuit circuit, int nodeCircuitIndex, HashSet<Integer> openCircuits, ArrayList<Edge> eulerCircuit) {
//        openCircuits.add(circuit.circuitID); //mark current circuit as opened
//        if (nodeCircuitIndex != 0) { //adjust the circuit if the starting point isn't at the beginning - e.g. A,B,C,D,A -> B,C,C,D,A for nodeCircuitIndex = 2 (C)
//            circuit.nodes.add(nodeCircuitIndex, circuit.nodes.get(nodeCircuitIndex));
//            circuit.nodes.removeFirst();
//        }
//        Node sourceNode = circuit.nodes.remove(nodeCircuitIndex), targetNode;
//        Edge eulerEdge;
//
//        while (!circuit.nodes.isEmpty()) {
//
//            if (nodeCircuitIndex == circuit.nodes.size()) {nodeCircuitIndex = 0;} //return to the start of the list representing the circuit if the circuit isn't fully traversed yet and nodeCircuitIndex > 0
//
//            targetNode = circuit.nodes.remove(nodeCircuitIndex);
//            Node finalTargetNode = targetNode;
//            if (sourceNode.getIndex() == targetNode.getIndex()) { //if next edge is a loop
//                eulerEdge = sourceNode.edges().filter(e -> (e.getNode0().getIndex() == finalTargetNode.getIndex() && e.getNode1().getIndex() == finalTargetNode.getIndex()) && !edgeContained[Integer.parseInt(e.getId())]).findFirst().get();
//            } else {
//                eulerEdge = sourceNode.edges().filter(e -> (e.getNode0().getIndex() == finalTargetNode.getIndex() || e.getNode1().getIndex() == finalTargetNode.getIndex()) && !edgeContained[Integer.parseInt(e.getId())]).findFirst().get();
//            }
//
//            edgeContained[Integer.parseInt(eulerEdge.getId())] = true; //mark edge as contained in the eulerian circuit
//            eulerCircuit.add(eulerEdge);
//
//            HashSet<Integer> crossingCircuits = new HashSet<>(crosspoints[Integer.parseInt(targetNode.getId())]);
//            crossingCircuits.removeAll(openCircuits);
//            if (!crossingCircuits.isEmpty()) { //check if a crosspoint has been reached
//                Circuit recursiveCircuit = circuits.get(crossingCircuits.stream().findFirst().get());
//                int recursiveIndex = recursiveCircuit.nodes.indexOf(targetNode); //get the index of the node in the circuit which has been crossed
//                traverseCircuits(recursiveCircuit, recursiveIndex, openCircuits, eulerCircuit); //recursive call to squeeze in the crossing circuit
//            }
//
//            sourceNode = targetNode;
//        }
//    }

//    /**
//     * Clones the specified graph.
//     * Does <b>only</b> clone edges and nodes while attributes, type of graph, stylesheets, etc. are ignored.
//     *
//     * @param graph graph to be cloned
//     * @return clone of the specified graph
//     */
//    private static Graph cloneGraph(Graph graph) {
//        MultiGraph graphClone = new MultiGraph(String.format("%s_copy", graph.getId()), false, true);
//        graph.edges().forEach(e -> graphClone.addEdge(e.getId(), e.getNode0().getId(), e.getNode1().getId()));
//        return graphClone;
//    }



    /**
     * Inner representation of a single circuit in the eulerian graph.
     */
    private static class Circuit {
        private final ArrayList<Node> nodes;
        private final int circuitID;

        /**
         * Constructs a new circuit consisting of the specified startingNode and circuitID.
         *
         * @param startingNode first node of the circle
         * @param circuitID id of the circle
         */
        public Circuit(Node startingNode, int circuitID) {
            this.nodes = new ArrayList<>();
            nodes.add(startingNode);
            this.circuitID = circuitID;
        }

        @Override
        public String toString() {
            return String.format("Circuit %d: %s", circuitID, nodes);
        }
    }
}
