package pl.edu.pw.ee;

public class ParallelGeneticAlgorithmParameters extends GeneticAlgorithmParameters {

    private final int islands;
    private final int migrationInterval;
    private final double migrationFactor;

    public ParallelGeneticAlgorithmParameters(
            int populationSize,
            int generations,
            double crossoverFactor,
            double crossoverProbability,
            double mutationProbability,
            int islands,
            int migrationInterval,
            double migrationFactor
    ) {
        super(populationSize, generations, crossoverFactor, crossoverProbability, mutationProbability);
        this.islands = islands;
        this.migrationInterval = migrationInterval;
        this.migrationFactor = migrationFactor;
    }

    public int getIslands() {
        return islands;
    }

    public int getMigrationInterval() {
        return migrationInterval;
    }

    public double getMigrationFactor() {
        return migrationFactor;
    }

}
