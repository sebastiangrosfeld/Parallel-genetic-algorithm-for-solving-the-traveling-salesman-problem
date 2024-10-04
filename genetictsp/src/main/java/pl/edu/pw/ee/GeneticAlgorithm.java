package pl.edu.pw.ee;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm implements TSPSolver {

    protected final Random random;
    protected final FitnessFunction fitnessFunction;
    protected final Selection selection;
    protected final Crossover crossover;
    protected final Mutation mutation;
    private final GeneticAlgorithmParameters parameters;

    public GeneticAlgorithm(Random random, FitnessFunction fitnessFunction, Selection selection, Crossover crossover, Mutation mutation, GeneticAlgorithmParameters parameters) {
        this.random = random;
        this.fitnessFunction = fitnessFunction;
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = mutation;
        this.parameters = parameters;
    }

    @Override
    public List<Node> solve(List<Node> nodes) {
        final var population = new Population(nodes, parameters.getPopulationSize(), random);

        for (int i = 0; i < parameters.getGenerations(); i++) {
            performReproduction(population);
            final var bestIndividual = population.getBestIndividual(fitnessFunction);
            System.out.println("Generation " + i + " best: " + fitnessFunction.calculateFitness(bestIndividual));
        }

        return population.getBestIndividual(fitnessFunction).getGenome();
    }

    protected void performReproduction(Population population) {
        final var populationSize = population.getMembers().size();
        final List<Individual> newMembers = new ArrayList<>(populationSize);

        final var eliteIndividuals = getEliteIndividuals(population);
        eliteIndividuals.stream()
                .map(this::applyMutation)
                .forEach(newMembers::add);

        while (newMembers.size() < populationSize) {
            final var offspring = createOffspring(population);
            final var mutatedOffspring = applyMutation(offspring);
            newMembers.add(mutatedOffspring);
        }

        population.setMembers(newMembers);
    }

    protected List<Individual> getEliteIndividuals(Population population) {
        final var count = (int) ((1 - parameters.getCrossoverFactor()) * parameters.getPopulationSize());
        return population.getBestIndividuals(fitnessFunction, count);
    }

    protected Individual createOffspring(Population population) {
        final var firstParent = selection.select(population);
        final var secondParent = selection.select(population);

        if (random.nextDouble() < parameters.getCrossoverProbability()) {
            return crossover.crossover(firstParent, secondParent);
        }

        return random.nextDouble() > 0.5 ? firstParent : secondParent;
    }

    protected Individual applyMutation(Individual individual) {
        if (random.nextDouble() < parameters.getMutationProbability()) {
            return mutation.mutate(individual);
        }

        return individual;
    }

}
