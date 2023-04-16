import javax.swing.*;
import java.awt.*;

public class DrawInfo extends JLabel {
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new java.awt.Font("Source Code Pro", Font.PLAIN, 18));
        g.setColor(new Color(200,200,200));
        String out1 = "Gen: " + Main.tsp.getGeneration();
        String out2 = "Fitness: " +  Math.round(TSPImpl.best.fitness*100.0)/100.0;
        String outfit2 = "Fitness: " +  Math.round(TSPImpl.best2.fitness*100.0)/100.0;
        String outfit3 = "Fitness: " +  Math.round(TSPImpl.best3.fitness*100.0)/100.0;

        g.drawString(out1,10,18);
        g.drawString(out2,10,40);
        g.drawString(outfit2,200,40);
        g.drawString(outfit2,400,40);


        if(Main.tsp.getCurrentPoints() != Main.tsp.getMaxPoints()){
            String out3 = String.format("[%d / %d]",Main.tsp.getCurrentPoints(),Main.data.length);
            g.drawString(out3,250,18);
            String out4 = "Nächster Punkt in " + Main.tsp.getCurrentCounter()/1000 + "k Gen.";
            g.drawString(out4,350,18);
        }

    }
}
