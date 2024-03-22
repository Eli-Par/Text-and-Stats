import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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

            int act = chooser.showOpenDialog(frame);
            if (act == JFileChooser.APPROVE_OPTION)
                loadFile(chooser.getSelectedFile());

        });

        save.addActionListener(e -> {

        });

    }

    private JTabbedPane editors;
    public App() {
        editors = new JTabbedPane();
    }

    public void loadFile(File f) {

        JTextArea area = new JTextArea();
        String title = f.getName();

        try {

            FileInputStream in = new FileInputStream(f);
            StringBuilder sBuilder = new StringBuilder();
            byte[]fileData = new byte[1024];
            int cnt;

            while ((cnt = in.read(fileData)) > 0)
                sBuilder.append(new String(fileData, 0, cnt));

            area.setText(sBuilder.toString());
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        JScrollPane scroll = new JScrollPane(area);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        editors.addTab(title, scroll);

    }

    public void init() {

        JFrame frame = new JFrame(TITLE);
        // JPanel panel = new JPanel();
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();

        JMenuBar topMenu = new JMenuBar();
        // editors.setSize(panel.getSize());

        addMenuItems(topMenu);
        frame.add(topMenu, BorderLayout.NORTH);
        frame.add(editors);

        // panel.setSize(WIDTH, HEIGHT);
        // panel.setPreferredSize(panel.getSize());
        // //frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.pack();
        frame.setSize(600, 400);
        frame.setLocation((screenSize.width - frame.getWidth()) / 2, 0);
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        new App().init();
    }
}
