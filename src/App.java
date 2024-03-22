import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Scanner;

public class App implements KeyListener {

    public static final String TITLE = "Editor";

    public static final String SETTINGS_PATH = System.getProperty("user.home") + "/.comp2800-settings";

    public static final int WIDTH = 800, HEIGHT = 450;

    private static FileTabList tabList;

    public static Font MENU_FONT = new Font("Tahoma", Font.PLAIN, 30);

    public void addMenuItems(JMenuBar bar) {

        JMenu fileMenu = new JMenu("File");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem open = new JMenuItem("Open");

        bar.add(fileMenu);
        fileMenu.setFont(MENU_FONT);
        fileMenu.add(save);
        fileMenu.add(open);

        open.addActionListener(this::onOpen);
        save.addActionListener(this::onSave);

    }

    public App() {
        tabList = new FileTabList(this);
    }

    public void onOpen(ActionEvent e) {

        JFileChooser chooser = new JFileChooser(getLastOpenPath());
        JFrame frame = new JFrame("Select File");

        int act = chooser.showOpenDialog(frame);
        if (act == JFileChooser.APPROVE_OPTION)
            loadFile(chooser.getSelectedFile());

    }

    public void onSave(ActionEvent e) {

        try {
            tabList.save();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void setLastOpenPath(String s) {

        try {

            PrintStream out = new PrintStream(new FileOutputStream(SETTINGS_PATH));
            out.println(s);
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public String getLastOpenPath() {

        String s = System.getProperty("user.home");
        try {

            Scanner scanner = new Scanner(new File(SETTINGS_PATH));
            s = scanner.nextLine();
            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return s;

    }

    public void loadFile(File f) {

        String title = f.getName();
        StringBuilder sBuilder = new StringBuilder();
        setLastOpenPath(f.getParent());

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

        TabPanel tabPanel = new TabPanel(f, sBuilder.toString(), title);
        tabList.addTab(tabPanel);

    }

    public void init() {

        JFrame frame = new JFrame(TITLE);
        // JPanel panel = new JPanel();

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
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocation((screenSize.width - frame.getWidth()) / 2, 0);
        frame.setVisible(true);

    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.isControlDown()) {
            switch (e.getKeyCode()) {
                case 'X':
                    break;
                case 'O':
                    onOpen(null);
                    break;
                case 'S':
                    onSave(null);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        new App().init();
    }

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {

    }

}
