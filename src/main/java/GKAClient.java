import aufgabe1.storage.GraphBuilder;
import org.graphstream.graph.Graph;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class GKAClient {

    public static void main(String[] args) throws URISyntaxException, MalformedURLException {
        System.setProperty("org.graphstream.ui", "swing");

        GraphBuilder graphBuilder = GraphBuilder.getInstance();
        Graph graph = graphBuilder.buildGraph(ResourceLoadHelper.loadResource("graphs/graph01.gka"), ResourceLoadHelper.loadResource("css/graph.css"), 15);
        graph.display();
    }
}
