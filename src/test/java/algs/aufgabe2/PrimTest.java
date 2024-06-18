package algs.aufgabe2;

import algs.GraphBuilder;
import org.graphstream.algorithm.Prim;
import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.Random;
import java.util.stream.Stream;

import static aufgabe2.algs.Prim.minimalSpanningTreePrim;
import static util.weightSum.graphWeightSum;
import static aufgabe2.generator.RandomGraphConnected.generateConnectedGraph;

public class PrimTest {

    @Nested
    class RandomizedDorogovtsev {

        private static Stream<Arguments> randomOptions() {
            return Stream.of(
                    Arguments.of(1000, 100),
                    Arguments.of(2000, 100),
                    Arguments.of(4000, 1000),
                    Arguments.of(8000, 1000),
                    Arguments.of(16000, 10000),
                    Arguments.of(32000, 10000),
                    Arguments.of(64000, 100000),
                    Arguments.of(128000, 100000),
                    Arguments.of(256000, 1000000),
                    Arguments.of(512000, 1000000),
                    Arguments.of(1024000, 10000000)
                    //Arguments.of(2048000, 10000000) - may throw OutOfMemoryError
            );
        }

        @MethodSource("randomOptions")
        @ParameterizedTest
        void randomTree(int size, int maxWeight) {
            Graph graph = randomGraph(size, maxWeight);

            int actual = graphWeightSum(minimalSpanningTreePrim(graph));
            int expected = computeMinimalSpanningTree(graph);

            Assertions.assertEquals(actual, expected);
        }

        private int computeMinimalSpanningTree(Graph graph) {
            Prim prim = new Prim("isInTree", "inTree", "notInTree");
            prim.init(graph);
            prim.compute();

            return weightSumGraphStream(graph);
        }

        private int weightSumGraphStream(Graph graph) {
            return graph.edges()
                    .filter(e -> e.getAttribute("isInTree").equals("inTree"))
                    .map(e -> (int) e.getAttribute("weight"))
                    .reduce(0, Integer::sum);
        }

        private Graph randomGraph(int size, int maxWeight) {
            Random rand = new Random();
            Graph graph = new MultiGraph("graph");
            DorogovtsevMendesGenerator generator = new DorogovtsevMendesGenerator();
            generator.addEdgeAttribute("weight", e -> rand.nextInt(maxWeight));
            generator.addSink(graph);
            generator.begin();
            for (int i = 0; i < size; i++) {
                generator.nextEvents();
            }
            generator.end();
            return graph;
        }
    }

    @Nested
    class RandomizedOwnGenerator {

        private static Stream<Arguments> randomOptions() {
            return Stream.of(
                    Arguments.of(1000, 200000),
                    Arguments.of(2000, 4000),
                    Arguments.of(4000, 8000),
                    Arguments.of(8000, 16000),
                    Arguments.of(16000, 32000),
                    Arguments.of(32000, 64000),
                    Arguments.of(64000, 128000),
                    Arguments.of(128000, 256000),
                    Arguments.of(256000, 512000),
                    Arguments.of(512000, 1024000),
                    Arguments.of(1024000, 2048000)
                    //Arguments.of(2048000, 6912000) - may throw OutOfMemoryError
            );
        }

        @MethodSource("randomOptions")
        @ParameterizedTest
        void randomTree(int nodes, int edges) throws IOException {
            Graph graph = randomGraph(nodes, edges);

            int actual = graphWeightSum(minimalSpanningTreePrim(graph));
            int expected = computeMinimalSpanningTree(graph);

            Assertions.assertEquals(expected, actual);
        }

        private int computeMinimalSpanningTree(Graph graph) {
            Prim prim = new Prim("isInTree", "inTree", "notInTree");
            prim.init(graph);
            prim.compute();

            return weightSumGraphStream(graph);
        }

        private int weightSumGraphStream(Graph graph) {
            return graph.edges()
                    .filter(e -> e.getAttribute("isInTree").equals("inTree"))
                    .map(e -> (int) e.getAttribute("weight"))
                    .reduce(0, Integer::sum);
        }

        private Graph randomGraph(int nodes, int edges) throws IOException {
            return generateConnectedGraph(nodes, edges, "testGraph", aufgabe1.storage.GraphBuilder.getInstance(), null, 0).getGraph();
        }
    }

    @Test
    public void notConnected() {
        GraphBuilder graphBuilder = GraphBuilder.builder("graph", false)
                .node1("A").node2("B").next()
                .node1("B").node2("C").next()
                .node1("C").node2("A").next()
                .node1("D").node2("E").next()
                .node1("E").node2("F").next()
                .node1("F").node2("D").next();
        Graph graph = graphBuilder.graph();

        Assertions.assertThrows(IllegalArgumentException.class, () -> minimalSpanningTreePrim(graph));
    }
}
