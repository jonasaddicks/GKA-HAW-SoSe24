package aufgabe1.algs;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class BreadthFirstSearch {

    private static Graph workingGraph;
    private static Node startNode;
    private static Node endNode;

    private static boolean tFound;
    private static boolean sFound;

    private static Queue<Node> nodeQueue;

    private static int[][] pathIDTable;
    private static Node[] pathNodeTable;
    private static LinkedList<Node> path;

    public static synchronized LinkedList<Node> shortestPathBFS(Graph graph, Node s, Node t) {
        if (s.equals(t)) { //start == goal?
            path = new LinkedList<>();
            path.addFirst(s);
            return path;
        }

        workingGraph = graph;
        startNode = s;
        endNode = t;

        initBFS(); //initialize variables
        bfs(); //apply breadth first search
        if (tFound) {
            pathBackTrack(); //backtrack and collect the path
            return path;
        } else {
            return null; //no path was found
        }
    }

    private static void initBFS() {
        tFound = false;
        sFound = false;

        nodeQueue = new LinkedList<>();
        nodeQueue.add(startNode);

        path = new LinkedList<>();

        pathNodeTable = new Node[workingGraph.getNodeCount()]; //index = nodeID, pointer to the best predecessor node
        pathIDTable = new int[workingGraph.getNodeCount()][2]; //index = nodeID, contains the length of the shortest path to each node and the predecessor nodes IDs
        for (int i = 0; i < workingGraph.getNodeCount(); i++) {
            pathIDTable[i][0] = -1; //-1 = unvisited
            pathIDTable[i][1] = -1; //-1 = no predecessor
        }

        int id1 = Integer.parseInt(startNode.getId());
        pathNodeTable[id1] = startNode;
        pathIDTable[id1][0] = 0; //length of path from start to s = 0
        pathIDTable[id1][1] = id1; //predecessor of start = start
    }

    private static void bfs() {
        while (!nodeQueue.isEmpty() && !tFound) { //as long as the queue is not empty, there are unvisited and unvisited and reachable nodes from start
            Node workingNode = nodeQueue.poll(); //workingNode = current iterations node to review

            workingNode.neighborNodes()
                    .filter(n -> n.hasEdgeFrom(workingNode)) //filter nodes n which are reachable from current workingNode
                    .distinct() //no duplicates - graphstreams '.neighborNodes()' may return a single node multiple times for each edge
                    .filter(n -> pathIDTable[Integer.parseInt(n.getId())][1] == -1) //filter unvisited nodes
                    .forEach(n -> {
                        nodeQueue.add(n); //queue up unvisited nodes
                        pathIDTable[Integer.parseInt(n.getId())][0] = pathIDTable[Integer.parseInt(workingNode.getId())][0] + 1; //successors path length = workingNodes path length + 1
                        pathIDTable[Integer.parseInt(n.getId())][1] = Integer.parseInt(workingNode.getId()); //successors predecessor = workingNode (ID)
                        pathNodeTable[Integer.parseInt(n.getId())] = workingNode; //successors predecessor = workingNode (ID)
                        if (n.equals(endNode)) {tFound = true;} //if t (goal) was found set the flag and terminate
                    });
        }
    }

    private static void pathBackTrack() {

        path.addFirst(endNode);
        for (int nodeID = Integer.parseInt(endNode.getId()); nodeID != pathIDTable[nodeID][1]; nodeID = pathIDTable[nodeID][1]) { //walk the discovered path backwards and save the used nodes
            path.addFirst(pathNodeTable[nodeID]);
        }
    }

    private static void pathBackTrackBFS() {
        Node predNode = endNode;
        while (!sFound) {
            path.addFirst(predNode); //add the next step to the path
            Node workingNode = predNode;

            predNode = workingNode.neighborNodes()
                    .filter(n -> n.hasEdgeToward(workingNode)) //filter nodes n which are reachable from current workingNode
                    .distinct() //no duplicates - graphstreams '.neighborNodes()' may return a single node multiple times for each edge
                    .filter(n -> pathIDTable[Integer.parseInt(n.getId())][0] == pathIDTable[Integer.parseInt(workingNode.getId())][0] - 1) //filter nodes that are one step closer to the start
                    .findFirst() //take first node matching the criteria (one step closer to the start)
                    .get();
            if (pathIDTable[Integer.parseInt(predNode.getId())][0] == 0) { //set flag if distance between the next step and start = 0
                sFound = true;
                path.addFirst(predNode);
            }
        }
    }
}
