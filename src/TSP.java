import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class TSP  {
    public static final int ANZAHL_SAVINGS = 8;
    public static final int CHILD_NUMBER = 6;
    private static final boolean VOROPTIMIERUNG = false;
    private static final int STARTPOP = 100;
    public static final String TOURNAME = "usa48";

    public static Random random = new Random();
    public static Permutation best;

    public static Permutation[] permutations;
    public static int anzahlBerchnungenBeiBeste = 0;

    public static final double ZOOM = 8.0;
    public static Visualization vis;
    public static final String TOURPATH = "Datensätze/";
    public static  final File TOURFILE = new File(TOURPATH + TOURNAME+ ".tsp");

    public static  final File OPTIMUMTOURFILE = new File(TOURPATH + TOURNAME +".opt");

    public static void main(String[] args) throws Exception {
        Punkt[] data;
        data = readFile();
        setDistances(data);

        permutations = fillStartPermutations(data);
        chooseMutations();
        best = permutations[0].getCopy();
        Thread visualization = new Thread(() -> vis = new Visualization());
        visualization.start();
        while (vis == null){
            Thread.sleep(10);
        }
        System.out.println("--------------START-------------");
        while (vis != null){
            mutate();
            chooseMutations();

            if(permutations[0].compareTo(best)<0) {
                System.out.println(best);
                best = permutations[0].getCopy();
                anzahlBerchnungenBeiBeste = Permutation.count;
                vis.draw.updateUI();
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
                punkt.distances.put(datum.id, HilfsFormeln.getAbstand(punkt, datum));
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

    public static void crossover(){
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
        TSP.permutations = neu;
    }
    public static void chooseMutations() {
        Arrays.sort(permutations);
        Permutation[] permutationsChosen = new Permutation[ANZAHL_SAVINGS];
        for(int i = 0; i<ANZAHL_SAVINGS;i++){
            double rand = random.nextDouble();
            permutationsChosen[i] = (rand<0.9) ? permutations[i] : permutations[permutations.length-1-i];
        }
        permutations = permutationsChosen;
    }

    public static void mutate() {
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