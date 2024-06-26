package aufgabe2.generator;

import aufgabe1.storage.GraphBuilder;
import aufgabe1.storage.GraphTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Random;

public class RandomGraphConnected {

    private RandomGraphConnected() {}

    private static final boolean DIRECTED = false;
    private static final int MAXWEIGHT = 1000;

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
    public static GraphTemplate generateConnectedTemplate(int numberOfNodes, int numberOfEdges, String randomGraphName, int properties) {
        if (numberOfEdges < numberOfNodes - 1) {
            throw new IllegalArgumentException(String.format("edges: %d, nodes: %d - edges has to be at least equal to nodes - 1", numberOfEdges, numberOfNodes));
        }

        GraphTemplate randomTemplate = new GraphTemplate(randomGraphName, properties);

        Random randomIndex = new Random();
        ArrayList<Integer> existingNodes = new ArrayList<>();
        int nodeIDCount = 0;

        Integer node1 = nodeIDCount++;
        randomTemplate.addNode(String.valueOf(node1));//TODO ?
        existingNodes.add(node1);
        Integer node2;

        for (int i = 0; i < numberOfNodes - 1; i++) {
            node1 = nodeIDCount++;
            randomTemplate.addNode(String.valueOf(node1));
            node2 = existingNodes.get(randomIndex.nextInt(existingNodes.size()));
            randomTemplate.addEdge(String.valueOf(node1), String.valueOf(node2), DIRECTED, randomIndex.nextInt(MAXWEIGHT), null);
            existingNodes.add(node1);
        }

        for (int i = 0; i < numberOfEdges - numberOfNodes - 1; i++) {
            node1 = existingNodes.get(randomIndex.nextInt(existingNodes.size()));
            node2 = existingNodes.get(randomIndex.nextInt(existingNodes.size()));
            randomTemplate.addEdge(String.valueOf(node1), String.valueOf(node2), DIRECTED, randomIndex.nextInt(MAXWEIGHT), null);
        }

        return randomTemplate;
    }

    public static GraphBuilder.GraphInstance generateConnectedGraph (int numberOfNodes, int numberOfEdges, String randomGraphName, GraphBuilder graphBuilder, URI stylesheet, int properties) throws MalformedURLException {;
        return graphBuilder.buildGraphFromTemplate(stylesheet, generateConnectedTemplate(numberOfNodes, numberOfEdges, randomGraphName, properties));
    }
}
