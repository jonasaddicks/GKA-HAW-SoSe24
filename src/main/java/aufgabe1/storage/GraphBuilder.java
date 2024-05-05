package aufgabe1.storage;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraphBuilder {

    private static GraphBuilder INSTANCE;

    private final Pattern EDGE_PATTERN = Pattern.compile("(?<node1>[\\wäöü]+)\\s*?(?<directed>->|--)\\s*?(?<node2>[\\wäöü]+)\\s*?\\(?(?<attribute>\\w*?)\\)?\\s*?:?\\s*?(?<weight>\\d*?)?\\s*?;");
    private List<Graph> graphBuilds;



    private GraphBuilder() {
        graphBuilds = new ArrayList<>();
    }

    public static GraphBuilder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GraphBuilder();
        }
        return INSTANCE;
    }

    public Graph buildGraph(URI graphURI, URI styleSheet) throws MalformedURLException {
        GraphTemplate template = buildTemplate(graphURI);

        Graph graph = new MultiGraph(template.getName(), false, true);
        graph.setAttribute("ui.stylesheet", String.format("url('%s')", styleSheet.toURL()));

        Node node1;
        Node node2;
        Edge edge;

        for (GraphEdge templateEdge : template.getGraphEdges()) {
            node1 = graph.addNode(templateEdge.getNode1());
            node1.setAttribute("ui.label", templateEdge.getNode1());
            node2 = graph.addNode(templateEdge.getNode2());
            node2.setAttribute("ui.label", templateEdge.getNode2());
            edge = graph.addEdge(UUID.randomUUID().toString(), node1, node2, templateEdge.isDirected());
            //TODO
        }
        System.out.println(template);

        return graph;
    }

    private GraphTemplate buildTemplate(URI graphURI) {

        GraphTemplate graphTemplate = new GraphTemplate(graphURI.getPath());

        try {
            FileInputStream edgeStream = new FileInputStream(graphURI.getPath());
            BufferedReader edgeReader = new BufferedReader(new InputStreamReader(edgeStream));

            String edgeLine;
            Matcher matcher;
            while ((edgeLine = edgeReader.readLine()) != null) {
                matcher = EDGE_PATTERN.matcher(edgeLine);
                if (matcher.find()) {
                    graphTemplate.addEdge(
                            matcher.group("node1"),
                            matcher.group("node2"),
                            switch (matcher.group("directed")) {
                                case "--" -> false;
                                case "->" -> true;
                                default -> throw new IllegalArgumentException(String.format("graph type '%s' not allowed", matcher.group("directed")));
                            },
                            (!matcher.group("weight").isEmpty()) ? Integer.valueOf(matcher.group("weight")) : null,
                            matcher.group("attribute")
                    );
                }
            }
            graphTemplate.setDirected();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return graphTemplate;
    }
}
