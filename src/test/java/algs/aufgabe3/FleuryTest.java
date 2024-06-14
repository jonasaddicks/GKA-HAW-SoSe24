package algs.aufgabe3;

import algs.GraphBuilder;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static aufgabe3.algs.Fleury.eulerCircuitFleury;

public class FleuryTest {



    //POSITIVE
    @Test
    public void completeGraph() {
        GraphBuilder graphBuilder = GraphBuilder.builder("graph", false)
                .node1("A").node2("B").next()
                .node1("B").node2("C").next()
                .node1("C").node2("D").next()
                .node1("D").node2("E").next()
                .node1("E").node2("A").next()

                .node1("A").node2("C").next()
                .node1("C").node2("E").next()
                .node1("E").node2("B").next()
                .node1("B").node2("D").next()
                .node1("D").node2("A").next();
        Graph graph = graphBuilder.graph();

        ArrayList<Edge> eulerCircuit = eulerCircuitFleury(graph);
        Assertions.assertTrue(EulerCircuitTestHelper.isEulerCircuit(eulerCircuit, graph));
    }

    @Test
    public void eulerCircuit() {
        GraphBuilder graphBuilder = GraphBuilder.builder("graph", false)
                .node1("A").node2("B").next()
                .node1("B").node2("C").next()
                .node1("C").node2("D").next()
                .node1("D").node2("E").next()
                .node1("E").node2("F").next()
                .node1("F").node2("G").next()
                .node1("G").node2("H").next()
                .node1("H").node2("I").next()
                .node1("I").node2("D").next()
                .node1("D").node2("J").next()
                .node1("J").node2("G").next()
                .node1("G").node2("A").next();
        Graph graph = graphBuilder.graph();

        ArrayList<Edge> eulerCircuit = eulerCircuitFleury(graph);
        Assertions.assertTrue(EulerCircuitTestHelper.isEulerCircuit(eulerCircuit, graph));
    }

    @Test
    public void eulerCircuitLoops() {
        GraphBuilder graphBuilder = GraphBuilder.builder("graph", false)
                .node1("A").node2("B").next()
                .node1("B").node2("C").next()
                .node1("C").node2("D").next()
                .node1("D").node2("E").next()
                .node1("E").node2("F").next()
                .node1("F").node2("G").next()
                .node1("G").node2("H").next()
                .node1("H").node2("I").next()
                .node1("I").node2("D").next()
                .node1("D").node2("J").next()
                .node1("J").node2("G").next()
                .node1("G").node2("A").next()

                .node1("G").node2("G").next()
                .node1("E").node2("E").next()
                .node1("B").node2("B").next()
                .node1("J").node2("J").next()
                .node1("A").node2("A").next();
        Graph graph = graphBuilder.graph();

        ArrayList<Edge> eulerCircuit = eulerCircuitFleury(graph);
        Assertions.assertTrue(EulerCircuitTestHelper.isEulerCircuit(eulerCircuit, graph));
    }

    @Test
    public void multiGraphPos() {
        GraphBuilder graphBuilder = GraphBuilder.builder("graph", false)
                .node1("A").node2("B").next()
                .node1("B").node2("C").next()
                .node1("C").node2("D").next()
                .node1("G").node2("H").next()
                .node1("H").node2("I").next()
                .node1("I").node2("D").next()
                .node1("D").node2("J").next()
                .node1("J").node2("G").next()
                .node1("G").node2("A").next()

                .node1("G").node2("H").next()
                .node1("H").node2("I").next()
                .node1("I").node2("D").next();
        Graph graph = graphBuilder.graph();

        ArrayList<Edge> eulerCircuit = eulerCircuitFleury(graph);
        Assertions.assertTrue(EulerCircuitTestHelper.isEulerCircuit(eulerCircuit, graph));
    }

    @Test
    public void singleNode() {
        GraphBuilder graphBuilder = GraphBuilder.builder("graph", false)
                .node1("A").next();
        Graph graph = graphBuilder.graph();

        ArrayList<Edge> eulerCircuit = eulerCircuitFleury(graph);
        Assertions.assertTrue(EulerCircuitTestHelper.isEulerCircuit(eulerCircuit, graph));
    }

    @Test
    public void singleNodeLoop() {
        GraphBuilder graphBuilder = GraphBuilder.builder("graph", false)
                .node1("A").node2("A").next();
        Graph graph = graphBuilder.graph();

        ArrayList<Edge> eulerCircuit = eulerCircuitFleury(graph);
        Assertions.assertTrue(EulerCircuitTestHelper.isEulerCircuit(eulerCircuit, graph));
    }



    //NEGATIVE
    @Test
    public void emptyGraph() {
        Graph graph = new GraphBuilder("graph", false).graph();

        ArrayList<Edge> eulerCircuit = eulerCircuitFleury(graph);
        Assertions.assertNull(eulerCircuit);
    }

    @Test
    public void multiGraphNeg() {
        GraphBuilder graphBuilder = GraphBuilder.builder("graph", false)
                .node1("A").node2("B").next()
                .node1("B").node2("C").next()
                .node1("C").node2("D").next()
                .node1("D").node2("E").next()
                .node1("E").node2("F").next()
                .node1("F").node2("G").next()
                .node1("G").node2("H").next()
                .node1("H").node2("I").next()
                .node1("I").node2("D").next()
                .node1("D").node2("J").next()
                .node1("J").node2("G").next()
                .node1("G").node2("A").next()

                .node1("G").node2("H").next()
                .node1("H").node2("I").next()
                .node1("I").node2("D").next();
        Graph graph = graphBuilder.graph();

        ArrayList<Edge> eulerCircuit = eulerCircuitFleury(graph);
        Assertions.assertNull(eulerCircuit);
    }

    @Test
    public void noEulerCircuit() {
        GraphBuilder graphBuilder = GraphBuilder.builder("graph", false)
                .node1("A").node2("B").next()
                .node1("B").node2("C").next()
                .node1("C").node2("D").next()
                .node1("D").node2("A").next()

                .node1("A").node2("E").next();
        Graph graph = graphBuilder.graph();

        ArrayList<Edge> eulerCircuit = eulerCircuitFleury(graph);
        Assertions.assertNull(eulerCircuit);
    }

    @Test
    public void notConnected() {
        GraphBuilder graphBuilder = GraphBuilder.builder("graph", false)
                .node1("A").node2("B").next()
                .node1("B").node2("C").next()
                .node1("C").node2("D").next()
                .node1("D").node2("A").next()

                .node1("E").node2("F").next()
                .node1("F").node2("G").next()
                .node1("G").node2("H").next()
                .node1("H").node2("E").next();
        Graph graph = graphBuilder.graph();

        ArrayList<Edge> eulerCircuit = eulerCircuitFleury(graph);
        Assertions.assertNull(eulerCircuit);
    }
}
