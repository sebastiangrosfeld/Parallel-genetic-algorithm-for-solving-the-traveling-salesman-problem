package pl.edu.pw.ee;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.BDDMockito.given;

class SimpleCrossoverTest {

    @Test
    void givenTwoIndividuals_whenCrossover_thenReturnsOffspring() {
        // given
        final var firstParent = createFirstParent();
        final var secondParent = createSecondParent();
        final var random = Mockito.mock(Random.class);
        final var simpleCrossover = new SimpleCrossover(random);
        given(random.nextInt(firstParent.getGenome().size()))
                .willReturn(2);

        // when
        final var offspring = simpleCrossover.crossover(firstParent, secondParent);

        // then
        final var expectedOffspringGenes = List.of(
                new Node(2, 2),
                new Node(1, 1),
                new Node(0, 0),
                new Node(3, 3),
                new Node(4, 4)
        );
        final var actualOffspringGenes = offspring.getGenome();
        assertIterableEquals(expectedOffspringGenes, actualOffspringGenes);
    }

    private Individual createFirstParent() {
        final var firstNode = new Node(0, 0);
        final var secondNode = new Node(1, 1);
        final var thirdNode = new Node(2, 2);
        final var fourthNode = new Node(3, 3);
        final var fifthNode = new Node(4, 4);
        final var genes = List.of(firstNode, secondNode, thirdNode, fourthNode, fifthNode);
        return new Individual(genes);
    }

    private Individual createSecondParent() {
        final var firstNode = new Node(4, 4);
        final var secondNode = new Node(3, 3);
        final var thirdNode = new Node(2, 2);
        final var fourthNode = new Node(1, 1);
        final var fifthNode = new Node(0, 0);
        final var genes = List.of(firstNode, secondNode, thirdNode, fourthNode, fifthNode);
        return new Individual(genes);
    }

}