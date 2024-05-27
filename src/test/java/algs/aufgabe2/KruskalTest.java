package algs.aufgabe2;

import org.graphstream.algorithm.Kruskal;
import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.Stream;

import static aufgabe2.algs.Kruskal.minimalSpanningTreeKruskal;
import static aufgabe2.algs.weightSum.graphWeightSum;

public class KruskalTest {

    @Nested
    class Randomized {

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
                    Arguments.of(1024000, 10000000),
                    Arguments.of(2048000, 10000000)
            );
        }

        @MethodSource("randomOptions")
        @ParameterizedTest
        void randomTree(int size, int maxWeight) {
            Graph graph = randomGraph(size, maxWeight);

            int actual = graphWeightSum(minimalSpanningTreeKruskal(graph));
            int expected = computeMinimalSpanningTree(graph);

            Assertions.assertEquals(actual, expected);
        }

        private int computeMinimalSpanningTree(Graph graph) {
            Kruskal kruskal = new Kruskal("isInTree", "inTree", "notInTree");
            kruskal.init(graph);
            kruskal.compute();

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
}
