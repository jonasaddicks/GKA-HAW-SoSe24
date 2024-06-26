
import aufgabe1.storage.GraphBuilder;
import aufgabe1.storage.GraphSandbox;
import aufgabe3.generator.RandomGraphEuler;
import org.graphstream.graph.Edge;
import util.view.thread.ViewerThread;
import aufgabe2.generator.RandomGraphConnected;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static aufgabe1.algs.BreadthFirstSearch.shortestPathBFS;
import static aufgabe2.algs.Kruskal.minimalSpanningTreeKruskal;
import static aufgabe2.algs.Prim.minimalSpanningTreePrim;
import static aufgabe3.algs.Fleury.eulerCircleFleury;
import static aufgabe3.algs.Hierholzer.eulerCircuitHierholzer;
import static util.weightSum.graphWeightSum;

public class GKAClient {

    private static final Scanner PROMPT = new Scanner(System.in);
    private static final GraphSandbox SANDBOX = GraphSandbox.getInstance();
    private static final GraphBuilder BUILDER = GraphBuilder.getInstance();

    private static URI STYLESHEET;
    private static final String GRAPHS_SAVES = "graphs/%s.gka";

    private static GraphBuilder.GraphInstance workingGraph;
    private static ViewerThread workingViewerThread;



    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        System.setProperty("org.graphstream.ui", "swing");
        STYLESHEET = ResourceLoadHelper.loadResource("css/graph.css");
        shell();
    }



    public static void shell() {
        String command;
        System.out.printf("type \"quit\" to terminate%n");
        System.out.printf("options: \"sandbox\", \"display\", \"generate\", \"getCurrent\", \"setCurrent\", \"algorithm\"%n%n");
        while (!(command = PROMPT.nextLine()).equals("quit")) {

            switch (command) {

                case "sandbox":
                    sandbox();
                    break;

                case "display":
                    display();
                    break;

                case "generate":
                    generate();
                    break;

                case "getCurrent":
                    getCurrent();
                    break;

                case "setCurrent":
                    setCurrent();
                    break;

                case "algorithm":
                    algorithm();
                    break;

//                GraphStream issue - closing the display (viewer) disrupts/freezes the main thread
//                case "close":
//                    close();
//                    break;

                default: System.out.printf("unknown command: \"%s\"%n", command);
            }
            System.out.printf("%n");
        }
        System.exit(0);
    }



    private static void sandbox() {
        try {
            SANDBOX.buildTemplateFromSandbox(STYLESHEET);
        } catch (IOException e) {
            System.err.printf("An Error occurred: %s%n", e.getMessage());
        }
        System.exit(0);
    }

    private static void display() {
        try {
            System.out.print("File: ");
            String file = PROMPT.nextLine().trim();
            System.out.printf("property bits as decimal in the following order: displayNodeAttribute - displayWeight - displayEdgeAttribute - displayEdgeID%nProperties: ");
            int properties = Integer.parseInt(PROMPT.nextLine().trim());

            workingGraph = BUILDER.buildGraphFromFile(ResourceLoadHelper.loadResource(String.format(GRAPHS_SAVES, file)), STYLESHEET, properties);
//            if (Objects.nonNull(workingViewerThread)) {
//                workingViewerThread.notifyViewer();
//            }
            workingViewerThread = new ViewerThread(workingGraph.getGraph());
        } catch (MalformedURLException  | URISyntaxException | NumberFormatException | NullPointerException e) {
            System.err.printf("An Error occurred: %s%n", e.getMessage());
        }
    }



    private static void generate() {
        System.out.printf("options: \"saveConnected\", \"displayConnected\", \"saveEuler\", \"displayEuler\"%nalgorithm: ");

        String generate = PROMPT.nextLine().trim();

        switch (generate) {

            case "saveConnected":
                generateSaveConnected();
                break;

            case "displayConnected":
                generateDisplayConnected();
                break;

            case "saveEuler":
                generateSaveEuler();
                break;

            case "displayEuler":
                generateDisplayEuler();
                break;

            default: System.out.printf("\"%s\" is no valid generator%n", generate);
        }
    }

    private static void generateSaveConnected() {
        String numberOfNodesString, numberOfEdgesString, randomGraphName;

        System.out.print("Number of nodes: ");
        numberOfNodesString = PROMPT.nextLine().trim();
        System.out.print("Number of edges: ");
        numberOfEdgesString = PROMPT.nextLine().trim();
        System.out.print("Graphs name: ");
        randomGraphName = PROMPT.nextLine().trim();

        try {
            RandomGraphConnected.generateConnectedTemplate(Integer.parseInt(numberOfNodesString), Integer.parseInt(numberOfEdgesString), randomGraphName, 0).saveTemplate();
            System.exit(0);
        } catch (IllegalArgumentException | IOException e) {
            System.err.printf("An Error occurred: %s%n", e.getMessage());
        }
    }

    private static void generateDisplayConnected() {
        String numberOfNodesString, numberOfEdgesString, randomGraphName, properties;

        System.out.print("Number of nodes: ");
        numberOfNodesString = PROMPT.nextLine().trim();
        System.out.print("Number of edges: ");
        numberOfEdgesString = PROMPT.nextLine().trim();
        System.out.print("Graphs name: ");
        randomGraphName = PROMPT.nextLine().trim();
        System.out.print("Properties: ");
        properties = PROMPT.nextLine().trim();

        try {
            workingGraph = RandomGraphConnected.generateConnectedGraph(Integer.parseInt(numberOfNodesString), Integer.parseInt(numberOfEdgesString), randomGraphName, GraphBuilder.getInstance(), STYLESHEET, Integer.parseInt(properties));
            workingViewerThread = new ViewerThread(workingGraph.getGraph());
        } catch (IllegalArgumentException | MalformedURLException e) {
            System.err.printf("An Error occurred: %s%n", e.getMessage());
        }
    }

    private static void generateSaveEuler() {
        String numberOfNodes, avgDegree, randomGraphName;

        System.out.print("Number of nodes: ");
        numberOfNodes = PROMPT.nextLine().trim();
        System.out.print("Average Degree(int): ");
        avgDegree = PROMPT.nextLine().trim();
        System.out.print("Graphs name: ");
        randomGraphName = PROMPT.nextLine().trim();

        try {
            RandomGraphEuler.generateEulerTemplate(Integer.parseInt(numberOfNodes), Integer.parseInt(avgDegree), randomGraphName, 0).saveTemplate();
            System.exit(0);
        } catch (IllegalArgumentException | IOException e) {
            System.err.printf("An Error occurred: %s%n", e.getMessage());
        }
    }

    private static void generateDisplayEuler() {
        String numberOfNodes, avgDegree, randomGraphName, properties;

        System.out.print("Number of nodes: ");
        numberOfNodes = PROMPT.nextLine().trim();
        System.out.print("Average Degree(int): ");
        avgDegree = PROMPT.nextLine().trim();
        System.out.print("Graphs name: ");
        randomGraphName = PROMPT.nextLine().trim();
        System.out.print("Properties: ");
        properties = PROMPT.nextLine().trim();

        try {
            workingGraph = RandomGraphEuler.generateEulerianGraph(Integer.parseInt(numberOfNodes), Integer.parseInt(avgDegree), randomGraphName, GraphBuilder.getInstance(), STYLESHEET, Integer.parseInt(properties));
            workingViewerThread = new ViewerThread(workingGraph.getGraph());
        } catch (IllegalArgumentException | MalformedURLException e) {
            System.err.printf("An Error occurred: %s%n", e.getMessage());
        }
    }



    private static void getCurrent() {
        System.out.println(Objects.nonNull(workingGraph) ? workingGraph.getGraph().getId() : "no selected graph");
    }

    private static void setCurrent() {
        try {
            System.out.print("file: ");
            String file = PROMPT.nextLine().trim();

            workingGraph = BUILDER.buildGraphFromFile(ResourceLoadHelper.loadResource(String.format(GRAPHS_SAVES, file)), STYLESHEET, 0);
        } catch (MalformedURLException  | URISyntaxException | NumberFormatException | NullPointerException e) {
            System.err.printf("An Error occured: %s%n", e.getMessage());
        }
    }



    private static void algorithm() {
        if (Objects.nonNull(workingGraph)) {
            System.out.printf("options: \"bfs\", \"kruskal\", \"prim\", \"weightSum\", \"fleury\", \"hierholzer\"%nalgorithm: ");

            String algorithm = PROMPT.nextLine().trim();

            switch (algorithm) {

                case "bfs":
                    bfs();
                    break;

                case "kruskal":
                    kruskal();
                    break;

                case "prim":
                    prim();
                    break;

                case "weightSum":
                    weightSum();
                    break;

                case "fleury":
                    fleury();
                    break;

                case "hierholzer":
                    hierholzer();
                    break;

                default: System.out.printf("\"%s\" is no valid algorithm%n", algorithm);
            }

        } else {
            System.out.println("no graph to work with");
        }
    }

    private static void bfs() {
        String nodeMarker;
        Graph graph = workingGraph.getGraph();
        HashMap<String, Integer> labelToId = workingGraph.getLabelToId();
        Node node1, node2;

        System.out.print("Node s: ");
        if (labelToId.containsKey(nodeMarker = PROMPT.nextLine().trim())) {
            node1 = graph.getNode(labelToId.get(nodeMarker));
        } else {
            try {
                node1 = graph.getNode(nodeMarker);
            } catch (NumberFormatException e) {
                node1 = null;
                System.err.printf("An Error occured: %s%n", e.getMessage());
            }
        }

        System.out.print("Node t: ");
        if (labelToId.containsKey(nodeMarker = PROMPT.nextLine().trim())) {
            node2 = graph.getNode(labelToId.get(nodeMarker));
        } else {
            try {
                node2 = graph.getNode(nodeMarker);
            } catch (NumberFormatException e) {
                node2 = null;
                System.err.printf("An Error occured: %s%n", e.getMessage());
            }
        }

        if (Objects.nonNull(node1) && Objects.nonNull(node2)) {

            LinkedList<Node> path = shortestPathBFS(graph, node1, node2);
            System.out.println(Objects.isNull(path) ? "No path found" : path);

            LinkedList<Node> copyPath = (Objects.nonNull(path)) ? new LinkedList<>(path) : new LinkedList<>();

            if (copyPath.size() > 1) {
                Node predNode = copyPath.poll();
                predNode.setAttribute("ui.class", "start");
                Node currentNode;

                for (currentNode = copyPath.poll(); !copyPath.isEmpty(); currentNode = copyPath.poll()) {
                    predNode.getEdgeBetween(currentNode).setAttribute("ui.class", "path");
                    currentNode.setAttribute("ui.class", "path");
                    predNode = currentNode;
                }
                predNode.getEdgeBetween(currentNode).setAttribute("ui.class", "path");
                currentNode.setAttribute("ui.class", "goal");

                System.out.print("press enter to continue");
                PROMPT.nextLine();

                copyPath = new LinkedList<>(path);

                predNode = copyPath.poll();
                predNode.setAttribute("ui.class", "");

                for (currentNode = copyPath.poll(); !copyPath.isEmpty(); currentNode = copyPath.poll()) {
                    predNode.getEdgeBetween(currentNode).setAttribute("ui.class", "");
                    currentNode.setAttribute("ui.class", (Object) "");
                    predNode = currentNode;
                }
                predNode.getEdgeBetween(currentNode).setAttribute("ui.class", "");
                currentNode.setAttribute("ui.class", "");
            }
        } else {
            System.out.printf("Error - Node1: %s, Node2: %s%n", node1, node2);
        }
    }

    private static void kruskal() {
        System.out.print("display minimal spanning tree? (y/n): ");
        String displayMinimalSpanningTree = PROMPT.nextLine();

        try {
            Graph minimalSpanningTree = minimalSpanningTreeKruskal(workingGraph.getGraph());
            minimalSpanningTree.setAttribute("ui.stylesheet", String.format("url('%s')", STYLESHEET.toURL()));
            workingGraph.setGraph(minimalSpanningTree);
            if (displayMinimalSpanningTree.equals("y")) {
                workingViewerThread = new ViewerThread(workingGraph.getGraph());
            }
        } catch (MalformedURLException | IllegalArgumentException e) {
            System.err.printf("An Error occurred: %s%n", e.getMessage());
        }
    }

    private static void prim() {
        System.out.print("display minimal spanning tree? (y/n): ");
        String displayMinimalSpanningTree = PROMPT.nextLine();

        try {
            Graph minimalSpanningTree = minimalSpanningTreePrim(workingGraph.getGraph());
            minimalSpanningTree.setAttribute("ui.stylesheet", String.format("url('%s')", STYLESHEET.toURL()));
            workingGraph.setGraph(minimalSpanningTree);
            if (displayMinimalSpanningTree.equals("y")) {
                workingViewerThread = new ViewerThread(workingGraph.getGraph());
            }
        } catch (MalformedURLException | IllegalArgumentException e) {
            System.err.printf("An Error occurred: %s%n", e.getMessage());
        }
    }

    private static void weightSum() {
        System.out.printf("weightSum of current graph: %d", graphWeightSum(workingGraph.getGraph()));
    }

    private static void fleury() {
        ArrayList<Edge> eulerCircuit = eulerCircleFleury(workingGraph.getGraph());
        if (Objects.nonNull(eulerCircuit)) {
            System.out.println(eulerCircuit);
        } else {
            System.out.println("No eulerian circuit has been found");
        }
    }

    private static void hierholzer() {
        ArrayList<Edge> eulerCircuit = eulerCircuitHierholzer(workingGraph.getGraph());
        if (Objects.nonNull(eulerCircuit)) {
            System.out.println(eulerCircuit);
        } else {
            System.out.println("No eulerian circuit has been found");
        }
    }



    private static void close() {
        if (Objects.nonNull(workingViewerThread)) {workingViewerThread.notifyViewer();}
    }
}
