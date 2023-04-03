import javax.swing.*;
import java.awt.*;

public class Visualization extends JFrame{
    JFrame frame;
    JLabel draw;
    public Visualization(){
        frame = new JFrame("TSP");
        frame.setSize(1000,850);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.requestFocus();
        draw = new Draw();
        draw.setBounds(0,0,1000,850);
        draw.setVisible(true);
        draw.setOpaque(true);
        draw.setBackground(Color.BLACK);
        frame.add(draw);
        frame.setVisible(true);
    }
}
