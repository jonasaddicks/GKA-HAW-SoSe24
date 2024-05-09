package algs;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import java.util.Objects;

public class GraphBuilder {

    protected final Graph graph;
    private final boolean directed;
    private int nodeID;
    private int edgeID;

    public GraphBuilder(String name, boolean directed) {
        this.graph = new MultiGraph(name, false, false);
        this.directed = directed;
        nodeID = 0;
        edgeID = 0;
    }

    public static GraphBuilder builder(String name, boolean directed) {
        return new GraphBuilder(name, directed);
    }

    public GraphLineBuilder node1(String node1) {
        return new GraphLineBuilder(this, graph, directed, node1);
    }

    public Graph graph() {
        return graph;
    }



    public static class GraphLineBuilder {
        private final GraphBuilder graphLinesBuilder;
        private final Graph graph;
        private final boolean directed;
        private final String node1;
        private String node2;
        private String edge;
        private int weight = 1;

        public GraphLineBuilder(GraphBuilder graphLinesBuilder, Graph graph, boolean directed, String node1) {
            this.graphLinesBuilder = graphLinesBuilder;
            this.graph = graph;
            this.directed = directed;
            this.node1 = node1;
        }

        public GraphLineBuilder node2(String id) {
            node2 = id;
            return this;
        }

        public GraphLineBuilder edge(String edge) {
            this.edge = edge;
            return this;
        }

        public GraphLineBuilder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public GraphBuilder next() {
            final Node node1 = graph.addNode(this.node1);
            if (Objects.isNull(node1.getAttribute("id"))) {node1.setAttribute("id", graphLinesBuilder.getNodeID());}

            if (Objects.nonNull(this.node2)) {
                final Node node2 = graph.addNode(this.node2);
                if (Objects.isNull(node2.getAttribute("id"))) {node2.setAttribute("id", graphLinesBuilder.getNodeID());}

                final Edge edge = graph.addEdge(Integer.toString(graphLinesBuilder.getEdgeID()), node1, node2, directed);
                edge.setAttribute("weight", this.weight == 0 ? this.weight : 1);
                if (Objects.nonNull(this.edge)) edge.setAttribute("attribute", this.edge);
            }
            return graphLinesBuilder;
        }
    }

    public int getNodeID() {
        return nodeID++;
    }

    public int getEdgeID() {
        return edgeID++;
    }
}