import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Scanner;

public class GKAClient {

    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "swing");

        Graph graph = new SingleGraph("Tutorial 1");

        graph.display();

        Scanner scanner = new Scanner(System.in);
        String nextLine;
        while (scanner.hasNext() | nextLine == null || !nextLine.equals("quit")) {
            nextLine = scanner.next();

            //graph.addEdge()
        }
    }
}
