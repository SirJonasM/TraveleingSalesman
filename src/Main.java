import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class Main {
    public static Visualization vis;
    public static final String TOURNAME = "usa48";

    public static final String TOURPATH = "Datensätze/";
    public static final File TOURFILE = new File(TOURPATH + TOURNAME+ ".tsp");
    public static final double ZOOM = 8.1;
    public static  Point[] data;
    private static final Random random = new Random();
    static TSP tsp;


    public static void main(String[] args) throws Exception {

        data = readFile();
//        data = randomizedData(5000);
        setDistances(data);

        tsp = new TSPImpl(data, TSP.BEGINWITHALLPOINTS, 10,8,6,false);
        tsp.start();
        Thread visualization = new Thread(() -> vis = new Visualization());
        visualization.start();
        visualization.join();
        System.out.println("--------------START-------------");
        while (vis != null){
            if(tsp.evolute()){
                vis.drawGraph.updateUI();
                System.out.println(tsp);
            }
            vis.drawInfo.updateUI();
        }
    }
    public static void setDistances(Point[] data){
        for (Point point1 : data) {
            for (Point point2 : data) {
                point1.distances.put(point2.id, HelpMethods.getAbstand(point1, point2));
            }
        }
    }

    public static Point[] readFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(TOURFILE);
        Point[] punkte = {};
        int length = 0;
        for(int i = 0; i<6;i++){
            String n = scanner.nextLine();
            String n1 = n.split(":")[0];
            if(n1.equals("DIMENSION ")){
                String n2 = n.split(" ")[2];
                length = Integer.parseInt(n2);
                punkte = new Point[length];
                continue;
            }
            if(n.equals("NODE_COORD_SECTION")){
                for(int z = 0; z<length;z++){
                    int index = scanner.nextInt();
                    String xS = scanner.next();
                    String yS = scanner.next();
                    double x = Double.parseDouble(xS);
                    double y = Double.parseDouble(yS);
                    Point point = new Point(index-1,x,y);
                    punkte[index-1] = point;
                    if(xS.equals("EOF")) break;
                }
            }
        }
        return punkte;
    }
    private static Point[] randomizedData(int punktAnzahl) {
        Point[] data = new Point[punktAnzahl];
        for(int i = 0; i<punktAnzahl;i++){
            int x = random.nextInt(980);
            int y = random.nextInt(700);
            Point p = new Point(i,x,y);
            data[i] = p;
        }
        return data;
    }
}
