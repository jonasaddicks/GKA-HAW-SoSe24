package aufgabe2.generator;

import aufgabe1.storage.GraphTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class randomGraphGenerator {

    private static final boolean DIRECTED = false;
    private static final int MAXWEIGHT = 100;

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
    public static void generateConnectedGraph (int numberOfNodes, int numberOfEdges, String randomGraphName) throws IOException {
        if (numberOfEdges < numberOfNodes - 1) {
            throw new IllegalArgumentException(String.format("edges: %d, nodes: %d - edges has to be at least equal to nodes - 1", numberOfEdges, numberOfNodes));
        }

        GraphTemplate randomTemplate = new GraphTemplate(randomGraphName);

        Random randomIndex = new Random();
        ArrayList<Integer> existingNodes = new ArrayList<>();
        int nodeIDCount = 0;

        Integer node1 = nodeIDCount++;
        randomTemplate.addNode(String.valueOf(nodeIDCount));
        existingNodes.add(node1);
        Integer node2;

        for (int i = 0; i < numberOfNodes - 1; i++) {
            node1 = nodeIDCount++;
            randomTemplate.addNode(String.valueOf(node1));
            node2 = existingNodes.get(randomIndex.nextInt(existingNodes.size()));
            randomTemplate.addEdge(String.valueOf(node1), String.valueOf(node2), false, randomIndex.nextInt(MAXWEIGHT), null);
            existingNodes.add(node1);
        }

        for (int i = 0; i < numberOfEdges - numberOfNodes - 1; i++) {
            node1 = existingNodes.get(randomIndex.nextInt(existingNodes.size()));
            node2 = existingNodes.get(randomIndex.nextInt(existingNodes.size()));
            randomTemplate.addEdge(String.valueOf(node1), String.valueOf(node2), false, randomIndex.nextInt(MAXWEIGHT), null);
        }

        randomTemplate.saveTemplate();
    }
}
