package aufgabe3.algs;

import aufgabe3.generator.RandomGraphGeneratorEuler;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.graphicGraph.GraphicNode;

import java.util.ArrayList;
import java.util.Objects;

import static util.GraphValidater.isEulerian;

public class Hierholzer {

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

        ArrayList<Circuit> circuits = new ArrayList<>();
        int circuitCount = 0;
        int edgeCount = graphClone.getEdgeCount();

        while (edgeCount > 0) {
            if (Objects.isNull(currentNode)) {
                currentNode = graphClone.nodes().filter(n -> n.getDegree() > 0).findFirst().get();
                circuits.add(new Circuit(currentNode, circuitCount));
            }

            successorNode = currentNode.neighborNodes().findFirst().get(); //what happens with loops?

            eulerEdge = currentNode.getEdgeBetween(successorNode);
            circuits.get(circuitCount).addEdge(eulerEdge);
            graphClone.removeEdge(eulerEdge);
            edgeCount--;

            if (successorNode == circuits.get(circuitCount).startingNode) {
                currentNode = null;
                circuitCount++;
            } else {
                currentNode = successorNode;
            }
        }

        for (Circuit c : circuits) {
            System.out.println(c);
        }

        return null;
    }

    private static Graph cloneGraph(Graph graph) {
        MultiGraph graphClone = new MultiGraph(String.format("%s_copy", graph.getId()), false, true);
        graph.edges().forEach(e -> graphClone.addEdge(e.getId(), e.getNode0().getId(), e.getNode1().getId()));
        return graphClone;
    }



    private static class Circuit {
        private final ArrayList<Edge> edges;
        private final Node startingNode;
        private final int circuitID;

        public Circuit(Node startingNode, int circuitID) {
            this.edges = new ArrayList<>();
            this.startingNode = startingNode;
            this.circuitID = circuitID;
        }

        public void addEdge(Edge edge) {
            this.edges.add(edge);
        }

        public Node getStartingNode() {
            return this.startingNode;
        }

        public ArrayList<Edge> getEdges() {
            return this.edges;
        }

        public int getCircuitID() {
            return this.circuitID;
        }

        @Override
        public String toString() {
            return String.format("Circuit %d: %s", circuitID, edges);
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
