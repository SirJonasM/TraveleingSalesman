import java.util.*;


public class TSPImpl implements TSP{
    int nextPoint;
    public final int ANZAHL_SAVINGS;
    private final int CHILD_NUMBER;
    private final boolean VOROPTIMIERUNG;
    private final int STARTPOP;
    public final Point[] data;
    public int gen = 0;
    public int currentCounter;
    public Random random = new Random();
    public Permutation[] best = new Permutation[3];

    public Permutation[] permutations;
    public int counterTillNextPoint = 50_000;

    public TSPImpl(Point[] data, int startingPoints, int STARTPOP, int ANZAHL_SAVINGS, int CHILD_NUMBER, boolean VOROPTIMIERUNG){
        this.data = data;
        this.STARTPOP = STARTPOP;
        this.ANZAHL_SAVINGS =ANZAHL_SAVINGS;
        this.CHILD_NUMBER = CHILD_NUMBER;
        this.VOROPTIMIERUNG = VOROPTIMIERUNG;
        nextPoint = startingPoints;
        if(nextPoint == -1) nextPoint = data.length;
        if(nextPoint == -2) nextPoint = data.length/10;
        currentCounter = counterTillNextPoint;
    }
    public void start(){
        Point[] startData = new Point[nextPoint];
        System.arraycopy(data,0,startData,0,nextPoint);
        permutations = fillStartPermutations(startData);
        chooseMutations();
        best[0] = permutations[0].getCopy();
        best[1] = permutations[1].getCopy();
        best[2] = permutations[2].getCopy();
    }

    public boolean evolute() {
        mutate();
        chooseMutations();
        gen++;
        currentCounter--;
        if(getCurrentPoints() != getMaxPoints() && getCurrentCounter() < 0){
            currentCounter = counterTillNextPoint;
            addPoint();
        }

        if(permutations[0].fitness<best[2].fitness){
            best[2] = permutations[0].getCopy();
            Arrays.sort(best);
            return true;
        }
        return false;
    }


     public  Permutation[] fillStartPermutations(Point[] mutation) {
        Permutation[] neu = new Permutation[STARTPOP];
        for(int n = 0; n<STARTPOP;n++) {
            if(VOROPTIMIERUNG) {
                ArrayList<Point> mut1 = new ArrayList<>(List.of(mutation));
                ArrayList<Point> mut2 = new ArrayList<>();
                int r = random.nextInt(mut1.size());
                mut2.add(mut1.get(r));
                mut1.remove(r);

                for (int i = 0; i < mutation.length - 1; i++) {
                    int id = HelpMethods.minAbstand(mut2.get(i), mut1);
                    mut2.add(mut1.get(id));
                    mut1.remove(id);
                }

                Point[] mut = mut2.toArray(new Point[0]);
                neu[n] = new Permutation(mut);
            }
            else{
                neu[n] = new Permutation(mutation);
            }
        }
        return neu;
    }

    private void crossover(){
        Permutation [] neu = new Permutation[ANZAHL_SAVINGS*2];

        for(int i = 0;i < neu.length; i++){
            int parent1 = random.nextInt(permutations.length);
            int parent2 = random.nextInt(permutations.length);
            while(parent1 == parent2)
                parent2 = random.nextInt(permutations.length);
            neu[i] = permutations[parent1].crossOver(permutations[parent2]);

        }
        permutations = neu;
    }
    void chooseMutations() {
        Arrays.sort(permutations);
        Permutation[] permutationsChosen = new Permutation[ANZAHL_SAVINGS];
        for(int i = 0; i<ANZAHL_SAVINGS;i++){
            double rand = random.nextDouble();
            permutationsChosen[i] = (rand<0.9) ? permutations[i] : permutations[permutations.length-1-i];
        }
        permutations = permutationsChosen;
    }

    private void mutate() {
        crossover();
        int count = 0;
        Permutation[] neu = new Permutation[permutations.length*CHILD_NUMBER];
        for (Permutation permutation : permutations) {
            for (int i = 0; i < CHILD_NUMBER; i++) {
                Permutation p = permutation.getCopy();
                p.makeChanges(1);
                neu[count] = p;
                count++;
            }
        }
        permutations = neu;
    }
    public int getCurrentPoints() {
        return nextPoint;
    }

    public int getMaxPoints() {
        return data.length;
    }

    public int getCurrentCounter() {
        return currentCounter;
    }

    public int getGeneration() {
        return gen;
    }

    @Override
    public void addPoint() {
        data[nextPoint].justAdded = true;
        if(data[nextPoint-1].justAdded)data[nextPoint-1].justAdded = false;
        for(Permutation permutation : permutations){
            permutation.addPoint(data[nextPoint]);
        }
        System.arraycopy(permutations,0,best,0,3);
        nextPoint++;
    }

    @Override
    public Permutation[] getBest() {
        return best;
    }


    @Override
    public String toString(){
        return gen + ": " + Arrays.toString(best);
    }

}