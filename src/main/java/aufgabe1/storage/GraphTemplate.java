package aufgabe1.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GraphTemplate {

    private final String name;
    private boolean directed;
    private final List<GraphEdge> graphEdges = new ArrayList<>();
    private static final String FILE_TYPE = "gka";
    private static final String SAVE_DIRECTORY = "src\\main\\resources\\graphs\\";

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
        File newGraphFile = new File(String.format("%s%s.%s", SAVE_DIRECTORY, name, FILE_TYPE));
        if (!newGraphFile.createNewFile()) {
            System.out.printf("error: file \"%s.%s\" already exists%n", name, FILE_TYPE);
        } else {
            BufferedWriter saver = new BufferedWriter(new FileWriter(newGraphFile));
            for (GraphEdge graphEdge : graphEdges) {
                saver.write(String.format("%s %s %s%s%s;%n",
                        graphEdge.getNode1(),
                        (graphEdge.isDirected()) ? "->" : "--",
                        graphEdge.getNode2(),
                        graphEdge.getEdgeAttribute().isEmpty() || Objects.isNull(graphEdge.getEdgeAttribute()) ? "" : String.format(" (%s)", graphEdge.getEdgeAttribute()),
                        graphEdge.getWeight() != 0 ? String.format(" : %d", graphEdge.getWeight()) : ""
                ));
            }
            saver.close();
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
