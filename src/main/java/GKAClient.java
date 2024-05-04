import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GKAClient {

    private static final Pattern EDGE_PATTERN = Pattern.compile("\\s*(?<identity>\\S+)\\s+(?<node1>\\S+)\\s+(?<node2>\\S+)");

    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "swing");

        Graph graph = new SingleGraph("TestGraph");
        graph.setStrict(false);
        graph.setAutoCreate(true);

        graph.display();

        Scanner scanner = new Scanner(System.in);
        String nextLine = "continue";

        while (!nextLine.equals("quit") && scanner.hasNextLine()) {
            nextLine = scanner.nextLine();
            final Matcher matcher = EDGE_PATTERN.matcher(nextLine);

            if (matcher.find()) {
                String identity = matcher.group("identity");
                String node1 = matcher.group("node1");
                String node2 = matcher.group("node2");

                System.out.printf("attributes: %n\tid: %s, %n\tnode1: %s, %n\tnode2: %s%nfrom line: \"%s\"%n", identity, node1, node2, nextLine);
                graph.addEdge(identity, node1, node2);
            }
        }
        System.exit(0);
    }
}
