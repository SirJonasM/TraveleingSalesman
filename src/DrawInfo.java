import javax.swing.*;
import java.awt.*;

public class DrawInfo extends JLabel {
    public TSP tsp;
    public DrawInfo(TSP tsp){
        super();
        this.tsp = tsp;
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Permutation[] bestPermutations = tsp.getBest();
        g.setFont(new java.awt.Font("Source Code Pro", Font.PLAIN, 18));
        g.setColor(new Color(200,200,200));
        String outputGeneration = "Gen: " + Main.tsp.getGeneration();
        String outputBest1 = "Fitness: " +  bestPermutations[0].getFitness();
        String outputBest2 = "Fitness: " +  bestPermutations[1].getFitness();
        String outputBest3 = "Fitness: " +  bestPermutations[2].getFitness();

        g.drawString(outputGeneration,10,18);
        g.drawString(outputBest1,10,40);
        g.drawString(outputBest2,200,40);
        g.drawString(outputBest3,400,40);


        if(Main.tsp.getCurrentPoints() != Main.tsp.getMaxPoints()){
            String out3 = String.format("[%d / %d]",Main.tsp.getCurrentPoints(),Main.data.length);
            g.drawString(out3,250,18);
            String out4 = "Nächster Punkt in " + Main.tsp.getCurrentCounter()/1000 + "k Gen.";
            g.drawString(out4,350,18);
        }

    }
}
