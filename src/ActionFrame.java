import javax.swing.*;
import java.awt.*;

public class ActionFrame extends JFrame {

    public ActionFrame() {
        super("COVID-19");
        JPanel panel = new JPanel();
        add(panel);
        JLabel tekst = new JLabel("COVID-19");
        tekst.setFont(new Font("Helvetica", Font.BOLD, 35));
        tekst.setForeground( Color.red.darker());
        panel.add(tekst);
        panel.setSize(200,200);
        JPanel buttonPanel = new ButtonPanel();
        add(buttonPanel);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(300,200);
        setLocation(600,300);
        setVisible(true);
    }
}