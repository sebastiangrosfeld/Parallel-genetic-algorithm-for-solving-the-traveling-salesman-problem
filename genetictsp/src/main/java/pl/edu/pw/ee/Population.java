package pl.edu.pw.ee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Population {

    private List<Individual> members;
    private int generation = 0;

    public Population(List<Individual> individuals) {
        this.members = individuals;
    }

    public Population(List<Node> nodes, int size, Random random) {
        members = new ArrayList<>(size);
        initializePopulation(nodes, size, random);
    }

    public List<Individual> getMembers() {
        return this.members;
    }

    public void setMembers(List<Individual> members) {
        this.members = members;
        generation++;
    }

    public int getGeneration() {
        return generation;
    }

    public Individual getBestIndividual(FitnessFunction fitnessFunction) {
        return Collections.min(members, fitnessFunction);
    }

    public List<Individual> getBestIndividuals(FitnessFunction fitnessFunction, int count) {
        final var individuals = new ArrayList<>(members);
        individuals.sort(fitnessFunction);
        return individuals.subList(0, count);
    }

    private void initializePopulation(List<Node> nodes, int size, Random random) {
        while (members.size() < size) {
            final var individual = createIndividualWithShuffledNodes(nodes, random);
            members.add(individual);
        }
    }

    private Individual createIndividualWithShuffledNodes(List<Node> nodes, Random random) {
        final var genes = new ArrayList<>(nodes);
        Collections.shuffle(genes, random);
        return new Individual(genes);
    }

}
