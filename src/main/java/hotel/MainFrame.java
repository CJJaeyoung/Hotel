package hotel;

import javax.swing.*;
        import java.awt.*;

public class MainFrame extends JFrame{
    MainFrame() {
        setTitle("Hotel Customer Management");
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSize(1000,600);
        setLayout(new GridBagLayout());
        setResizable(false);
        setExtendedState(JFrame.NORMAL);
        Login login = new Login(this);
        setContentPane(login);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new MainFrame();
    }


}

