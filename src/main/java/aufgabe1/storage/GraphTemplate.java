package aufgabe1.storage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphTemplate {

    private final String name;
    private boolean directed;
    private final List<GraphEdge> graphEdges = new ArrayList<>();
    private final String fileType = "gka";
    private final String saveDirectory = "src\\main\\resources\\graphs\\";

    private final int propertyInt;
    private final boolean displayNodeAttribute;
    private final boolean displayWeight;
    private final boolean displayEdgeAttribute;
    private final boolean displayEdgeID;



    public GraphTemplate(String name) {
        this(name, 0);
    }

    public GraphTemplate(String name, int properties) {
        this.name = name;

        //properties are defined by the last 4 bits of the 'properties' integer
        this.propertyInt = properties;
        this.displayNodeAttribute = ((properties >> 3) & 1) ==1;        //bit 3 for displayNodeAttribute property (2^3)
        this.displayWeight = ((properties >> 2) & 1) == 1;              //bit 2 for displayWeight property (2^2)
        this.displayEdgeAttribute = ((properties >> 1) & 1) == 1;       //bit 1 for displayEdgeAttribute (2^1)
        this.displayEdgeID = (properties & 1) == 1;                     //bit 0 for displayEdgeID (2^0)
    }

    public void saveTemplate() throws IOException {
        File newGraphFile = new File(String.format("%s%s.%s", saveDirectory, name, fileType));
        if (!newGraphFile.createNewFile()) {
            System.out.printf("error: file \"%s.%s\" already exists%n", name, fileType);
        }
    }



    public void setDirected() {
        if (!graphEdges.isEmpty()) {
            this.directed = graphEdges.getFirst().isDirected();
        }
    }

    public void addEdge(String node1, String node2, boolean directed, Integer weight, String edgeAttribute) {
        graphEdges.add(new GraphEdge(node1, node2, directed, weight, edgeAttribute));
    }

    public String getName(){
        return this.name;
    }

    public boolean isDisplayNodeAttribute() {
        return displayNodeAttribute;
    }

    public boolean isDisplayWeight() {
        return displayWeight;
    }

    public boolean isDisplayEdgeAttribute() {
        return displayEdgeAttribute;
    }

    public boolean isDisplayEdgeID() {
        return displayEdgeID;
    }

    public List<GraphEdge> getGraphEdges() {
        return this.graphEdges;
    }



    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("name: %ndirected: %nproperties: %s%n%n", name, directed, propertyInt));
        graphEdges.forEach(s -> stringBuilder.append(String.format("%s%n", s)));
        return stringBuilder.toString();
    }
}
