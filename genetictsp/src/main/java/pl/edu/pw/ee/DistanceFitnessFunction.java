package pl.edu.pw.ee;

public class DistanceFitnessFunction implements FitnessFunction {

    @Override
    public int compare(Individual firstIndividual, Individual secondIndividual) {
        return Double.compare(calculateFitness(firstIndividual), calculateFitness(secondIndividual));
    }

    @Override
    public double calculateFitness(Individual individual) {
        final var genome = individual.getGenome();

        var totalDistance = 0.0;
        for (int i = 0; i < genome.size() - 1; i++) {
            final var firstNode = genome.get(i);
            final var secondNode = genome.get(i + 1);
            totalDistance += firstNode.calculateDistance(secondNode);
        }
        totalDistance += genome.getLast().calculateDistance(genome.getFirst());

        return totalDistance;
    }

}
