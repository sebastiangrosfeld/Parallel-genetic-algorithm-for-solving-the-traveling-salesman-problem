package pl.edu.pw.ee;

import java.util.LinkedList;
import java.util.Random;

public class SimpleCrossover implements Crossover {

    private final Random random;

    public SimpleCrossover(Random random) {
        this.random = random;
    }

    @Override
    public Individual crossover(Individual firstParent, Individual secondParent) {
        final var genomeSize = firstParent.getGenome().size();
        final var crossoverPoint = random.nextInt(genomeSize);
        final var firstParentGenomePart = firstParent.getGenome().subList(0, crossoverPoint);
        final var firstParentGenomeRestPart = firstParent.getGenome().subList(crossoverPoint, genomeSize);
        final var secondParentGenomePart = secondParent.getGenome().subList(crossoverPoint, genomeSize);
        final var offspringGenome = new LinkedList<>(firstParentGenomePart);
        offspringGenome.addAll(secondParentGenomePart);
        offspringGenome.addAll(firstParentGenomeRestPart);

        for (final var node : secondParentGenomePart) {
            if (firstParentGenomePart.contains(node)) {
                offspringGenome.remove(node);
            }

            if (firstParentGenomeRestPart.contains(node)) {
                offspringGenome.reversed().remove(node);
            }
        }

        return new Individual(offspringGenome);
    }

}
