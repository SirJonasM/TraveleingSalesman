import java.util.*;

public class Permutation implements Comparable<Permutation> {
    public static int count = 0;
    private static final Random random = new Random();
    Punkt[] mutation;
    double fitness;

    public Permutation(Punkt[] mutation) {
        this.mutation = mutation;
        this.fitness = Math.round(berechneFitness() * 100.0) / 100.0;
    }
    public Permutation(Punkt[] mutation,double fitness) {
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

    public void addPoint(Punkt [] allPoints){
        if(allPoints.length == mutation.length) return;
        Random r = new Random();
        int[] indices = new int[allPoints.length-mutation.length];
        int counter = 0;
        for(int i = 0;i<allPoints.length;i++){
            boolean isInIt = false;
            for (Punkt p : mutation){
                if(allPoints[i].equals(p)){
                    isInIt = true;
                }
            }
            if(!isInIt){
                indices[counter] = i;
                counter++;
            }
        }
        Punkt[] newMutation = new Punkt[mutation.length+1];
        System.arraycopy(mutation,0,newMutation,0,mutation.length);
        newMutation[newMutation.length-1] = allPoints[indices[r.nextInt(indices.length)]];
        mutation = newMutation;
        berechneFitness();
    }
    public void makeChanges() {
        int rand = random.nextInt(4);
        if (rand == 0) {
            tworsMutation();
        } else {
            reverseSequence();
        }
        berechneFitness();
    }

    private void reverseSequence() {
        int a = random.nextInt(mutation.length);
        int b = random.nextInt(mutation.length - a) + a;
        int mid = a + (b - a) / 2;
        Punkt storage;
        for (int i = a; i < mid; i++) {
            storage = mutation[i];
            mutation[i] = mutation[b - i];
            mutation[b - i] = storage;
        }
    }

    private void tworsMutation() {
        int change1 = random.nextInt(mutation.length);
        int change2 = (change1 + random.nextInt(0, mutation.length/5+3) + 1) % mutation.length;
        Punkt storage = mutation[change1];
        mutation[change1] = mutation[change2];
        mutation[change2] = storage;
    }

    public Permutation getCopy(){
        Punkt[] neu = new Punkt[mutation.length];
        System.arraycopy(mutation,0,neu,0,mutation.length);
        return new Permutation(neu,fitness);
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

