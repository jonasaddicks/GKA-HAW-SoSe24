package aufgabe1.algs;

import org.apache.commons.math3.util.Pair;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstSearch {

    private static int iLength, id1;
    private static boolean tFound;
    private static Queue<Node> nodeQueue;
    private static int[][] pathTable;



    public static synchronized void shortestPathBFS(Graph graph, Node s, Node t) {
        iLength = 0;
        tFound = false;

        nodeQueue = new LinkedList<>();
        nodeQueue.add(s);

        pathTable = new int[graph.getNodeCount()][2];
        for (int i = 0; i < graph.getNodeCount(); i++) {
            pathTable[i][0] = -1;
            pathTable[i][1] = -1;
        }

        id1 = (Integer)s.getAttribute("id");

        pathTable[id1][0] = 0;
        pathTable[id1][1] = id1;

        while (!nodeQueue.isEmpty() && !tFound) {
            Node workingNode = nodeQueue.poll();

            workingNode.neighborNodes()
                    .filter(n -> pathTable[(Integer)n.getAttribute("id")][1] == -1)
                    .forEach(n -> {
                        nodeQueue.add(n);
                        pathTable[(Integer)n.getAttribute("id")][0] = iLength + 1;
                        pathTable[(Integer)n.getAttribute("id")][1] = (Integer)workingNode.getAttribute("id");
                        if (n.equals(t)) {tFound = true;}
                    });
            iLength++;
        }

        for (int nodeID = (Integer)t.getAttribute("id"); nodeID != id1; nodeID = pathTable[nodeID][1]) {
            //TODO
        }
    }
}
