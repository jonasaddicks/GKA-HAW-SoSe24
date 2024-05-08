package aufgabe1.algs;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class BreadthFirstSearch {

    private static Graph workingGraph;
    private static Node startNode;
    private static Node endNode;

    private static int iLength;
    private static boolean tFound;

    private static Queue<Node> nodeQueue;

    private static int[][] pathIDTable;
    private static Node[] pathNodeTable;
    private static LinkedList<Node> path;




    public static synchronized LinkedList<Node> shortestPathBFS(Graph graph, Node s, Node t) {
        if (s.equals(t)) {
            path = new LinkedList<>();
            path.addFirst(s);
            return path;
        }

        workingGraph = graph;
        startNode = s;
        endNode = t;

        initBFS();
        bfs();
        if (tFound) {
            pathBackTrack();
            return path;
        } else {
            return null;
        }
    }

    private static void initBFS() {
        iLength = 0;
        tFound = false;

        nodeQueue = new LinkedList<>();
        nodeQueue.add(startNode);

        path = new LinkedList<>();

        pathNodeTable = new Node[workingGraph.getNodeCount()];
        pathIDTable = new int[workingGraph.getNodeCount()][2];
        for (int i = 0; i < workingGraph.getNodeCount(); i++) {
            pathIDTable[i][0] = -1;
            pathIDTable[i][1] = -1;
        }

        int id1 = (Integer)startNode.getAttribute("id");
        pathNodeTable[id1] = startNode;
        pathIDTable[id1][0] = 0;
        pathIDTable[id1][1] = id1;
    }

    private static void bfs() {
        while (!nodeQueue.isEmpty() && !tFound) {
            Node workingNode = nodeQueue.poll();

            workingNode.neighborNodes()
                    .filter(n -> (!workingNode.getEdgeBetween(n).isDirected()) || (workingNode.getEdgeBetween(n).isDirected() && Objects.equals((Node) workingNode.getEdgeBetween(n).getAttribute("goalNode"), n)))
                    .distinct()
                    .filter(n -> pathIDTable[(Integer)n.getAttribute("id")][1] == -1)
                    .forEach(n -> {
                        nodeQueue.add(n);
                        pathIDTable[(Integer)n.getAttribute("id")][0] = iLength + 1;
                        pathIDTable[(Integer)n.getAttribute("id")][1] = (Integer)workingNode.getAttribute("id");
                        pathNodeTable[(Integer)n.getAttribute("id")] = workingNode;
                        if (n.equals(endNode)) {tFound = true;}
                    });
            iLength++;
        }
    }

    private static void pathBackTrack() {

        path.addFirst(endNode);
        for (int nodeID = (Integer)endNode.getAttribute("id"); nodeID != pathIDTable[nodeID][1]; nodeID = pathIDTable[nodeID][1]) {
            path.addFirst(pathNodeTable[nodeID]);
        }
    }
}
