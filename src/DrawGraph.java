import javax.swing.JLabel;
import java.awt.*;

//Beste L�sung: Fitness: 33588.34 | [31, 20, 12, 24, 13, 22, 10, 14, 19, 11, 46, 32, 45, 35, 29, 42, 16, 26, 18, 36, 5, 27, 6, 17, 43, 30, 0, 7, 8, 37, 39, 2, 15, 21, 40, 33, 28, 1, 25, 3, 34, 44, 9, 23, 41, 4, 47, 38]
public class DrawGraph extends JLabel {
    Graphics2D g2d;
    Color green = new Color(0,255,0);
    Color top = new Color(150,0,150);
    Color mid = new Color(200,200,200);
    Color bottom = new Color(80,80,80);
    Color red = new Color(255,0,0);
    private final TSP tsp;

    public DrawGraph(TSP tsp){
        super();
        this.tsp = tsp;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Permutation[] best = tsp.getBest();
        drawMutation(best[0].mutation,bottom,bottom,bottom);
        drawMutation(best[1].mutation, mid,mid,mid);
        drawMutation(best[2].mutation,top,red,green);

        if(Main.tsp.getCurrentPoints() == Main.data.length) return;
        int x1 = (int) (Main.data[Main.tsp.getCurrentPoints()].x/Main.ZOOM) ;
        int y1 = (int) (Main.data[Main.tsp.getCurrentPoints()].y /Main.ZOOM)+10;
        g2d.setColor(new Color(0,0,255));
        g2d.fillOval(x1-3,y1-3,6,6);

    }
    public void drawMutation(Point[] mutation, Color color1, Color color2, Color color3){
        for (int i = 0; i < mutation.length; i++) {
            g2d.setColor(color1);
            if (mutation[i].justAdded) g2d.setColor(color2);
            int x1 = (int) (mutation[i].x / Main.ZOOM) + 3;
            int y1 = (int) (mutation[i].y / Main.ZOOM) + 10;

            int x2 = (int) (mutation[(i + 1) % mutation.length].x / Main.ZOOM)+3;
            int y2 = (int) (mutation[(i + 1) % mutation.length].y / Main.ZOOM) + 10;

            g2d.fillOval(x1 - 3, y1 - 3, 6, 6);
            if (mutation[(i + 1) % mutation.length].justAdded)
                g2d.setColor(color3);
            g2d.drawLine(x1, y1, x2, y2);
        }

    }
}
