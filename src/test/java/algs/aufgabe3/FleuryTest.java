package algs.aufgabe3;

import algs.GraphBuilder;
import aufgabe3.generator.RandomGraphEuler;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.stream.Stream;

import static aufgabe3.algs.Fleury.eulerCircleFleury;

public class FleuryTest {

    @Nested
    class RandomizedEulerCircuit {

        private static Stream<Arguments> randomOptions() {
            return Stream.of(
                    Arguments.of(1000, 2),
                    Arguments.of(1000, 5),
                    Arguments.of(2000, 2),
                    Arguments.of(2000, 5),
                    Arguments.of(4000, 2),
                    Arguments.of(4000, 5),
                    Arguments.of(8000, 2),
                    Arguments.of(8000, 5),
                    Arguments.of(16000, 2),
                    Arguments.of(16000, 5)
            );
        }

        @MethodSource("randomOptions")
        @ParameterizedTest
        void randomCircuit(int numberOfNodes, int avgDegree) throws MalformedURLException {
            Graph graph = randomGraph(numberOfNodes, avgDegree);

            ArrayList<Edge> eulerCircuit = eulerCircleFleury(graph);
            System.out.println(eulerCircuit);
            Assertions.assertTrue(EulerCircuitTestHelper.validEulerCircuit(eulerCircuit, graph));
        }

        private Graph randomGraph(int size, int avgDegree) throws MalformedURLException {
            Graph graph = RandomGraphEuler.generateEulerianGraph(size, avgDegree, "randomCircuit", aufgabe1.storage.GraphBuilder.getInstance(), null, 0).getGraph();
            return graph;
        }
    }



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

        ArrayList<Edge> eulerCircuit = eulerCircleFleury(graph);
        Assertions.assertTrue(EulerCircuitTestHelper.validEulerCircuit(eulerCircuit, graph));
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

        ArrayList<Edge> eulerCircuit = eulerCircleFleury(graph);
        System.out.println(eulerCircuit);
        Assertions.assertTrue(EulerCircuitTestHelper.validEulerCircuit(eulerCircuit, graph));
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

        ArrayList<Edge> eulerCircuit = eulerCircleFleury(graph);
        Assertions.assertTrue(EulerCircuitTestHelper.validEulerCircuit(eulerCircuit, graph));
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

        ArrayList<Edge> eulerCircuit = eulerCircleFleury(graph);
        Assertions.assertTrue(EulerCircuitTestHelper.validEulerCircuit(eulerCircuit, graph));
    }

    @Test
    public void singleNode() {
        GraphBuilder graphBuilder = GraphBuilder.builder("graph", false)
                .node1("A").next();
        Graph graph = graphBuilder.graph();

        ArrayList<Edge> eulerCircuit = eulerCircleFleury(graph);
        Assertions.assertTrue(EulerCircuitTestHelper.validEulerCircuit(eulerCircuit, graph));
    }

    @Test
    public void singleNodeLoop() {
        GraphBuilder graphBuilder = GraphBuilder.builder("graph", false)
                .node1("A").node2("A").next();
        Graph graph = graphBuilder.graph();

        ArrayList<Edge> eulerCircuit = eulerCircleFleury(graph);
        Assertions.assertTrue(EulerCircuitTestHelper.validEulerCircuit(eulerCircuit, graph));
    }



    //NEGATIVE
    @Test
    public void emptyGraph() {
        Graph graph = new GraphBuilder("graph", false).graph();

        ArrayList<Edge> eulerCircuit = eulerCircleFleury(graph);
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

        ArrayList<Edge> eulerCircuit = eulerCircleFleury(graph);
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

        ArrayList<Edge> eulerCircuit = eulerCircleFleury(graph);
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

        ArrayList<Edge> eulerCircuit = eulerCircleFleury(graph);
        Assertions.assertNull(eulerCircuit);
    }
}
