package aufgabe1.storage;

public class GraphEdge {

    private String node1;
    private String node2;
    private boolean directed;
    private String edgeAttribute;
    private Integer weight;

    public GraphEdge(String node1, String node2, boolean directed, Integer weight, String edgeAttribute) {
        this.node1 = node1;
        this.node2 = node2;
        this.directed = directed;
        this.weight = weight;
        this.edgeAttribute = edgeAttribute;
    }

    public String getNode1() {
        return node1;
    }

    public String getNode2() {
        return node2;
    }

    public boolean isDirected() {
        return directed;
    }

    public String getEdgeAttribute() {
        return edgeAttribute;
    }

    public Integer getWeight() {
        return weight;
    }
}
