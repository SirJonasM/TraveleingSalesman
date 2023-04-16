import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static Visualization vis;
    public static final String TOURNAME = "usa48";

    public static final String TOURPATH = "Datensätze/";
    public static final File TOURFILE = new File(TOURPATH + TOURNAME+ ".tsp");
    public static final double ZOOM = 1.0;
    public static  Punkt[] data;
    private static final Random random = new Random();
    static TSP tsp;


    public static void main(String[] args) throws Exception {

        data = readFile();
        data = randomizedData(5000);

        
        setDistances(data);

        tsp = new TSPImpl(data, TSP.BEGINWITHATENTH, 10,8,6,false);
        tsp.start();
        Thread visualization = new Thread(() -> vis = new Visualization());
        visualization.start();
        visualization.join();
        System.out.println("--------------START-------------");
        while (vis != null){
            if(tsp.evolution()){
                vis.drawGraph.updateUI();
            }
            vis.drawInfo.updateUI();
        }
    }
    public static void setDistances(Punkt [] data){
        for (Punkt punkt1 : data) {
            for (Punkt punkt2 : data) {
                punkt1.distances.put(punkt2.id, HilfsFormeln.getAbstand(punkt1, punkt2));
            }
        }
    }

    public static Punkt[] readFile() throws FileNotFoundException {
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
}
