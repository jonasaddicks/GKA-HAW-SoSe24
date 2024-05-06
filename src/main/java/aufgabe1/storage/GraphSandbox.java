package aufgabe1.storage;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraphReader {

    private static GraphReader INSTANCE;

    private Pattern EDGE_PATTERN = Pattern.compile("(?<node1>[\\wäöü]+)\\s*?(?<directed>-|>)\\s*?(?<node2>[\\wäöü]+)\\s*?(?<attribute>[\\wäöü]*?)\\s*?:?\\s*?(?<weight>\\d*?);");
    private final Scanner scanner = new Scanner(System.in);



    private GraphReader() {}

    public static GraphReader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GraphReader();
        }
        return INSTANCE;
    }



    public void readGraph(URI styleSheet) throws MalformedURLException {
        Graph graph = new MultiGraph("sandbox", false, true);
        graph.setAttribute("ui.stylesheet", String.format("url('%s')", styleSheet.toURL()));
        graph.display();

        System.out.printf("name: ");
        GraphTemplate template = new GraphTemplate(scanner.nextLine());

        System.out.printf("format: \"<Node1> [-/>] <Node2> <attribute> : <weight>;\" type \"quit\" to finish%n");
        String edgeLine;
        Matcher matcher;
        while(!(edgeLine = scanner.nextLine()).equals("quit")) {
            matcher = EDGE_PATTERN.matcher(edgeLine);
            if(matcher.find()) {

                String node1 = matcher.group("node1");
                String node2 = matcher.group("node2");
                boolean directed = switch (matcher.group("directed")) {
                    case "-" -> false;
                    case ">" -> true;
                    default -> throw new IllegalArgumentException(String.format("graph type '%s' not allowed", matcher.group("directed")));
                };
                int weight;
                String attribute;

                graph.addEdge(
                        UUID.randomUUID().toString(),
                        matcher.group("node1"),
                        matcher.group("node2"),
                        switch (matcher.group("directed")) {
                        case "-" -> false;
                        case ">" -> true;
                        default -> throw new IllegalArgumentException(String.format("graph type '%s' not allowed", matcher.group("directed")));
                });

                template.addEdge(
                        matcher.group("node1"),
                        matcher.group("node2"),
                        switch (matcher.group("directed")) {
                            case "-" -> false;
                            case ">" -> true;
                            default -> throw new IllegalArgumentException(String.format("graph type '%s' not allowed", matcher.group("directed")));
                        },
                        !matcher.group("weight").isEmpty() ? Integer.parseInt(matcher.group("weight")) : 1,
                        matcher.group("attribute"));
            } else {
                System.out.printf("wrong format: \"%s\" invalid%n", edgeLine);
            }
        }

        template.setDirected();
    }
}
