package pl.edu.pw.ee;

public class GeneticAlgorithmParameters {

    private final int populationSize;
    private final int generations;
    private final double crossoverFactor;
    private final double crossoverProbability;
    private final double mutationProbability;

    public GeneticAlgorithmParameters(
            int populationSize,
            int generations,
            double crossoverFactor,
            double crossoverProbability,
            double mutationProbability
    ) {
        this.populationSize = populationSize;
        this.generations = generations;
        this.crossoverFactor = crossoverFactor;
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability = mutationProbability;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getGenerations() {
        return generations;
    }

    public double getCrossoverFactor() {
        return crossoverFactor;
    }

    public double getCrossoverProbability() {
        return crossoverProbability;
    }

    public double getMutationProbability() {
        return mutationProbability;
    }

}
