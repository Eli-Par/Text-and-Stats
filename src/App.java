import javax.swing.*;
import java.awt.*;

public class App {

    public static final String TITLE = "Editor";

    public static final int WIDTH = 1600, HEIGHT = 900;

    public void addMenuItems(JMenuBar bar) {

        JMenu fileMenu = new JMenu("File");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem open = new JMenuItem("Open");

        bar.add(fileMenu);
        fileMenu.add(save);
        fileMenu.add(open);

        open.addActionListener(e -> {

            JFileChooser chooser = new JFileChooser();
            JFrame frame = new JFrame("Select File");

            chooser.showOpenDialog(frame);
            System.out.println(chooser.getSelectedFile());

        });

        save.addActionListener(e -> {

        });

    }

    private JTabbedPane editors;
    public App() {
        editors = new JTabbedPane();
    }

    public void init() {

        JFrame frame = new JFrame(TITLE);
        JPanel panel = new JPanel();
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();

        JMenuBar topMenu = new JMenuBar();

        addMenuItems(topMenu);
        panel.add(topMenu);
        panel.add(editors);

        panel.setSize(WIDTH, HEIGHT);
        panel.setPreferredSize(panel.getSize());
        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocation((screenSize.width - frame.getWidth()) / 2, 0);
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        new App().init();
    }
}
