import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static Visualization vis;
    public static final String TOURNAME = "usa48";

    public static final String TOURPATH = "Datensätze/";
    public static final File TOURFILE = new File(TOURPATH + TOURNAME+ ".tsp");
    public static final double ZOOM = 8.0;

    public static void main(String[] args) throws Exception {

        Punkt[] data;
        data = readFile();
        setDistances(data);

        TSP tspImpl = new TSPImpl(100,8,6,false);

        Thread visualization = new Thread(() -> vis = new Visualization());
        visualization.start();
        tspImpl.start(data);
        while (vis == null){
            Thread.sleep(10);
        }
        System.out.println("--------------START-------------");
        while (vis != null){
            if(tspImpl.evolution()) vis.draw.updateUI();
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
}
