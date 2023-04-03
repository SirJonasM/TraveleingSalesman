import java.util.ArrayList;

public class HilfsFormeln {

    public static double getAbstand(Punkt punkt1, Punkt punkt2) {

        return Math.sqrt((punkt1.x - punkt2.x) * (punkt1.x - punkt2.x) + (punkt1.y - punkt2.y) * (punkt1.y - punkt2.y));
    }
    public static double getNINTAbstand(Punkt punkt1, Punkt punkt2) {
        double rij = Math.sqrt(((punkt1.x - punkt2.x) * (punkt1.x - punkt2.x) + (punkt1.y - punkt2.y) * (punkt1.y - punkt2.y))/10.0);
        int tij = nint(rij);
        return (tij<rij) ? tij+1 : tij;
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
