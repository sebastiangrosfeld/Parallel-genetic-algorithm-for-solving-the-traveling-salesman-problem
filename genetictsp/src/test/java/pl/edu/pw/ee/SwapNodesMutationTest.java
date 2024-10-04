package pl.edu.pw.ee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SwapNodesMutationTest {

    @Test
    void givenIndividual_whenMutate_thenReturnsIndividualWithSwappedTwoNodes() {
        // given
        final var firstNode = new Node(0, 0);
        final var secondNode = new Node(3, 4);
        final var thirdNode = new Node(-6, -8);
        final var genes = List.of(firstNode, secondNode, thirdNode);
        final var individual = new Individual(genes);
        final var random = Mockito.mock(Random.class);
        final var mutation = new SwapNodesMutation(random);
        given(random.nextInt(genes.size()))
                .willReturn(0)
                .willReturn(2);

        // when
        final var mutatedIndividual = mutation.mutate(individual);

        // then
        final var mutatedGenes = mutatedIndividual.getGenome();
        final var expectedMutatedGenes = List.of(thirdNode, secondNode, firstNode);
        assertIterableEquals(expectedMutatedGenes, mutatedGenes);
    }

    @Test
    void givenIndividualWithOneNode_whenMutate_thenReturnsTheSameIndividual() {
        // given
        final var node = new Node(5, 10);
        final var genes = List.of(node);
        final var individual = new Individual(genes);
        final var random = Mockito.mock(Random.class);
        final var mutation = new SwapNodesMutation(random);

        // when
        final var mutatedIndividual = mutation.mutate(individual);

        // then
        final var mutatedGenes = mutatedIndividual.getGenome();
        final var expectedMutatedGenes = List.of(node);
        assertIterableEquals(expectedMutatedGenes, mutatedGenes);
    }

}