import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Scanner;
import static util.algobase.*;

public class App implements KeyListener, WindowListener {

    public static final String TITLE = "Editor";

    public static final String SETTINGS_PATH = System.getProperty("user.home") + "/.comp2800-settings";

    public static final int WIDTH = 1000, HEIGHT = 650;

    private static FileTabList tabList;

    public static Font MENU_FONT = new Font("Tahoma", Font.PLAIN, 30);

    UserOptions opts;

    public JToolBar createCardToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JPanel barPanel = new JPanel();
        barPanel.setLayout(new BoxLayout(barPanel, BoxLayout.X_AXIS));

        barPanel.add(new PageSwitchButton(tabList, "Editor", "Editor"));
        barPanel.add(Box.createHorizontalStrut(5));
        barPanel.add(new PageSwitchButton(tabList, "Label", "TestLabel"));
        barPanel.add(Box.createHorizontalStrut(5));
        barPanel.add(new PageSwitchButton(tabList, "Word Stats", "WordStats"));
        barPanel.add(Box.createHorizontalStrut(5));
        barPanel.add(new PageSwitchButton(tabList, "Char Stats", "CharStats"));
        barPanel.add(Box.createHorizontalStrut(5));
        barPanel.add(new PageSwitchButton(tabList, "Sent Stats", "SentStats"));

        toolBar.add(barPanel);

        return toolBar;
    }

    public void addMenuItems(JMenuBar bar) {

        JMenu fileMenu = new JMenu("File");
        JMenu vMenu = new JMenu("View");

        JMenuItem save = new JMenuItem("Save");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem saveAll = new JMenuItem("Save All");

        JMenuItem font = new JMenuItem("Font");
        JMenuItem zoomIn = new JMenuItem("Zoom In");
        JMenuItem zoomOut = new JMenuItem("Zoom Out");
        JMenuItem zoom = new JMenuItem("Zoom Reset");

        bar.add(fileMenu);
        bar.add(vMenu);

        fileMenu.setFont(MENU_FONT);
        fileMenu.add(save);
        fileMenu.add(saveAll);
        fileMenu.add(open);

        vMenu.setFont(MENU_FONT);
        vMenu.add(font);
        vMenu.add(zoomIn);
        vMenu.add(zoomOut);
        vMenu.add(zoom);

        open.addActionListener(this::onOpen);
        save.addActionListener(this::onSave);
        saveAll.addActionListener(this::onSaveAll);
        font.addActionListener(this::fontSetter);
        zoomIn.addActionListener(this::onZoomIn);
        zoomOut.addActionListener(this::onZoomOut);
        zoom.addActionListener(this::onZoomReset);

    }

    public App() {

        tabList = new FileTabList(this);
        opts = new UserOptions();

        loadOptions();
        tabList.setEditorFont(new Font(opts.fontFamily, Font.PLAIN, opts.fontSize));

    }

    public void onOpen(ActionEvent e) {

        JFileChooser chooser = new JFileChooser(opts.lastOpenLocation);
        JFrame frame = new JFrame("Select File");

        int act = chooser.showOpenDialog(frame);
        if (act == JFileChooser.APPROVE_OPTION) {
            //Load the file only if it is not already open
            if(!tabList.tryOpeningPath(chooser.getSelectedFile().getAbsolutePath())) {
                loadFile(chooser.getSelectedFile());
            }
        }

    }

    public void fontSetter(ActionEvent e) {
        new FontSetter(tabList);
    }

    public void onZoomIn(ActionEvent e) {

        var editor = tabList.getCurrentEditor();
        if(editor == null)
            return;

        JTextArea area = editor.getTextArea();
        Font f = area.getFont();
        int ind = lower_bound(FontSetter.FONT_SIZES, f.getSize()) + 1;

        if(ind < FontSetter.FONT_SIZES.length)
            area.setFont(new Font(f.getFamily(), Font.PLAIN, FontSetter.FONT_SIZES[ind]));

    }

    public void onZoomOut(ActionEvent e) {

        var editor = tabList.getCurrentEditor();
        if(editor == null)
            return;

        JTextArea area = editor.getTextArea();
        Font f = area.getFont();
        int ind = lower_bound(FontSetter.FONT_SIZES, f.getSize()) - 1;

        if(ind >= 0)
            area.setFont(new Font(f.getFamily(), Font.PLAIN, FontSetter.FONT_SIZES[ind]));

    }

    public void onZoomReset(ActionEvent e) {

        var editor = tabList.getCurrentEditor();
        if(editor == null)
            return;

        editor.getTextArea().setFont(tabList.getFont());

    }

    public void onSave(ActionEvent e) {

        try {
            tabList.save();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void onSaveAll(ActionEvent e) {

        try {
            tabList.saveAll();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void saveOptions() {

        try {

            PrintStream out = new PrintStream(new FileOutputStream(SETTINGS_PATH));

            out.println(opts.lastOpenLocation);
            out.println(opts.fontFamily);
            out.println(opts.fontSize);

            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void loadOptions() {

        opts.lastOpenLocation = System.getProperty("user.home");
        opts.fontSize = 12;

        try {

            Scanner scanner = new Scanner(new File(SETTINGS_PATH));

            opts.lastOpenLocation = scanner.nextLine();
            opts.fontFamily = scanner.nextLine();
            opts.fontSize = scanner.nextInt();

            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void loadFile(File f) {

        String title = f.getName();
        StringBuilder sBuilder = new StringBuilder();
        opts.lastOpenLocation = f.getParent();

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

        JToolBar toolBar = createCardToolBar();

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        topPanel.add(topMenu, BorderLayout.NORTH);
        topPanel.add(toolBar, BorderLayout.SOUTH);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(tabList);
        frame.addKeyListener(this);

        // panel.setSize(WIDTH, HEIGHT);
        // panel.setPreferredSize(panel.getSize());
        // //frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(this);
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
                case 107:
                    onZoomIn(null);
                    break;
                case 109:
                case KeyEvent.VK_MINUS:
                    onZoomOut(null);
                    break;
                case '0':
                    onZoomReset(null);
                    break;
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

    /**
     * Invoked the first time a window is made visible.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowOpened(WindowEvent e) {

    }

    /**
     * Invoked when the user attempts to close the window
     * from the window's system menu.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowClosing(WindowEvent e) {
        saveOptions();
        tabList.closing();
    }

    /**
     * Invoked when a window has been closed as the result
     * of calling dispose on the window.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowClosed(WindowEvent e) {

    }

    /**
     * Invoked when a window is changed from a normal to a
     * minimized state. For many platforms, a minimized window
     * is displayed as the icon specified in the window's
     * iconImage property.
     *
     * @param e the event to be processed
     * @see Frame#setIconImage
     */
    @Override
    public void windowIconified(WindowEvent e) {

    }

    /**
     * Invoked when a window is changed from a minimized
     * to a normal state.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    /**
     * Invoked when the Window is set to be the active Window. Only a Frame or
     * a Dialog can be the active Window. The native windowing system may
     * denote the active Window or its children with special decorations, such
     * as a highlighted title bar. The active Window is always either the
     * focused Window, or the first Frame or Dialog that is an owner of the
     * focused Window.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowActivated(WindowEvent e) {

    }

    /**
     * Invoked when a Window is no longer the active Window. Only a Frame or a
     * Dialog can be the active Window. The native windowing system may denote
     * the active Window or its children with special decorations, such as a
     * highlighted title bar. The active Window is always either the focused
     * Window, or the first Frame or Dialog that is an owner of the focused
     * Window.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
