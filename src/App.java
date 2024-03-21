import javax.swing.*;
import java.awt.*;

public class App {

    public static final String TITLE = "Editor";

    public static final int WIDTH = 1600, HEIGHT = 900;
    public static void main(String[] args) {

        JFrame frame = new JFrame(TITLE);
        JPanel panel = new JPanel();
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();

        panel.setSize(WIDTH, HEIGHT);
        panel.setPreferredSize(panel.getSize());
        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocation((screenSize.width - frame.getWidth()) / 2, 0);
        frame.setVisible(true);

    }
}
