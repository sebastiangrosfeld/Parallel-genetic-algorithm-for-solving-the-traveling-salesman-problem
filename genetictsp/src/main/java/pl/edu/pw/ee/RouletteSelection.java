package pl.edu.pw.ee;

import java.util.List;
import java.util.Random;

public class RouletteSelection implements Selection {

    private final Random random;
    private final FitnessFunction fitnessFunction;
    private int memoizedPopulationGeneration;
    private List<Double> memoizedSelectionProbabilities;

    public RouletteSelection(Random random, FitnessFunction fitnessFunction) {
        this.random = random;
        this.fitnessFunction = fitnessFunction;
    }

    @Override
    public Individual select(Population population) {
        if (memoizedPopulationGeneration == 0 || memoizedPopulationGeneration != population.getGeneration()) {
            memoizedPopulationGeneration = population.getGeneration();
            final var fitnessValues = calculateFitnessValues(population);
            final var totalFitness = calculateTotalFitness(population);
            memoizedSelectionProbabilities = calculateSelectionProbabilities(fitnessValues, totalFitness);
        }

        return performRouletteSelection(memoizedSelectionProbabilities, population);
    }

    private List<Double> calculateFitnessValues(Population population) {
        return population.getMembers().stream()
                .map(fitnessFunction::calculateFitness)
                .toList();
    }

    private double calculateTotalFitness(Population population) {
        return population.getMembers().stream()
                .map(fitnessFunction::calculateFitness)
                .reduce(Double::sum)
                .orElse(Double.MAX_VALUE);
    }

    private List<Double> calculateSelectionProbabilities(List<Double> fitnessValues, double totalFitness) {
        return fitnessValues.stream()
                .map(fitness -> totalFitness - fitness)
                .toList();
    }

    private double calculateTotalProbability(List<Double> selectionProbabilities) {
        return selectionProbabilities.stream()
                .reduce(Double::sum)
                .orElse(Double.MAX_VALUE);
    }

    private Individual performRouletteSelection(List<Double> selectionProbabilities, Population population) {
        final var totalProbability = calculateTotalProbability(selectionProbabilities);
        final var randomNumber = random.nextDouble(totalProbability);

        var accumulatedProbability = 0.0;
        for (int i = 0; i < selectionProbabilities.size(); i++) {
            if (randomNumber < accumulatedProbability + selectionProbabilities.get(i)) {
                return population.getMembers().get(i);
            }

            accumulatedProbability += selectionProbabilities.get(i);
        }

        return population.getMembers().getLast();
    }

}
