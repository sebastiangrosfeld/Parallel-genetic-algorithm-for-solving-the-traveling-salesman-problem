package pl.edu.pw.ee;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

class RouletteSelectionTest {

    @Test
    void givenPopulation_whenSelect_thenReturnsIndividual() {
        // given
        final var firstIndividual = createFirstIndividual();
        final var secondIndividual = createSecondIndividual();
        final var thirdIndividual = createThirdIndividual();
        final var population = new Population(List.of(firstIndividual, secondIndividual, thirdIndividual));
        final var random = Mockito.mock(Random.class);
        final var fitnessFunction = Mockito.mock(FitnessFunction.class);
        final var selection = new RouletteSelection(random, fitnessFunction);
        given(fitnessFunction.calculateFitness(firstIndividual)).willReturn(10.0);
        given(fitnessFunction.calculateFitness(secondIndividual)).willReturn(20.0);
        given(fitnessFunction.calculateFitness(thirdIndividual)).willReturn(30.0);
        given(random.nextDouble(120.0)).willReturn(55.0);

        // when
        final var selectedIndividual = selection.select(population);

        // then
        assertEquals(secondIndividual, selectedIndividual);
    }

    private Individual createFirstIndividual() {
        final var firstNode = new Node(0, 0);
        final var secondNode = new Node(3, 4);
        final var thirdNode = new Node(-6, -8);
        final var genes = List.of(firstNode, secondNode, thirdNode);
        return new Individual(genes);
    }

    private Individual createSecondIndividual() {
        final var firstNode = new Node(1, 1);
        final var secondNode = new Node(4, 5);
        final var thirdNode = new Node(-7, -9);
        final var genes = List.of(firstNode, secondNode, thirdNode);
        return new Individual(genes);
    }

    private Individual createThirdIndividual() {
        final var firstNode = new Node(2, 2);
        final var secondNode = new Node(5, 6);
        final var thirdNode = new Node(-8, -10);
        final var genes = List.of(firstNode, secondNode, thirdNode);
        return new Individual(genes);
    }

}