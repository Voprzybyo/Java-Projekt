import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ButtonPanel extends JPanel{

    public static final int HEIGHT = 100;
    public static final int WIDTH = 200;
    private JButton Button1;
    private JButton Button2;
    private JButton Button3;

    private JPanel buttonPanel;

    public ButtonPanel() {

        Button1 = new Button1();
        Button2 = new Button2();
        Button3 = new Button3();

        buttonPanel = this;

        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        add(Button1);
        add(Button2);
        add(Button3);
    }

    class Button1 extends JButton implements ActionListener {

        Button1() {
            super("Co to jest?");
            setFont(new Font("Helvetica", Font.BOLD, 15));
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new ObrazFrame();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    class Button2 extends JButton implements ActionListener {

        Button2() {
            super("POLSKA");
            setFont(new Font("Helvetica", Font.BOLD, 15));
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            InfoFrame.RamkaInformacyjnaPolska();
        }
    }

    class Button3 extends JButton implements ActionListener {

        Button3() {
            super("ŚWIAT");
            setFont(new Font("Helvetica", Font.BOLD, 15));
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            InfoFrame.RamkaInformacyjnaSwiat();
        }
    }

}