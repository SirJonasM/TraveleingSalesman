import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Permutation implements Comparable<Permutation> {
    public static int count = 0;
    Punkt[] mutation;
    double fitness;

    public Permutation(Punkt[] mutation) {
        this.mutation = mutation;
        this.fitness = Math.round(berechneFitness(mutation) * 100.0) / 100.0;
    }



    public double berechneFitness(Punkt[] mutation) {
        count++;
        double fitness = 0;
        for (int i = 0; i < mutation.length; i++) {
            fitness += mutation[i].distances.get(mutation[(i + 1) % mutation.length].id);
        }
        return fitness;
    }

    @Override
    public String toString() {
        return "Fitness: " + Math.round((fitness)*100.0)/100.0 + " | " + Arrays.toString(mutation);
    }

    @Override
    public int compareTo(Permutation o) {
        return Double.compare(this.fitness,o.fitness);
    }
}

