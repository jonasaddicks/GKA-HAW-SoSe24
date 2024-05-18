package algs;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import java.util.HashMap;
import java.util.Objects;

public class GraphBuilder {

    protected final Graph graph;
    private final boolean directed;
    private final HashMap<String, Integer> labelToId;
    private int nodeID;
    private int edgeID;

    public GraphBuilder(String name, boolean directed) {
        this.graph = new MultiGraph(name, false, false);
        this.directed = directed;
        this.labelToId = new HashMap<>();
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

            Node node1;
            if (graphLinesBuilder.getLabelToId().containsKey(this.node1)) {
                node1 = graph.getNode(graphLinesBuilder.getLabelToId().get(this.node1));
            } else {
                node1 = graph.addNode(Integer.toString(graphLinesBuilder.getNodeID()));
                node1.setAttribute("nodeMarker", this.node1);
                graphLinesBuilder.getLabelToId().put(this.node1, Integer.valueOf(node1.getId()));
            }

            if (Objects.nonNull(this.node2)) {

                Node node2;
                if (graphLinesBuilder.getLabelToId().containsKey(this.node2)) {
                    node2 = graph.getNode(graphLinesBuilder.getLabelToId().get(this.node2));
                } else {
                    node2 = graph.addNode(Integer.toString(graphLinesBuilder.getNodeID()));
                    node2.setAttribute("nodeMarker", this.node2);
                    graphLinesBuilder.getLabelToId().put(this.node2, Integer.valueOf(node2.getId()));
                }

                final Edge edge = graph.addEdge(Integer.toString(graphLinesBuilder.getEdgeID()), node1, node2, directed);
                edge.setAttribute("weight", this.weight == 0 ? this.weight : 1);
                if (Objects.nonNull(this.edge)) edge.setAttribute("attribute", this.edge);
            }
            return graphLinesBuilder;
        }
    }

    public HashMap<String, Integer> getLabelToId() {
        return labelToId;
    }

    public int getNodeID() {
        return nodeID++;
    }

    public int getEdgeID() {
        return edgeID++;
    }
}