import java.util.*;

public class Permutation implements Comparable<Permutation> {
    public static int count = 0;
    private static final Random random = new Random();
    Point[] mutation;
    double fitness;

    public Permutation(Point[] mutation) {
        this.mutation = mutation;
        this.fitness = berechneFitness();
    }
    public Permutation(Point[] mutation, double fitness) {
        this.mutation = mutation;
        this.fitness = fitness;
    }

    public double berechneFitness() {
        count++;
        fitness = 0;
        for (int i = 0; i < mutation.length; i++) {
            fitness += mutation[i].distances.get(mutation[(i + 1) % mutation.length].id);
        }
        return fitness;
    }

    public void addPoint(Point point){
        Point[] newMutation = new Point[mutation.length+1];
        System.arraycopy(mutation,0,newMutation,0,mutation.length);
        newMutation[mutation.length] = point;
        mutation = newMutation;
        berechneFitness();
    }
    public void makeChanges(int mutationsFaktor) {
        for(int i = 0; i<mutationsFaktor;i++){
            int rand = random.nextInt(4);
            if (rand == 0) {
                tworsMutation();
            } else {
                reverseSequence();
            }
            }
        berechneFitness();
    }

    private void reverseSequence() {
        int a = random.nextInt(mutation.length);
        int b = random.nextInt(mutation.length - a) + a;
        int mid = a + (b - a) / 2;
        Point storage;
        for (int i = a; i < mid; i++) {
            storage = mutation[i];
            mutation[i] = mutation[b - i];
            mutation[b - i] = storage;
        }
    }

    private void tworsMutation() {
        int change1 = random.nextInt(mutation.length);
        int change2 = (change1 + random.nextInt(0, mutation.length/5+3) + 1) % mutation.length;
        Point storage = mutation[change1];
        mutation[change1] = mutation[change2];
        mutation[change2] = storage;
    }

    public Permutation crossOver(Permutation permutation2){
        Point[] newPermutation = new Point[mutation.length];
        int mid = random.nextInt(mutation.length);
        System.arraycopy(mutation,0,newPermutation,0,mid);
        for(int i = 0;i < mutation.length;i++){
            boolean contains = false;
            for(int j = 0;j<mid;j++){
                if(newPermutation[j].equals(permutation2.mutation[i])){
                    contains = true;
                    break;
                }
            }
            if(contains) continue;
            newPermutation[mid] = permutation2.mutation[i];
            mid++;
        }
        return new Permutation(newPermutation);
    }

    public Permutation getCopy(){
        Point[] neu = new Point[mutation.length];
        System.arraycopy(mutation,0,neu,0,mutation.length);
        return new Permutation(neu,fitness);
    }

    @Override
    public String toString() {
        return "Fitness: " + Math.round((fitness)*100.0)/100.0 + " | " + Arrays.toString(mutation);
    }

    public double getFitness(){
        return Math.round(fitness * 100)/100.0;
    }

    @Override
    public int compareTo(Permutation o) {
        return Double.compare(this.fitness,o.fitness);
    }
}

