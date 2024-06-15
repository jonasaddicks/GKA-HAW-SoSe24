package algs.aufgabe2;

import org.graphstream.graph.Graph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static aufgabe2.algs.Kruskal.minimalSpanningTreeKruskal;
import static aufgabe2.algs.Prim.minimalSpanningTreePrim;
import static util.weightSum.graphWeightSum;
import static aufgabe2.generator.RandomGraphGeneratorConnected.generateConnectedGraph;


public class Runtimeanalysis {
    @Nested
    class RuntimeCompareAnalysis {

        private static Stream<Arguments> randomOptions() {
            return Stream.of(
                    Arguments.of(1000, 200000),
                    Arguments.of(2000, 400000),
                    Arguments.of(4000, 800000),
                    Arguments.of(8000, 1600000),
                    Arguments.of(16000, 3200000),
                    Arguments.of(32000, 6400000)
                    //Arguments.of(64000, 12800000),
                    //Arguments.of(128000, 25600000),
                    //Arguments.of(256000, 51200000)
                    //Arguments.of(512000, 10240000),
                    //Arguments.of(1024000, 2048000)
                    //Arguments.of(2048000, 6912000) - may throw OutOfMemoryError
            );
        }

        @MethodSource("randomOptions")
        @ParameterizedTest
        void randomTree(int nodes, int edges) {
            Graph graph = randomGraph(nodes, edges);

            int actual = graphWeightSum(minimalSpanningTreeKruskal(graph));
            int expected = graphWeightSum(minimalSpanningTreePrim(graph));

            Assertions.assertEquals(expected,actual);
        }


        private Graph randomGraph(int nodes, int edges) {
            return generateConnectedGraph(nodes, edges, "testGraph");
        }
    }
}
