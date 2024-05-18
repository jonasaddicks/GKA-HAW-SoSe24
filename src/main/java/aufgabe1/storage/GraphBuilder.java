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
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraphBuilder {

    private static GraphBuilder INSTANCE;

    private final Pattern EDGE_PATTERN = Pattern.compile("(?<node1>[\\wäöü]+)(\\s*?(?<directed>->|--)\\s*?(?<node2>[\\wäöü]+))?(\\s*?(\\((?<attribute>[\\wäöü]*?)\\))?\\s*?(:\\s*?(?<weight>\\d*?)?\\s*?)?)?;");
    private final int STANDARD_WEIGHT = 0;
    private final List<Graph> graphBuilds = new ArrayList<>();
    private int edgeID;
    private int nodeID;



    private GraphBuilder() {}

    public static GraphBuilder getInstance() { //singleton
        if (INSTANCE == null) {
            INSTANCE = new GraphBuilder();
        }
        return INSTANCE;
    }



    public Graph buildGraphFromFile(URI graphURI, URI styleSheet) throws MalformedURLException {
        return buildGraphFromFile(graphURI, styleSheet, 0);
    }

    public Graph buildGraphFromFile(URI graphURI, URI styleSheet, int properties) throws MalformedURLException {
        GraphTemplate template = buildTemplate(graphURI, properties); //builds a template from the file first to represent the abstract structure of the graph
        return buildGraphFromTemplate(styleSheet, template);
    }

    public Graph buildGraphFromTemplate(URI styleSheet, GraphTemplate graphTemplate) throws MalformedURLException {
        HashMap<String, Integer> labelToId = new HashMap<>();
        nodeID = 0; //counter to be assigned as id to every new node
        edgeID = -1; //counter to be assigned as id to every new edge

        Graph graph = new MultiGraph(graphTemplate.getName(), false, false);
        graph.setAttribute("ui.stylesheet", String.format("url('%s')", styleSheet.toURL()));

        Node node1;
        Node node2;
        Edge edge;

        for (GraphEdge templateEdge : graphTemplate.getGraphEdges()) { //iterate over every single edge in the template
            if (Objects.nonNull(templateEdge.getNode2())) { //case: an edge between two nodes is to be added

                node1 = initNode(labelToId, graph, templateEdge.getNode1());
                node2 = initNode(labelToId, graph, templateEdge.getNode2());

                if (graphTemplate.isDisplayNodeAttribute()) {
                    node1.setAttribute("ui.label", String.format("%s id:%s", node1.getAttribute("nodeMarker"), node1.getId()));
                    node2.setAttribute("ui.label", String.format("%s id:%s", node2.getAttribute("nodeMarker"), node2.getId()));
                }

                edge = graph.addEdge(Integer.toString(++edgeID), node1, node2, templateEdge.isDirected());
                edge.setAttribute("weight", templateEdge.getWeight());
                edge.setAttribute("ui.label", getEdgeAttribute(graphTemplate, templateEdge));

            } else { //case: only one node without edge is to be added

                node1 = initNode(labelToId, graph, templateEdge.getNode1());
                if (graphTemplate.isDisplayNodeAttribute()) {
                    node1.setAttribute("ui.label", String.format("%s id:%s", node1.getAttribute("nodeMarker"), node1.getId()));
                }
            }
        }

        graphBuilds.add(graph);
        return graph;
    }

    private Node initNode(Map<String, Integer> labelToId, Graph graph, String marker){
        Node node;
        if (labelToId.containsKey(marker)) { //does node with the given marker already exists?
            node = graph.getNode(labelToId.get(marker)); //get existing node
        } else {
            node = graph.addNode(Integer.toString(nodeID)); //create new node with current id
            node.setAttribute("nodeMarker", marker); //set given marker
            labelToId.put(marker, nodeID); //mark node with given marker as existing
            nodeID++; //increment nodeID
        }
        return node;
    }

    private String getEdgeAttribute(GraphTemplate template, GraphEdge templateEdge) { //constructs the edges label according to the given properties
        return String.format("%s%s%s",
                (template.isDisplayEdgeAttribute()) ? templateEdge.getEdgeAttribute() : "",
                (template.isDisplayWeight()) ? " weight:" + templateEdge.getWeight() : "",
                (template.isDisplayEdgeID()) ? " id:" + edgeID : ""
        );
    }

    private GraphTemplate buildTemplate(URI graphURI, int properties) {

        GraphTemplate graphTemplate = new GraphTemplate(graphURI.getPath(), properties);

        try {
            FileInputStream saveFileInputStream = new FileInputStream(graphURI.getPath());
            InputStreamReader saveFileInputStreamReader = new InputStreamReader(saveFileInputStream, StandardCharsets.ISO_8859_1);
            BufferedReader saveFileBufferedReader = new BufferedReader(saveFileInputStreamReader); //read edges from file line by line

            String edgeLine;
            Matcher matcher;
            while ((edgeLine = saveFileBufferedReader.readLine()) != null) {
                matcher = EDGE_PATTERN.matcher(edgeLine); //apply pattern
                if (matcher.find()) {
                    graphTemplate.addEdge(
                            matcher.group("node1"),
                            matcher.group("node2"),
                            switch (matcher.group("directed")) {
                                case "--", "" -> false;
                                case "->" -> true;
                                case null -> false;
                                default -> throw new IllegalArgumentException(String.format("graph type '%s' not allowed", matcher.group("directed")));
                            },
                            Objects.nonNull(matcher.group("weight")) ? Integer.parseInt(matcher.group("weight")) : STANDARD_WEIGHT,
                            matcher.group("attribute")
                    );
                }
            }
            graphTemplate.setDirected(); //set directed property by checking first edge

        } catch (IOException | NumberFormatException e) {
            System.err.printf("An Error occured: %s%n", e.getMessage());
        }
        return graphTemplate;
    }
}
