package aufgabe2.generator;

import aufgabe1.storage.GraphTemplate;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import java.util.ArrayList;
import java.util.Random;

public class randomGraphGenerator {

    private static final boolean directed = false;

    /**
     * n = number of nodes
     * e = number of edges (mindestens n-1, da graph sonst nicht zusammenhängend)
     * Array/Liste zum Verwalten der Nodes, zufälliges Auswählen durch zufälligen Index
     *
     * Zuerst wird ein Spannbaum mit n Nodes generiert
     *      - Ursprungsnode generieren
     *      - wiederhole n - 1 mal (
     *      - node generieren
     *      - zufällige existierende Node auswählen und node inklusive Edge (mit beliebigem aber unterschiedlichem Gewicht) anhängen
     *      - Node dem Array/Liste hinzufügen )
     *
     * Anschließend werden die verbleibende Edges hinzugefügt
     *      - wiederhole e - (n - 1) mal (
     *      - Auswahl zwei zufälliger existierender Nodes
     *      - Generierung einer Edge (mit beliebigem aber unterschiedlichem Gewicht) zwischen den ausgewählten Nodes )
     */

    public static void generateConnectedGraph (int numberOfNodes, int numberOfEdges, String randomGraphName) {
        if (numberOfEdges < numberOfNodes - 1) {
            throw new IllegalArgumentException(String.format("edges: %d, nodes: %d - edges has to be at least equal to nodes - 1", numberOfEdges, numberOfNodes));
        }

        //TODO parallele Erzeugung des Templates
        GraphTemplate randomTemplate = new GraphTemplate(randomGraphName);
        Graph randomGraph = new MultiGraph(randomGraphName, false, false);

        Random randomIndex = new Random();
        ArrayList<Node> existingNodes = new ArrayList<>();
        int nodeIDCount = 0;
        int edgeIDCount = 0;

        Node node1 = randomGraph.addNode(String.valueOf(nodeIDCount++));
        existingNodes.add(node1);
        Node node2;
        Edge edge;

        for (int i = 0; i < numberOfNodes - 1; i++) {
            node1 = randomGraph.addNode(String.valueOf(nodeIDCount++));
            node2 = existingNodes.get(randomIndex.nextInt(existingNodes.size()));
            edge = randomGraph.addEdge(String.valueOf(edgeIDCount++), node1, node2, directed);
            edge.setAttribute("weight", 1); //TODO "beliebiges aber unterschiedliches Gewicht"
            existingNodes.add(node1);
        }

        for (int i = 0; i < numberOfEdges - numberOfNodes - 1; i++) {
            node1 = existingNodes.get(randomIndex.nextInt(existingNodes.size()));
            node2 = existingNodes.get(randomIndex.nextInt(existingNodes.size()));
            edge = randomGraph.addEdge(String.valueOf(edgeIDCount++), node1, node2, directed);
            edge.setAttribute("weight", 1); //TODO "beliebiges aber unterschiedliches Gewicht"
        }
    }
}
