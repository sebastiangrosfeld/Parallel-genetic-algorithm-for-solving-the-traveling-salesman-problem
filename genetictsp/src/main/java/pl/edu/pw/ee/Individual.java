package pl.edu.pw.ee;

import java.util.List;

public class Individual {

    private final List<Node> genome;

    public Individual(List<Node> genes) {
        this.genome = genes;
    }

    public List<Node> getGenome() {
        return genome;
    }

}
