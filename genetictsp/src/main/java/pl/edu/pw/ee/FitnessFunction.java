package pl.edu.pw.ee;

import java.util.Comparator;

public interface FitnessFunction extends Comparator<Individual> {

    double calculateFitness(Individual individual);

}
