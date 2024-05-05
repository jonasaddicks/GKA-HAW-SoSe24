package aufgabe1.storage;

import java.util.ArrayList;
import java.util.List;

public class GraphTemplate {

    private String name;
    private boolean directed;
    private List<GraphEdge> graphEdges;

    public GraphTemplate(String name) {
        this(name, false);
    }

    public GraphTemplate(String name, boolean directed) {
        this.name = name;
        this.directed = directed;
        this.graphEdges = new ArrayList<>();
    }

    public void addEdge(String node1, String node2, boolean directed, Integer weight, String edgeAttribute) {
        graphEdges.add(new GraphEdge(node1, node2, directed, weight, edgeAttribute));
    }

    public void setDirected() {
        if (graphEdges.size() > 0) {
            this.directed = graphEdges.getFirst().isDirected();
        }
    }

    public String getName(){
        return this.name;
    }

    public List<GraphEdge> getGraphEdges() {
        return this.graphEdges;
    }



    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        graphEdges.stream().forEach(s -> stringBuilder.append(String.format("from %s to %s - directed: %s - weight: %s - %s%n", s.getNode1(), s.getNode2(), s.isDirected(), s.getWeight(), s.getEdgeAttribute())));
        return stringBuilder.toString();
    }
}
