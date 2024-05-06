package aufgabe1.storage;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraphSandbox {

    private static GraphSandbox INSTANCE;

    private final Pattern EDGE_PATTERN = Pattern.compile("(?<node1>[\\wäöü]+)\\s*?(?<directed>[->])\\s*?(?<node2>[\\wäöü]+)\\s*?(?<attribute>[\\wäöü]*?)\\s*?:?\\s*?(?<weight>\\d*?);");
    private final Scanner scanner = new Scanner(System.in);



    private GraphSandbox() {}

    public static GraphSandbox getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GraphSandbox();
        }
        return INSTANCE;
    }



    public void buildTemplateFromSandbox(URI styleSheet) throws MalformedURLException {
        Graph graph = new MultiGraph("sandbox", false, true);
        graph.setAttribute("ui.stylesheet", String.format("url('%s')", styleSheet.toURL()));

        System.out.print("name: ");
        GraphTemplate template = new GraphTemplate(scanner.nextLine());

        System.out.printf("format: \"<Node1> [-/>] <Node2> <attribute> : <weight>;\" type \"exit\" to quit%n");
        String edgeLine;
        Matcher matcher;
        while(!(edgeLine = scanner.nextLine()).equals("exit")) {
            matcher = EDGE_PATTERN.matcher(edgeLine);
            if(matcher.find()) {

                String node1 = matcher.group("node1");
                String node2 = matcher.group("node2");
                boolean directed = switch (matcher.group("directed")) {
                    case "-" -> false;
                    case ">" -> true;
                    default -> throw new IllegalArgumentException(String.format("graph type '%s' not allowed", matcher.group("directed")));
                };
                int weight = !matcher.group("weight").isEmpty() ? Integer.parseInt(matcher.group("weight")) : 1;
                String attribute = matcher.group("attribute");

                graph.addEdge(UUID.randomUUID().toString(), node1, node2, directed);
                template.addEdge(node1, node2, directed, weight, attribute);
            } else {
                System.out.printf("wrong format: \"%s\" invalid%n", edgeLine);
            }
        }
        template.setDirected();
        template.saveTemplate();
    }
}
