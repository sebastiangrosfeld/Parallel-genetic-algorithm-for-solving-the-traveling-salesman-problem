package pl.edu.pw.ee;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DistanceFitnessFunctionTest {

    @Test
    void givenIndividual_whenCalculateFitness_thenReturnsTotalDistanceAsFitness() {
        // given
        final var firstNode = new Node(0, 0);
        final var secondNode = new Node(3, 4);
        final var thirdNode = new Node(-6, -8);
        final var genes = List.of(firstNode, secondNode, thirdNode);
        final var individual = new Individual(genes);
        final var fitnessFunction = new DistanceFitnessFunction();

        // when
        final var fitness = fitnessFunction.calculateFitness(individual);

        // then
        final var expectedFitness = 30.0;
        assertEquals(expectedFitness, fitness);
    }

    @Test
    void givenIndividualWithOneNode_whenCalculateFitness_thenReturnsZero() {
        // given
        final var node = new Node(5, 10);
        final var genes = List.of(node);
        final var individual = new Individual(genes);
        final var fitnessFunction = new DistanceFitnessFunction();

        // when
        final var fitness = fitnessFunction.calculateFitness(individual);

        // then
        final var expectedFitness = 0.0;
        assertEquals(expectedFitness, fitness);
    }

}