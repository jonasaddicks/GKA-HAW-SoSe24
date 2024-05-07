import aufgabe1.storage.GraphBuilder;
import aufgabe1.storage.GraphSandbox;
import aufgabe1.view.thread.ViewerThread;
import org.graphstream.graph.Graph;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Scanner;

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
        System.out.println("choose between \"sandbox\" and \"display\"");
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

                default: System.out.printf("unknown command: \"%s\"", command);
            }
        }
        System.exit(0);
    }



    private static void sandbox() {
        try {
            SANDBOX.buildTemplateFromSandbox(STYLESHEET);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private static void display() {
        try {
            System.out.print("file: ");
            String file = PROMPT.nextLine();
            System.out.printf("property bits as decimal in the following order: displayNodeAttribute - displayWeight - displayEdgeAttribute - displayEdgeID%nproperties: ");
            int properties = Integer.parseInt(PROMPT.nextLine());

            workingGraph = BUILDER.buildGraphFromFile(ResourceLoadHelper.loadResource(String.format(GRAPHS_SAVES, file)), STYLESHEET, properties);
//            if (Objects.nonNull(workingViewerThread)) {
//                workingViewerThread.notifyViewer();
//            }
            workingViewerThread = new ViewerThread(workingGraph);
        } catch (MalformedURLException  | URISyntaxException | NumberFormatException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private static void algorithm() {
        if (Objects.nonNull(workingGraph)) {
            System.out.print("algorithm: ");

            String algorithm = PROMPT.nextLine();

            switch (algorithm) {

                case "bfs":
                    break;

                default: System.out.printf("\"%s\" is no valid algorithm");
            }

        } else {
            System.out.println("no graph to work with");
        }
    }

    private static void close() {
        if (Objects.nonNull(workingViewerThread)) {workingViewerThread.notifyViewer();}
    }
}
