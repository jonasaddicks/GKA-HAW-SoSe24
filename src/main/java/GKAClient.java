import aufgabe1.storage.GraphBuilder;
import aufgabe1.storage.GraphSandbox;
import aufgabe1.view.thread.ViewerThread;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;

import static aufgabe1.algs.BreadthFirstSearch.shortestPathBFS;

public class GKAClient {

    private static final Scanner PROMPT = new Scanner(System.in);
    private static final GraphSandbox SANDBOX = GraphSandbox.getInstance();
    private static final GraphBuilder BUILDER = GraphBuilder.getInstance();

    private static URI STYLESHEET;
    private static final String GRAPHS_SAVES = "graphs/%s.gka";

    private static Graph workingGraph;
    private static ViewerThread workingViewerThread;



    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        System.setProperty("org.graphstream.ui", "swing");
        STYLESHEET = ResourceLoadHelper.loadResource("css/graph.css");
        shell();
    }



    public static void shell() {
        String command;
        System.out.println("type \"quit\" to terminate");
        System.out.println("options: \"sandbox\", \"display\", \"algorithm\"");
        while (!(command = PROMPT.nextLine()).equals("quit")) {

            switch (command) {

                case "sandbox":
                    sandbox();
                    break;

                case "display":
                    display();
                    break;

                case "algorithm":
                    algorithm();
                    break;

//                case "close":
//                    close();
//                    break;

                default: System.out.printf("unknown command: \"%s\"%n", command);
            }
        }
        System.exit(0);
    }



    private static void sandbox() {
        try {
            SANDBOX.buildTemplateFromSandbox(STYLESHEET);
        } catch (IOException e) {
            System.err.printf("An Error occured: %s%n", e.getMessage());
        }
        System.exit(0);
    }

    private static void display() {
        try {
            System.out.print("file: ");
            String file = PROMPT.nextLine().trim();
            System.out.printf("property bits as decimal in the following order: displayNodeAttribute - displayWeight - displayEdgeAttribute - displayEdgeID%nproperties: ");
            int properties = Integer.parseInt(PROMPT.nextLine().trim());

            workingGraph = BUILDER.buildGraphFromFile(ResourceLoadHelper.loadResource(String.format(GRAPHS_SAVES, file)), STYLESHEET, properties);
//            if (Objects.nonNull(workingViewerThread)) {
//                workingViewerThread.notifyViewer();
//            }
            workingViewerThread = new ViewerThread(workingGraph);
        } catch (MalformedURLException  | URISyntaxException | NumberFormatException | NullPointerException e) {
            System.err.printf("An Error occured: %s%n", e.getMessage());
        }
    }

    private static void algorithm() {
        if (Objects.nonNull(workingGraph)) {
            System.out.print("algorithm: ");

            String algorithm = PROMPT.nextLine().trim();

            switch (algorithm) {

                case "bfs":
                    bfs();
                    break;

                default: System.out.printf("\"%s\" is no valid algorithm%n", algorithm);
            }

        } else {
            System.out.println("no graph to work with");
        }
    }

    private static void bfs() {

        System.out.print("Node s: ");
        Node node1 = workingGraph.getNode(PROMPT.nextLine().trim());
        System.out.print("Node t: ");
        Node node2 = workingGraph.getNode(PROMPT.nextLine().trim());

        if (Objects.nonNull(node1) && Objects.nonNull(node2)) {
            LinkedList<Node> path = shortestPathBFS(workingGraph, node1, node2);
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

    private static void close() {
        if (Objects.nonNull(workingViewerThread)) {workingViewerThread.notifyViewer();}
    }
}
