package aufgabe1.storage;

import aufgabe1.view.thread.ViewerThread;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

import java.io.IOException;
import java.net.URI;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraphSandbox {

    private static GraphSandbox INSTANCE;

    private static final Pattern EDGE_PATTERN = Pattern.compile("(?<node1>[\\wäöü]+)\\s*?(?<directed>[->])\\s*?(?<node2>[\\wäöü]+)\\s*?(?<attribute>[\\wäöü]*?)\\s*?:?\\s*?(?<weight>\\d*?);");
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final int STANDARD_WEIGHT = 0;



    private GraphSandbox() {}

    public static GraphSandbox getInstance() { //singleton
        if (INSTANCE == null) {
            INSTANCE = new GraphSandbox();
        }
        return INSTANCE;
    }



    public void buildTemplateFromSandbox(URI styleSheet) throws IOException {
        Graph graph = new MultiGraph("sandbox", false, true);
        graph.setAttribute("ui.stylesheet", String.format("url('%s')", styleSheet.toURL()));

        ViewerThread viewerThread = new ViewerThread(graph); //open sandbox window

        System.out.print("name: ");
        GraphTemplate template = new GraphTemplate(SCANNER.nextLine());

        System.out.printf("format: \"<Node1> [-/>] <Node2> <attribute> : <weight>;\" type \"exit\" to quit%n");
        String edgeLine;
        Matcher matcher;
        while(!(edgeLine = SCANNER.nextLine()).equals("exit")) { //add a new edge for every line (that matches the pattern)
            matcher = EDGE_PATTERN.matcher(edgeLine);
            if(matcher.find()) {

                //TODO adding single nodes to the graph not possible
                String node1 = matcher.group("node1");
                String node2 = matcher.group("node2");
                boolean directed = switch (matcher.group("directed")) {
                    case "-" -> false;
                    case ">" -> true;
                    default -> throw new IllegalArgumentException(String.format("graph type '%s' not allowed", matcher.group("directed")));
                };
                int weight = !matcher.group("weight").isEmpty() ? Integer.parseInt(matcher.group("weight")) : STANDARD_WEIGHT;
                String attribute = matcher.group("attribute");

                graph.addEdge(UUID.randomUUID().toString(), node1, node2, directed); //add edge to be displayed in the window
                template.addEdge(node1, node2, directed, weight, attribute); //add edge to the abstract representation of the graph to be saved later
            } else {
                System.out.printf("wrong format: \"%s\" invalid%n", edgeLine);
            }
        }
        viewerThread.notifyViewer(); //close sandbox window
        template.setDirected(); //set directed property by checking first edge
        template.saveTemplate(); //save template to file
    }
}
