import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class App {

    public static final String TITLE = "Editor";

    public static final int WIDTH = 1600, HEIGHT = 900;

    private static FileTabList tabList;

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

    public App() {
        tabList = new FileTabList();
    }

    public void loadFile(File f) {

        String title = f.getName();
        StringBuilder sBuilder = new StringBuilder();

        try {

            InputStreamReader  in = new InputStreamReader (new FileInputStream(f), Charset.forName("UTF-8"));
            char[]fileData = new char[1024];
            int cnt;

            while ((cnt = in.read(fileData)) > 0)
                sBuilder.append(new String(fileData, 0, cnt));

            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        TabPanel tabPanel = new TabPanel(sBuilder.toString(), title);
        tabList.addTab(tabPanel);

    }

    public void init() {

        JFrame frame = new JFrame(TITLE);

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();

        JMenuBar topMenu = new JMenuBar();

        addMenuItems(topMenu);
        frame.add(topMenu, BorderLayout.NORTH);
        frame.add(tabList);

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
