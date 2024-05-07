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

    private final Pattern EDGE_PATTERN = Pattern.compile("(?<node1>[\\wäöü]+)\\s*?(?<directed>->|--)\\s*?(?<node2>[\\wäöü]+)\\s*?\\(?(?<attribute>[\\wäöü]*?)\\)?\\s*?:?\\s*?(?<weight>\\d*?)?\\s*?;");
    private final int STANDARD_WEIGHT = 0;
    private List<Graph> graphBuilds = new ArrayList<>();
    private int edgeID = -1;
    private int nodeID = 0;



    private GraphBuilder() {}

    public static GraphBuilder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GraphBuilder();
        }
        return INSTANCE;
    }



    public Graph buildGraphFromFile(URI graphURI, URI styleSheet) throws MalformedURLException {
        return buildGraphFromFile(graphURI, styleSheet, 0);
    }

    public Graph buildGraphFromFile(URI graphURI, URI styleSheet, int properties) throws MalformedURLException {
        GraphTemplate template = buildTemplate(graphURI, properties);
        return buildGraphFromTemplate(styleSheet, template);
    }

    public Graph buildGraphFromTemplate(URI styleSheet, GraphTemplate graphTemplate) throws MalformedURLException {
        Graph graph = new MultiGraph(graphTemplate.getName(), false, false);
        graph.setAttribute("ui.stylesheet", String.format("url('%s')", styleSheet.toURL()));

        Node node1;
        Node node2;
        Edge edge;

        for (GraphEdge templateEdge : graphTemplate.getGraphEdges()) {
            node1 = graph.addNode(templateEdge.getNode1());
            if (Objects.isNull(node1.getAttribute("id"))) {node1.setAttribute("id", nodeID++);}
            node2 = graph.addNode(templateEdge.getNode2());
            if (Objects.isNull(node2.getAttribute("id"))) {node2.setAttribute("id", nodeID++);}
            edge = graph.addEdge(Integer.toString(++edgeID), templateEdge.getNode1(), templateEdge.getNode2(), templateEdge.isDirected());

            if (graphTemplate.isDisplayNodeAttribute()) {
                node1.setAttribute("ui.label", String.format("%s id:%s", templateEdge.getNode1(), node1.getAttribute("id")));
                node2.setAttribute("ui.label", String.format("%s id:%s", templateEdge.getNode2(), node2.getAttribute("id")));
            }

            edge.setAttribute("weight", templateEdge.getWeight());
            edge.setAttribute("ui.label", getEdgeAttribute(graphTemplate, templateEdge));
            System.out.println(templateEdge);
        }

        graphBuilds.add(graph);
        return graph;
    }

    private String getEdgeAttribute(GraphTemplate template, GraphEdge templateEdge) {
        return String.format("%s%s%s",
                (template.isDisplayEdgeAttribute()) ? templateEdge.getEdgeAttribute() : "",
                (template.isDisplayWeight()) ? " weight:" + templateEdge.getWeight() : "",
                (template.isDisplayEdgeID()) ? " id:" + edgeID : ""
        );
    }

    private GraphTemplate buildTemplate(URI graphURI, int properties) {

        GraphTemplate graphTemplate = new GraphTemplate(graphURI.getPath(), properties);

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
                            !matcher.group("weight").isEmpty() ? Integer.parseInt(matcher.group("weight")) : STANDARD_WEIGHT,
                            matcher.group("attribute")
                    );
                }
            }
            graphTemplate.setDirected();

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return graphTemplate;
    }
}
