package aufgabe3.generator;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import util.NodeDegree;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class RandomGraphGeneratorEuler {

    public static Graph generateEulerianGraph (int numberOfNodes, int avgDegree, String randomGraphName) {
        Graph randomGraph = new MultiGraph(randomGraphName, false, false);
        int numberOfEdges = avgDegree * numberOfNodes / 2;

        Random randomIndex = new Random();
        ArrayList<Node> existingNodes = new ArrayList<>();
        int nodeIDCount = 0;
        int edgeIDCount = 0;

        Node node1 = randomGraph.addNode(String.valueOf(nodeIDCount++));
        node1.setAttribute("ui.label", String.format("nodeID:%d", 0));//TODO delete
        existingNodes.add(node1);
        Node node2;

        for (int i = 0; i < numberOfNodes - 1; i++) {
            node1 = randomGraph.addNode(String.valueOf(nodeIDCount++));
            node1.setAttribute("ui.label", String.format("nodeID:%d", nodeIDCount-1));//TODO delete
            node2 = existingNodes.get(randomIndex.nextInt(existingNodes.size()));
            Edge edge = randomGraph.addEdge(String.valueOf(edgeIDCount++), node1, node2, false);
            edge.setAttribute("ui.label", String.format("edgeID:%d", edgeIDCount-1));//TODO delete
            numberOfEdges--;
            existingNodes.add(node1);
        }

        while (numberOfEdges > 0) {
            node1 = existingNodes.get(randomIndex.nextInt(existingNodes.size()));
            node2 = existingNodes.get(randomIndex.nextInt(existingNodes.size()));
            Edge edge = randomGraph.addEdge(String.valueOf(edgeIDCount++), node1, node2, false);
            edge.setAttribute("ui.label", String.format("edgeID:%d", edgeIDCount-1));//TODO delete
            numberOfEdges--;
        }

        Connector connector = new Connector(randomGraph);
        for (Node node : existingNodes) {
            if (NodeDegree.getDegree(node) % 2 != 0) {
                if (connector.connect(node, edgeIDCount)) {
                    edgeIDCount++;
                }
            }
        }
        return randomGraph;
    }

    private static class Connector{
        private final Graph graph;
        private Node source;

        public Connector(Graph graph) {
            this.graph = graph;
            source = null;
        }

        private boolean connect(Node node, int edgeIDCount) {
            if (Objects.isNull(source)) {
                source = node;
                return false;
            }

            Edge edge = graph.addEdge(String.valueOf(edgeIDCount), source, node, false);
            edge.setAttribute("ui.label", String.format("edgeID:%d", edgeIDCount));//TODO delete
            source = null;
            return true;
        }
    }
}
