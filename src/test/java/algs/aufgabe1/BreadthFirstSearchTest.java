package algs.aufgabe1;

import algs.GraphBuilder;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.MultiGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Stream;

import static aufgabe1.algs.BreadthFirstSearch.shortestPathBFS;

public class BreadthFirstSearchTest {

    @Nested
    class Randomized {

        private static Stream<Arguments> randomOptions() {
            return Stream.of(
                    Arguments.of(100, 5, true, true),
                    Arguments.of(100, 5, true, false),
                    Arguments.of(100, 5, false, true),
                    Arguments.of(100, 5, false, false),
                    Arguments.of(1000, 5, true, true),
                    Arguments.of(1000, 5, true, false),
                    Arguments.of(1000, 5, false, true),
                    Arguments.of(1000, 5, false, false),
                    Arguments.of(1000, 2, true, true),
                    Arguments.of(1000, 2, true, false),
                    Arguments.of(1000, 2, false, true),
                    Arguments.of(1000, 2, false, false)
            );
        }

        @MethodSource("randomOptions")
        //@ParameterizedTest
        //TODO INTERNAL REPRESENTATION OF IDS (ATTRIBUTE("nodeID")) NOT COMPATIBLE WITH GRAPHSTREAM ('randomGraph' and 'dijkstra')
        void randomPath(int size, int avgDegree, boolean remove, boolean directed) {
            Graph graph = randomGraph(size, avgDegree, remove, directed);
            LinkedList<Node> actual = shortestPathBFS(graph, graph.getNode(0), graph.getNode(graph.getNodeCount() - 1));
            actual = Objects.nonNull(actual) ? actual : new LinkedList<>();
            LinkedList<Node> expected = pathToLinkedList(computeBestPath(graph.getNode(0), graph.getNode(graph.getNodeCount() - 1)));

            System.out.printf("actual: %s%n", actual);
            System.out.printf("expect: %s%n", expected);

            //Da der ungewichtete Dijkstra zum Vergleich genutzt wird können sich verschiedene kürzeste Pfade ergeben, weswegen lediglich auf die Länge der Pfade geprüft wird
            Assertions.assertEquals(actual.size(), expected.size());
        }

        private LinkedList<Node> pathToLinkedList(Path path) {
            return new LinkedList<>(path.getNodePath());
        }

        private Path computeBestPath(Node start, Node end) {
            Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.NODE, "result", null);
            dijkstra.init(start.getGraph());
            dijkstra.setSource(start);
            dijkstra.compute();
            return dijkstra.getPath(end);
        }

        private Graph randomGraph(int size, int averageDegree, boolean allowRemove, boolean directed) {
            Graph graph = new MultiGraph("graph");
            RandomGenerator generator = new RandomGenerator(averageDegree, allowRemove, directed, null, null);
            generator.addSink(graph);
            generator.begin();
            for (int i = 0; i < size; i++) {
                generator.nextEvents();
            }
            generator.end();
            return graph;
        }
    }



    @Test
    public void longerDirectedPath() {
        final Graph graph = GraphBuilder.builder("graph", true)
                .node1("A").node2("B").next()
                .node1("B").node2("C").next()
                .node1("C").node2("D").next()
                .node1("D").node2("E").next()
                .node1("E").node2("F").next()
                .node1("F").node2("A").next()
                .graph();

        LinkedList<Node> actual = shortestPathBFS(graph, graph.getNode("A"), graph.getNode("E"));
        LinkedList<Node> expected = new LinkedList<>(Arrays.asList(
                graph.getNode("A"),
                graph.getNode("B"),
                graph.getNode("C"),
                graph.getNode("D"),
                graph.getNode("E")
                ));
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void loopsAndMultipleEdgesDirected() {
        final Graph graph = GraphBuilder.builder("graph", true)
                .node1("T").node2("T").next()
                .node1("A").node2("A").next()
                .node1("B").node2("B").next()
                .node1("S").node2("S").next()
                .node1("A").node2("T").next()
                .node1("T").node2("A").next()
                .node1("B").node2("S").next()
                .node1("S").node2("B").next()
                .node1("T").node2("S").next()
                .node1("T").node2("B").next()
                .node1("B").node2("A").next()
                .graph();

        LinkedList<Node> actual = shortestPathBFS(graph, graph.getNode("S"), graph.getNode("T"));
        LinkedList<Node> expected = new LinkedList<>(Arrays.asList(
                graph.getNode("S"),
                graph.getNode("B"),
                graph.getNode("A"),
                graph.getNode("T")
        ));
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void MultipleEdgesUndirected() {
        final Graph graph = GraphBuilder.builder("graph", false)
                .node1("A").node2("B").next()
                .node1("B").node2("C").next()
                .node1("C").node2("D").next()
                .node1("D").node2("E").next()
                .node1("A").node2("F").next()
                .node1("F").node2("G").next()
                .node1("G").node2("H").next()
                .node1("H").node2("E").next()
                .graph();

        LinkedList<Node> actual = shortestPathBFS(graph, graph.getNode("A"), graph.getNode("E"));
        LinkedList<Node> expected = new LinkedList<>(Arrays.asList(
                graph.getNode("A"),
                graph.getNode("B"),
                graph.getNode("C"),
                graph.getNode("D"),
                graph.getNode("E")
        ));
        Assertions.assertEquals(actual.size(), expected.size());
    }

    @Test
    public void noPathDirected() {
        final Graph graph = GraphBuilder.builder("graph", true)
                .node1("A").node2("B").next()
                .node1("B").node2("C").next()
                .node1("C").node2("D").next()
                .graph();

        LinkedList<Node> actual = shortestPathBFS(graph, graph.getNode("C"), graph.getNode("A"));
        Assertions.assertNull(actual);
    }

    @Test
    public void noPathUndirected() {
        final Graph graph = GraphBuilder.builder("graph", false)
                .node1("A").node2("B").next()
                .node1("B").node2("C").next()
                .node1("D").node2("E").next()
                .graph();

        LinkedList<Node> actual = shortestPathBFS(graph, graph.getNode("A"), graph.getNode("E"));
        Assertions.assertNull(actual);
    }
}
