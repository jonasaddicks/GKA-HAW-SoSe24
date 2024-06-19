package aufgabe3.algs;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.net.URI;
import java.util.*;

import org.graphstream.graph.implementations.MultiGraph;
import util.DisjointSetsComponents;
import static aufgabe3.algs.GraphValidator.isEulerian;

public class Fleury {

    private static boolean checkEuler(Graph graph) {
        HashSet<Node> visited = new HashSet<>();
        //check degree
        //Begin at any arbitrary node of the graph G.
        // Proceed from that node using either depth-first or breadth-first search,
        // counting all nodes reached. Once the graph has been entirely traversed,
        // if the number of nodes counted is equal to the number of nodes of G,
        // the graph is connected; otherwise it is disconnected.
        for (Node node: graph) {
            if (node.getDegree() % 2 != 0) {
                return false;
            }
            node.neighborNodes().forEach(visited::add);
        }
        return visited.size() == graph.getNodeCount();
    }

    public static ArrayList<Edge> eulerCircleFleury(Graph originalGraph) throws IllegalArgumentException {
        if (!isEulerian(originalGraph)) return null;


        ArrayList<Edge> eulerCircle = new ArrayList<>();
        int edgeNum = originalGraph.getEdgeCount();

        if (originalGraph.getNodeCount() == 0) return null;
        if (originalGraph.getEdgeCount() == 0) return eulerCircle;

        Graph graph = cloneGraph(originalGraph);
        Node v0 = graph.getNode(getRandomIndex(graph.getNodeCount()));


        while (v0.getOutDegree() > 0){
            Edge e0 = v0.getEdge(getRandomIndex(v0.getOutDegree()));

            if (!isBridge(e0, v0, graph)){
                graph.removeEdge(e0.getId());
                //Wenn Nodes ohne Verbindung zum Graphen zurückbleiben müssen sie aus dem Graphen entfernt werden
                // damit die Überprüfung auf eine Brücke nicht falsch auswertet.
                if (v0.getDegree()==0) graph.removeNode(v0.getId());


                eulerCircle.add(e0);
                v0 = e0.getOpposite(v0);
            }
        }

        if (eulerCircle.size()==edgeNum) return eulerCircle;

        return null;
    }

    private static boolean isBridge(Edge e0,Node v0, Graph graph) {
        graph.removeEdge(e0.getId());
        //Wenn Nodes ohne Verbindung zum Graphen zurückbleiben müssen sie aus dem Graphen entfernt werden
        // damit die Überprüfung auf eine Brücke nicht falsch auswertet.
        if (v0.getDegree()==0) graph.removeNode(v0.getId());
        DisjointSetsComponents<Node> disjointNodes = new DisjointSetsComponents<>(graph.getNodeCount());
        graph.nodes().forEach(disjointNodes::add);

        graph.edges().forEach(e -> disjointNodes.union(e.getNode0(), e.getNode1()));
        graph.addEdge(e0.getId(),e0.getNode0(),e0.getNode1());
        return disjointNodes.getComponents() > 1;
    }

    private static int getRandomIndex(int range) {
        if (range == 0) return 1;
        else {
            Random rand = new Random();
            return rand.nextInt(range);
        }
    }
    private static Graph cloneGraph(Graph graph) {
        MultiGraph graphClone = new MultiGraph(String.format("%s_copy", graph.getId()), false, true);
        graph.edges().forEach(e -> graphClone.addEdge(e.getId(), e.getNode0().getId(), e.getNode1().getId()));
        return graphClone;
    }

    /**
     * @return  -ArrayList containing all edges in order
     *          -empty ArrayList if the graph is eulerian even though it has no edges
     *          -null if no circuit has been found
     */
}
