import java.util.*;


public class TSPImpl implements TSP{
    public final int ANZAHL_SAVINGS;
    private final int CHILD_NUMBER;
    private final boolean VOROPTIMIERUNG;
    private final int STARTPOP;

    public Random random = new Random();
    public static Permutation best;

    public Permutation[] permutations;
    public int anzahlBerchnungenBeiBeste = 0;


    public TSPImpl(int STARTPOP, int ANZAHL_SAVINGS, int CHILD_NUMBER, boolean VOROPTIMIERUNG){
        this.STARTPOP = STARTPOP;
        this.ANZAHL_SAVINGS =ANZAHL_SAVINGS;
        this.CHILD_NUMBER = CHILD_NUMBER;
        this.VOROPTIMIERUNG = VOROPTIMIERUNG;
    }
    public void start(Punkt[] punkte){
        permutations = fillStartPermutations(punkte);
        chooseMutations();
        best = permutations[0].getCopy();
    }

    public boolean evolution() {
        mutate();
        chooseMutations();

        if(permutations[0].compareTo(best)<0) {
            System.out.println(best);
            best = permutations[0].getCopy();
            anzahlBerchnungenBeiBeste = Permutation.count;
            return true;
        }
        return false;
    }
    private Punkt[] randomizedData(int punktAnzahl) {
        Punkt [] data = new Punkt[punktAnzahl];
        for(int i = 0; i<punktAnzahl;i++){
            int x = random.nextInt(980);
            int y = random.nextInt(700);
            Punkt p = new Punkt(i,x,y);
            data[i] = p;
        }
        return data;
    }

     public  Permutation[] fillStartPermutations(Punkt[] mutation) {
        Permutation[] neu = new Permutation[STARTPOP];
        for(int n = 0; n<STARTPOP;n++) {
            if(VOROPTIMIERUNG) {
                ArrayList<Punkt> mut1 = new ArrayList<>(List.of(mutation));
                ArrayList<Punkt> mut2 = new ArrayList<>();
                int r = random.nextInt(mut1.size());
                mut2.add(mut1.get(r));
                mut1.remove(r);

                for (int i = 0; i < mutation.length - 1; i++) {
                    int id = HilfsFormeln.minAbstand(mut2.get(i), mut1);
                    mut2.add(mut1.get(id));
                    mut1.remove(id);
                }

                Punkt[] mut = mut2.toArray(new Punkt[0]);
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
        System.arraycopy(permutations,0,neu,0,ANZAHL_SAVINGS);
        for(int i = 0; i<neu.length-ANZAHL_SAVINGS;i+=2){

            Punkt [] parent1 = new Punkt[permutations[0].mutation.length];
            Punkt [] parent2 = new Punkt[permutations[0].mutation.length];
            int rand1 = random.nextInt(ANZAHL_SAVINGS);
            int rand2 = random.nextInt(ANZAHL_SAVINGS);
            System.arraycopy(permutations[rand1].mutation,0,parent1,0,parent1.length);
            System.arraycopy(permutations[rand2].mutation,0,parent2,0,parent1.length);

            Punkt[] child1 = new Punkt[parent1.length];
            Punkt[] child2 = new Punkt[parent1.length];

            System.arraycopy(parent1,0,child1,0,child1.length);
            System.arraycopy(parent2,0,child2,0,child1.length);

            int index1 = random.nextInt(child1.length/2);
            int index2 = index1+random.nextInt(child2.length/2);
            for(int index = index1;index<index2;index++){
                for(int index4 = 0; index4< parent2.length;index4++) {
                    if(child1[index4].id == parent2[index].id){
                        Punkt storage = child1[index];
                        child1[index] = parent2[index];
                        child1[index4] = storage;
                    }
                    if(child2[index4].id == parent1[index].id){
                        Punkt storage = child2[index];
                        child2[index] = parent1[index];
                        child2[index4] = storage;
                    }
                }

            }
            neu[i+ANZAHL_SAVINGS] = new Permutation(child1);
            neu[i+1+ANZAHL_SAVINGS] = new Permutation(child2);
        }
        permutations = neu;
    }
    private void chooseMutations() {
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
                p.makeChanges();
                neu[count] = p;
                count++;
            }
        }
        permutations = neu;
    }


}