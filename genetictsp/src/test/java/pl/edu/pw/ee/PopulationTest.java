package pl.edu.pw.ee;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class PopulationTest {

    @Test
    void givenNodesAndSize_whenCreatingPopulation_thenPopulationIsInitialized() {
        // given
        final var nodes = List.of(
                new Node(0, 0),
                new Node(1, 1),
                new Node(2, 2)
        );
        final var size = 10;
        final var random = new Random();

        // when
        final var population = new Population(nodes, size, random);

        // then
        assertEquals(size, population.getMembers().size());
        for (final var individual : population.getMembers()) {
            assertNodesAreShuffled(nodes, individual.getGenome());
        }
    }

    private void assertNodesAreShuffled(List<Node> nodes, List<Node> shuffledNodes) {
        assertEquals(nodes.size(), shuffledNodes.size());
        for (var node : nodes) {
            assertEquals(1, shuffledNodes.stream()
                    .filter(currentNode -> currentNode.equals(node))
                    .count());
        }
        assertNotSame(nodes, shuffledNodes);
    }

}