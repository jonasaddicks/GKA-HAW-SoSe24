package aufgabe1.storage;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.List;

public class GraphForm {

    private String name;
    private boolean directed;
    private List<GraphEdge> graphEdges;

    public GraphForm(String name, boolean directed) {
        this.name = name;
        this.directed = directed;
        this.graphEdges = new ArrayList<>();
    }

    private class GraphEdge {

        private Node node1;
        private Node node2;
        private String edgeAttribute;
        private int weight;
    }
}
