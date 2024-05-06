package aufgabe1.storage;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import javax.swing.plaf.basic.BasicDesktopIconUI;
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
    private int edgeID;



    private GraphBuilder() {
        graphBuilds = new ArrayList<>();
        edgeID = -1;
    }

    public static GraphBuilder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GraphBuilder();
        }
        return INSTANCE;
    }

    public Graph buildGraph(URI graphURI, URI styleSheet) throws MalformedURLException {
        return buildGraph(graphURI, styleSheet, 0);
    }

    public Graph buildGraph(URI graphURI, URI styleSheet, int properties) throws MalformedURLException {
        GraphTemplate template = buildTemplate(graphURI, properties);

        Graph graph = new MultiGraph(template.getName(), false, true);
        graph.setAttribute("ui.stylesheet", String.format("url('%s')", styleSheet.toURL()));

        Node node1;
        Node node2;
        Edge edge;

        for (GraphEdge templateEdge : template.getGraphEdges()) {
            node1 = graph.addNode(templateEdge.getNode1());
            node2 = graph.addNode(templateEdge.getNode2());
            edge = graph.addEdge(Integer.toString(++edgeID), node1, node2, templateEdge.isDirected());

            if (template.isDisplayNodeAttribute()) {
                node1.setAttribute("ui.label", templateEdge.getNode1());
                node2.setAttribute("ui.label", templateEdge.getNode2());
            }

            edge.setAttribute("ui.label", buildEdgeAttribute(template, templateEdge));
            System.out.println(templateEdge);
            //TODO
        }

        graphBuilds.add(graph);
        return graph;
    }

    private String buildEdgeAttribute(GraphTemplate template, GraphEdge templateEdge) {
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
                            (!matcher.group("weight").isEmpty()) ? Integer.parseInt(matcher.group("weight")) : 1,
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
