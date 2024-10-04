package pl.edu.pw.ee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SwapNodesMutation implements Mutation {

    private final Random random;

    public SwapNodesMutation(Random random) {
        this.random = random;
    }

    @Override
    public Individual mutate(Individual individual) {
        final var genes = new ArrayList<>(individual.getGenome());
        final var firstIndex = random.nextInt(genes.size());
        final var secondIndex = random.nextInt(genes.size());
        Collections.swap(genes, firstIndex, secondIndex);
        return new Individual(genes);
    }

}
