import java.util.Map;
import java.util.TreeMap;

public class Punkt {
    final int id;
    final double x;
    final double y;
    Map<Integer, Double> distances;

    public Punkt(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        distances = new TreeMap<>();
    }



    @Override
    public String toString() {
        return id + "";
    }
}
