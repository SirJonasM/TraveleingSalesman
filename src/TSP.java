import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class TSP {
    public static final int ANZAHL_SAVINGS = 8;
    public static final int CHILD_NUMBER = 4;
    public static final int MAXBERECHNUNGEN = 100_000_000;
    private static final boolean VOROPTIMIERUNG = true;
    private static final int STARTPOP = 100;
    public static final String TOURNAME = "ch130";

    private static int TAUSCHRADIUS;

    public static Random random = new Random();
    public static Permutation best;

    public static int anzahlBerchnungenBeiBeste = 0;

    public static final double ZOOM = 1.1;

    public static Visualization vis;
    public static final String TOURPATH = "Datensätze/";
    public static  final File TOURFILE = new File(TOURPATH + TOURNAME+ ".tsp");

    public static  final File OPTIMUMTOURFILE = new File(TOURPATH + TOURNAME +".opt");

    public static void main(String[] args) throws Exception {
        Punkt[] data = readFile();
        setDistances(data);
        TAUSCHRADIUS = data.length/5+1;
        Permutation[] permutations = fillStartPermutations(data);
        best = permutations[0];
        permutations = chooseMutations(permutations);
        best = permutations[0];
        int count = 0;
        int z = 50_000;
        System.out.println("--------------START-------------");
        while (Permutation.count < MAXBERECHNUNGEN) {
            if(count>z){
                System.out.println("Berechnungen: " + Permutation.count + " | Generation:" +count);
                System.out.println("Best: " + best);
                z+=50_000;
            }

            permutations = mutate(permutations);
            permutations = chooseMutations(permutations);


            if(permutations[0].compareTo(best)<0) {
                Punkt [] neu = new Punkt[permutations[0].mutation.length];
                System.arraycopy(permutations[0].mutation,0,neu,0,neu.length);
                best = new Permutation(neu);
                System.out.println(best);
                anzahlBerchnungenBeiBeste = Permutation.count;
            }
            count++;
        }
        System.out.println("---------------------------------------------------------------------------------------------------");
        Arrays.stream(permutations).forEach(System.out::println);
        System.out.println("Beste Lösung: " + best);
        System.out.println("Generationen: " + count);
        System.out.println("Berechnungen: " + Permutation.count);
    }

    public static void main(boolean randomized,int punktAnzahl) throws Exception {

        Punkt[] data;
        if(!randomized){
            data = readFile();
        }
        else{
            data  = randomizedData(punktAnzahl);
        }
        setDistances(data);
        TAUSCHRADIUS = data.length/5+1;

        Permutation[] permutations = fillStartPermutations(data);
        best = permutations[0];
        permutations = chooseMutations(permutations);
        best = permutations[0];

        System.out.println("--------------START-------------");
        vis = new Visualization();
        while (true){
            permutations = mutate(permutations);
            permutations = chooseMutations(permutations);

            if(permutations[0].compareTo(best)<0) {
                Punkt [] neu = new Punkt[permutations[0].mutation.length];
                System.arraycopy(permutations[0].mutation,0,neu,0,neu.length);
                best = new Permutation(neu);
                anzahlBerchnungenBeiBeste = Permutation.count;
            }
        }
    }


    private static Punkt[] randomizedData(int punktAnzahl) {
        Punkt [] data = new Punkt[punktAnzahl];
        for(int i = 0; i<punktAnzahl;i++){
            int x = random.nextInt(980);
            int y = random.nextInt(700);
            Punkt p = new Punkt(i,x,y);
            data[i] = p;
        }
        return data;
    }

    static Punkt[] readFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(TOURFILE);
        Punkt[] punkte = {};
        int length = 0;
        for(int i = 0; i<6;i++){
            String n = scanner.nextLine();
            String n1 = n.split(":")[0];
            if(n1.equals("DIMENSION ")){
                String n2 = n.split(" ")[2];
                length = Integer.parseInt(n2);
                punkte = new Punkt[length];
                continue;
            }
            if(n.equals("NODE_COORD_SECTION")){
                for(int z = 0; z<length;z++){
                    int index = scanner.nextInt();
                    String xS = scanner.next();
                    String yS = scanner.next();
                    double x = Double.parseDouble(xS);
                    double y = Double.parseDouble(yS);
                    Punkt punkt = new Punkt(index-1,x,y);
                    punkte[index-1] = punkt;
                    if(xS.equals("EOF")) break;
                }
            }
        }
        return punkte;
    }

    public static void setDistances(Punkt [] data){
        for (Punkt punkt : data) {
            for (Punkt datum : data) {
                punkt.distances.put(datum.id, getAbstand(punkt, datum));
            }
        }
    }
    //---------- Voroptimierung -----------------
    //Füllt die Startpopulation.
    //Dabei wird eine Route so gewählt, dass es einen zufälligen Anfangspunkt gibt
    //und dann der nächste Punkt zu dem vorherigen Punkt gewählt wird bis alle Punkte eine Position haben.
    public static Permutation[] fillStartPermutations(Punkt[] mutation) {
        Permutation[] neu = new Permutation[STARTPOP];
        for(int n = 0; n<STARTPOP;n++) {
            if(VOROPTIMIERUNG) {
                ArrayList<Punkt> mut1 = new ArrayList<>(List.of(mutation));
                ArrayList<Punkt> mut2 = new ArrayList<>();
                int r = random.nextInt(mut1.size());
                mut2.add(mut1.get(r));
                mut1.remove(r);

                for (int i = 0; i < mutation.length - 1; i++) {
                    int id = minAbstand(mut2.get(i), mut1);
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

    public static Permutation[] crossover(Permutation[] permutationen){
        Permutation [] neu = new Permutation[ANZAHL_SAVINGS*2];
        System.arraycopy(permutationen,0,neu,0,ANZAHL_SAVINGS);
        for(int i = 0; i<neu.length-ANZAHL_SAVINGS;i+=2){

            Punkt [] parent1 = new Punkt[permutationen[0].mutation.length];
            Punkt [] parent2 = new Punkt[permutationen[0].mutation.length];
            int rand1 = random.nextInt(ANZAHL_SAVINGS);
            int rand2 = random.nextInt(ANZAHL_SAVINGS);
            System.arraycopy(permutationen[rand1].mutation,0,parent1,0,parent1.length);
            System.arraycopy(permutationen[rand2].mutation,0,parent2,0,parent1.length);

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
        return neu;
    }
    public static Permutation[] chooseMutations(Permutation[] permutations) {
        Arrays.sort(permutations);
        Permutation[] permutationsChosen = new Permutation[ANZAHL_SAVINGS];
        for(int i = 0; i<ANZAHL_SAVINGS-1;i++){
            double rand = random.nextDouble();
            permutationsChosen[i] = (rand<0.9) ? permutations[i] : permutations[permutations.length-1-i];
        }
        permutationsChosen[ANZAHL_SAVINGS-1] = best;
        return permutationsChosen;
    }

    public static Permutation[] mutate(Permutation[] permutations) {
        permutations = crossover(permutations);
        Permutation[] mutatedPermutations = new Permutation[permutations.length * CHILD_NUMBER];

        int count = 0;
        for (Permutation permutation : permutations) {
            for (int i = 0; i < CHILD_NUMBER; i++) {
                mutatedPermutations[count] = makeChanges(permutation);
                count++;
            }
        }
        return mutatedPermutations;
    }
    public static Permutation makeChanges(Permutation permutation) {
        Punkt [] neu = new Punkt[permutation.mutation.length];
        System.arraycopy(permutation.mutation,0,neu,0,neu.length);

        int rand = random.nextInt(10);
        //rand = 4;
        switch (rand) {
            case 0 -> centerInverse(neu);
            case 1 -> reverseSequence(neu);
            default -> tworsMutation(neu);
        }
        return new Permutation(neu);
    }

    private static void reverseSequence(Punkt [] neu) {
        int a = random.nextInt(neu.length);
        int b = random.nextInt(neu.length - a) + a;

        Punkt [] reverseSequence = new Punkt[b-a];
        System.arraycopy(neu,a,reverseSequence,0,b-a);
        for(int i = 0; i<reverseSequence.length;i++){
            Punkt storage = reverseSequence[i];
            reverseSequence[i] = reverseSequence[reverseSequence.length-1-i];
            reverseSequence[reverseSequence.length-1-i] = storage;
        }
        System.arraycopy(reverseSequence,0,neu,a,b-a);

    }

    private static void centerInverse(Punkt[] neu) {
        int rand = random.nextInt(neu.length/4,neu.length*3/4);

        for(int i = 0; i<rand/2;i++){
            Punkt storage = neu[i];
            neu[i] = neu[rand-i];
            neu[rand-i] = storage;
        }
        for(int i = 0; i<(neu.length-rand)/2;i++){
            Punkt storage = neu[i+rand];
            neu[i+rand] = neu[neu.length-i-1];
            neu[neu.length-i-1] = storage;
        }
    }

    private static void tworsMutation(Punkt [] neu){
        int change1 = random.nextInt(neu.length);
        int change2 = (change1 + random.nextInt(0,TAUSCHRADIUS)+1)%neu.length;
       Punkt storage = neu[change1];
        neu[change1] = neu[change2];
        neu[change2] = storage;
    }

    public static double getAbstand(Punkt punkt1, Punkt punkt2) {
//        double rij = Math.sqrt(((punkt1.x - punkt2.x) * (punkt1.x - punkt2.x) + (punkt1.y - punkt2.y) * (punkt1.y - punkt2.y))/10.0);
//        int tij = nint(rij);
//        return (tij<rij) ? tij+1 : tij;
        return Math.sqrt((punkt1.x - punkt2.x) * (punkt1.x - punkt2.x) + (punkt1.y - punkt2.y) * (punkt1.y - punkt2.y));
    }
    public static int nint(double rij){
        if(rij>0) return (int)(rij + 0.5);
        return (int)(rij-0.5);

    }
    public static int minAbstand(Punkt punkt, ArrayList<Punkt> liste) {
        double minDis = getAbstand(punkt, liste.get(0));
        int minId = 0;
        for (int i = 0; i < liste.size(); i++) {
            double dis = getAbstand(punkt, liste.get(i));
            if (dis < minDis) {
                minId = i;
                minDis = dis;
            }
        }
        return (minId);
    }

}
