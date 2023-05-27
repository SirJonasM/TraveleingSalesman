import javax.swing.*;
import java.awt.*;

public class VisualizationFrame extends JFrame {
    JFrame frame;
    JLabel drawGraph;
    JLabel drawInfo;
    public VisualizationFrame(TSP tsp) {
        frame = new JFrame("TSP");
        frame.setSize(1000,850);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.requestFocus();
        drawGraph = new DrawGraph(tsp);
        drawGraph.setBounds(0,0,1000,650);
        drawGraph.setVisible(true);
        drawGraph.setOpaque(true);
        drawGraph.setBackground(Color.BLACK);
        frame.add(drawGraph);
        drawInfo = new DrawInfo(tsp);
        drawInfo.setBounds(0,650,1000,100);

        drawInfo.setVisible(true);
        drawInfo.setOpaque(true);
        drawInfo.setBackground(Color.BLACK);
        frame.add(drawInfo);
        frame.setVisible(true);
    }
}
