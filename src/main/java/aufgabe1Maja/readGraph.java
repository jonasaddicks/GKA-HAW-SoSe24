package aufgabe1Maja;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class readGraph {
    private Pattern graphRegex = Pattern.compile("(?<node1>\\w*?)(\\s*?-(?<directed>[->])\\s*?(?<node2>\\w*?)(?>\\s*?\\((?<edgeName>\\w*?)\\))?\\s*?(?>:\\s*?(?<weight>\\d))?)?;");

    public void read(){
        Scanner scanner;
        String path = "resources/graph01.gka";
        scanner = new Scanner(path);
        boolean directed = false;
        String node2Name = "";
        String edgeName = "";
        int weight = 0;


        MultiGraph graph = new MultiGraph("test");

        //Read graph data from file
        while (scanner.hasNextLine()){
            String line = scanner.next();
            Matcher matcher = graphRegex.matcher(line);
            if (matcher.find()){
                String node1Name = matcher.group("node1Name");
                if (matcher.group("directed").contains(">")) directed = true;
                node2Name = matcher.group("node2Name");
                edgeName = matcher.group("edgeName");
                weight = Integer.parseInt(matcher.group("weight"));


                //Create Graph TODO

                Node node1 = graph.addNode(node1Name);
                Node node2 = graph.addNode(node2Name);
                Edge edge = graph.addEdge(edgeName, graph.getNode(node1Name), graph.getNode(node2Name));
                edge.setAttribute("weight",weight);
            };



        }
    }

    //Sollte geeignete Datenstruktur zurückgeben (shortest path und Anzahl der Kanten) TODO
    public void bfsAlgorithm(){

}
}
