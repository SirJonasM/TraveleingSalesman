import javax.swing.JLabel;
import java.awt.*;

//Beste L�sung: Fitness: 33588.34 | [31, 20, 12, 24, 13, 22, 10, 14, 19, 11, 46, 32, 45, 35, 29, 42, 16, 26, 18, 36, 5, 27, 6, 17, 43, 30, 0, 7, 8, 37, 39, 2, 15, 21, 40, 33, 28, 1, 25, 3, 34, 44, 9, 23, 41, 4, 47, 38]
public class Draw extends JLabel {

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(100,100,100));
        for (int i = 0; i < TSPImpl.best.mutation.length; i++) {
            int x1 = (int) (TSPImpl.best.mutation[i].x/Main.ZOOM) ;
            int y1 = (int) (TSPImpl.best.mutation[i].y /Main.ZOOM)+10;

            int x2 = (int) (TSPImpl.best.mutation[(i + 1) % TSPImpl.best.mutation.length].x/Main.ZOOM) ;
            int y2 = (int) (TSPImpl.best.mutation[(i + 1) % TSPImpl.best.mutation.length].y/Main.ZOOM)+10;
            g2d.fillOval(x1-3,y1-3,6,6);
            g2d.drawLine(x1, y1, x2, y2);

        }
    }
}
