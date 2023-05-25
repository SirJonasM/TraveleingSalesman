import java.util.ArrayList;

public class HelpMethods {

    public static double getAbstand(Point point1, Point point2) {

        return Math.sqrt((point1.x - point2.x) * (point1.x - point2.x) + (point1.y - point2.y) * (point1.y - point2.y));
    }
    public static double getNINTAbstand(Point point1, Point point2) {
        double rij = Math.sqrt(((point1.x - point2.x) * (point1.x - point2.x) + (point1.y - point2.y) * (point1.y - point2.y))/10.0);
        int tij = nint(rij);
        return (tij<rij) ? tij+1 : tij;
    }

    public static int nint(double rij){
        if(rij>0) return (int)(rij + 0.5);
        return (int)(rij-0.5);

    }
    public static int minAbstand(Point point, ArrayList<Point> liste) {
        double minDis = getAbstand(point, liste.get(0));
        int minId = 0;
        for (int i = 0; i < liste.size(); i++) {
            double dis = getAbstand(point, liste.get(i));
            if (dis < minDis) {
                minId = i;
                minDis = dis;
            }
        }
        return (minId);
    }
}
