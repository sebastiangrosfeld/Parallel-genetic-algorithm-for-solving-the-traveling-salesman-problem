package pl.edu.pw.ee;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelGeneticAlgorithm extends GeneticAlgorithm {

    private final ParallelGeneticAlgorithmParameters parameters;

    public ParallelGeneticAlgorithm(
            Random random,
            FitnessFunction fitnessFunction,
            Selection selection,
            Crossover crossover,
            Mutation mutation,
            ParallelGeneticAlgorithmParameters parameters
    ) {
        super(random, fitnessFunction, selection, crossover, mutation, parameters);
        this.parameters = parameters;
    }

    @Override
    public List<Node> solve(List<Node> nodes) {
        final var islands = createIslands(nodes);

        try (final var executorService = Executors.newFixedThreadPool(parameters.getIslands())) {
            runGeneticAlgorithm(islands, executorService);
        } catch (InterruptedException | ExecutionException exception) {
            exception.printStackTrace();
        }

        final var bestIndividual = getBestIndividualAmongIslands(islands);
        return bestIndividual.getGenome();
    }

    private List<Population> createIslands(List<Node> nodes) {
        final var islands = new ArrayList<Population>(parameters.getIslands());

        for (int i = 0; i < parameters.getIslands(); i++) {
            islands.add(new Population(nodes, parameters.getPopulationSize(), random));
        }

        return islands;
    }

    private int calculateNumberOfIterations() {
        var iterations = parameters.getGenerations() / parameters.getMigrationInterval();

        if (parameters.getGenerations() % parameters.getMigrationInterval() != 0) {
            iterations++;
        }

        return iterations;
    }

    private void runGeneticAlgorithm(List<Population> islands, ExecutorService executorService) throws ExecutionException, InterruptedException {
        final var iterations = calculateNumberOfIterations();

        for (int i = 0; i < iterations; i++) {
            final var tasks = performReproductionForIslands(islands, executorService);
            performSynchronization(tasks);
            performMigration(islands);
            final var bestIndividual = getBestIndividualAmongIslands(islands);
            final var generation = i * parameters.getMigrationInterval();
            System.out.println("Generation " + generation + " best: " + fitnessFunction.calculateFitness(bestIndividual));
        }
    }

    private Individual getBestIndividualAmongIslands(List<Population> islands) {
        var bestIndividual = islands.getFirst().getBestIndividual(fitnessFunction);

        for (int i = 1; i < islands.size(); i++) {
            final var islandBestIndividual = islands.get(i).getBestIndividual(fitnessFunction);
            if (fitnessFunction.calculateFitness(islandBestIndividual) < fitnessFunction.calculateFitness(bestIndividual)) {
                bestIndividual = islandBestIndividual;
            }
        }

        return bestIndividual;
    }

    private List<Individual> takeIndividualsToMigration(List<Individual> populationMembers) {
        final var individualsCount = (int) ((double) populationMembers.size() * parameters.getMigrationFactor());
        final List<Individual> individualsToMigrate = new ArrayList<>(individualsCount);

        for (int i = 0; i < individualsCount; i++) {
            final var individual = populationMembers.remove(random.nextInt(populationMembers.size()));
            individualsToMigrate.add(individual);
        }

        return individualsToMigrate;
    }

    private List<Future<?>> performReproductionForIslands(List<Population> islands, ExecutorService executorService) {
        final List<Future<?>> tasks = new ArrayList<>(islands.size());

        for (final var island : islands) {
            tasks.add(executorService.submit(() -> performLocalEvolution(island)));
        }

        return tasks;
    }

    private void performLocalEvolution(Population island) {
        for (int j = 0; j < parameters.getMigrationInterval(); j++) {
            performReproduction(island);
        }
    }

    private void performSynchronization(List<Future<?>> tasks) throws ExecutionException, InterruptedException {
        for (final var task : tasks) {
            task.get();
        }
    }

    private void performMigration(List<Population> islands) {
        for (int j = 1; j < islands.size(); j++) {
            final var firstIsland = islands.get(j - 1);
            final var secondIsland = islands.get(j);
            migrateMembersBetweenPopulations(firstIsland, secondIsland);
        }

        final var firstIsland = islands.getFirst();
        final var lastIsland = islands.getLast();
        migrateMembersBetweenPopulations(firstIsland, lastIsland);
    }

    private void migrateMembersBetweenPopulations(Population firstIsland, Population secondIsland) {
        final var individualsToMigrateFromFirstIsland = takeIndividualsToMigration(firstIsland.getMembers());
        final var individualsToMigrateFromSecondIsland = takeIndividualsToMigration(secondIsland.getMembers());
        firstIsland.getMembers().addAll(individualsToMigrateFromSecondIsland);
        secondIsland.getMembers().addAll(individualsToMigrateFromFirstIsland);
    }

}
