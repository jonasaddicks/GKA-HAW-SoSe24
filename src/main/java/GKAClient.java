import aufgabe1.storage.GraphBuilder;
import aufgabe1.storage.GraphSandbox;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class GKAClient {

    public static void main(String[] args) throws URISyntaxException, MalformedURLException, InterruptedException {
        System.setProperty("org.graphstream.ui", "swing");

        GraphSandbox graphReader = GraphSandbox.getInstance();
        graphReader.buildTemplateFromSandbox(ResourceLoadHelper.loadResource("css/graph.css"));

//        GraphBuilder graphBuilder = GraphBuilder.getInstance();
//        Graph graph = graphBuilder.buildGraphFromFile(ResourceLoadHelper.loadResource("graphs/graph01.gka"), ResourceLoadHelper.loadResource("css/graph.css"), 15);
//        graph.display();
    }
}
