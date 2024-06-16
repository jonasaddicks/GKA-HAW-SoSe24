package aufgabe3.algs;

import aufgabe3.generator.RandomGraphGeneratorEuler;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

import static aufgabe3.algs.GraphValidator.isEulerian;

public class Hierholzer {

    private static HashSet<Integer>[] crosspoints;
    private static ArrayList<Circuit> circuits;

    /**
     * @return  -ArrayList containing all edges in order
     *          -empty ArrayList if the graph is eulerian even though it has no edges
     *          -null if no circuit has been found
     */
    public static synchronized ArrayList<Edge> eulerCircuitHierholzer(Graph graph) {
        if (Objects.isNull(graph) || !isEulerian(graph)) {return null;}

        Graph graphClone = cloneGraph(graph);
        Node currentNode = null, successorNode;
        Edge eulerEdge;

        circuits = new ArrayList<>();
        crosspoints = new HashSet[graph.getNodeCount()];
        int circuitCount = 0;
        int edgeCount = graphClone.getEdgeCount();

        while (edgeCount > 0) {
            if (Objects.isNull(currentNode)) {
                currentNode = graphClone.nodes().filter(n -> n.getDegree() > 0).findFirst().get();
                circuits.add(new Circuit(currentNode, circuitCount));
            }

            successorNode = currentNode.neighborNodes().findFirst().get(); //what happens with loops?

            int nodeID = Integer.parseInt(successorNode.getId());
            if (Objects.isNull(crosspoints[nodeID])) {
                crosspoints[nodeID] = new HashSet<>();
            }
            crosspoints[nodeID].add(circuitCount);

            Node originalNode = graph.getNode(nodeID); //TODO delete
            originalNode.setAttribute("ui.label", String.format("nodeID:%d circuits: %s", nodeID, crosspoints[nodeID])); //TODO delete

            eulerEdge = currentNode.getEdgeBetween(successorNode);
            circuits.get(circuitCount).nodes.add(successorNode);
            graphClone.removeEdge(eulerEdge);
            edgeCount--;

            if (successorNode == circuits.get(circuitCount).nodes.getFirst()) {
                currentNode = null;
                circuitCount++;
            } else {
                currentNode = successorNode;
            }
        }

        //TODO delete
        for (Circuit c : circuits) {
            System.out.println(c);
        }
        System.out.println();

        ArrayList<Edge> eulerCircuit = new ArrayList<>();
        //traverseCircuits(circuits.getFirst(), new HashSet<>(), eulerCircuit);

        System.out.printf("expected: %d, actual: %d%n", graph.getEdgeCount(), eulerCircuit.size());//TODO delete
        System.out.println(eulerCircuit);//TODO delete
        return null;
    }

    private static void traverseCircuits(Circuit circuit, Node startingNode, HashSet<Integer> openCircuits, ArrayList<Edge> eulerCircuit) {
        openCircuits.add(circuit.circuitID);
        Node sourceNode = startingNode, targetNode;
        Edge eulerEdge;

        while (!circuit.nodes.isEmpty()) {

//            HashSet<Integer> crossingCircuits = new HashSet<>(crosspoints[Integer.parseInt(connectingNode.getId())]);
//            crossingCircuits.removeAll(openCircuits);
//            if (!crossingCircuits.isEmpty()) {
//                traverseCircuits(circuits.get(crossingCircuits.stream().findFirst().get()), openCircuits, eulerCircuit);
//            }
        }
    }

    private static Node getOppositeNode(Node node, Edge edge){
        Node node0 =  edge.getNode0();
        Node node1 =  edge.getNode1();

        return node.equals(node0) ? node1 : node0;
    }

    private static Node getConnectingNode(Edge edge1, Edge edge2) {
        Node edge1Node0 = edge1.getNode0();
        Node edge1Node1 = edge1.getNode1();
        Node edge2Node0 = edge2.getNode0();
        Node edge2Node1 = edge2.getNode1();

        if(edge1Node0.equals(edge2Node0) || edge1Node0.equals(edge2Node1)) {return edge1Node0;}
        return edge1Node1;
    }

    private static Graph cloneGraph(Graph graph) {
        MultiGraph graphClone = new MultiGraph(String.format("%s_copy", graph.getId()), false, true);
        graph.edges().forEach(e -> graphClone.addEdge(e.getId(), e.getNode0().getId(), e.getNode1().getId()));
        return graphClone;
    }



    private static class Circuit {
        private final ArrayList<Node> nodes;
        private final int circuitID;

        public Circuit(Node startingNode, int circuitID) {
            this.nodes = new ArrayList<>();
            nodes.add(startingNode);
            this.circuitID = circuitID;
        }

        public void addNode(Node node) {
            this.nodes.add(node);
        }

        @Override
        public String toString() {
            return String.format("Circuit %d: %s", circuitID, nodes);
        }
    }



    //TODO delete
    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "swing");
        Graph graph1 = RandomGraphGeneratorEuler.generateEulerianGraph(10, 2, "iwas");
        graph1.display();

        eulerCircuitHierholzer(graph1);
    }
}
