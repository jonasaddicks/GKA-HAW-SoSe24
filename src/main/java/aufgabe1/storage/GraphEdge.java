package aufgabe1.storage;

public class GraphEdge {

    final private String node1;
    final private String node2;
    final private boolean directed;
    final private String edgeAttribute;
    final private Integer weight;



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



    @Override
    public String toString() {
        return String.format("from %s to %s - directed: %s - weight: %s - %s", node1, node2, directed, weight, edgeAttribute);
    }
}
