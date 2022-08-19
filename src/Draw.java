import javax.swing.JLabel;
import java.awt.*;

//Beste L�sung: Fitness: 33588.34 | [31, 20, 12, 24, 13, 22, 10, 14, 19, 11, 46, 32, 45, 35, 29, 42, 16, 26, 18, 36, 5, 27, 6, 17, 43, 30, 0, 7, 8, 37, 39, 2, 15, 21, 40, 33, 28, 1, 25, 3, 34, 44, 9, 23, 41, 4, 47, 38]
public class Draw extends JLabel {

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(100,100,100));
        for (int i = 0; i < TSP.best.mutation.length; i++) {
            String iS = String.valueOf(TSP.best.mutation[i].id);
            int x1 = (int) (TSP.best.mutation[i].x/TSP.ZOOM) ;
            int y1 = (int) (TSP.best.mutation[i].y /TSP.ZOOM)+10;

            int x2 = (int) (TSP.best.mutation[(i + 1) % TSP.best.mutation.length].x/TSP.ZOOM) ;
            int y2 = (int) (TSP.best.mutation[(i + 1) % TSP.best.mutation.length].y/TSP.ZOOM)+10;
            g2d.fillOval(x1-3,y1-3,6,6);
            g2d.drawLine(x1, y1, x2, y2);
            //g.drawString(iS,x1,y1);

        }
        String fittnes = "Fittnes: " + TSP.best.fitness;
        Double currentBest = TSP.anzahlBerchnungenBeiBeste/100_000/10.0;
        Double count = Permutation.count/100_000/10.0;
        Double diff = (Permutation.count -TSP.anzahlBerchnungenBeiBeste)/100_000/10.0;
        String berechnungen = "Berechnungen: " + currentBest + " Mio." + " (" + count + " Mio." + ") Diff: "  +diff + " Mio.";


        g.drawRect(5,700,450,40);
        g.drawRect(5,750,230,40);


        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.PLAIN,20));
        g.drawString(fittnes,15,775);
        g.drawString(berechnungen,15,725);
        repaint();
    }
}
