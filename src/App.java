import javax.swing.*;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class App implements WindowListener {

    public static final String TITLE = "Text Buddy";

    public static final int WIDTH = 1000, HEIGHT = 650;

    private static FileTabList tabList;
    public MenuToolBar topMenu;
    public static FormattingToolBar formattingToolBar;

    public static boolean isDarkMode = true;

    public static JFrame frame;

    public static UserOptions opts = new UserOptions();

    public App() {

        loadOptions();

        FlatLaf.registerCustomDefaultsSource( "themes" );
        if(opts.isDarkMode) loadDark();
        else loadLight();

        tabList = new FileTabList(this, opts.useAutoSave);

    }

    public void loadOptions() {

        opts.lastOpenLocation = System.getProperty("user.home");
        opts.fontSize = 12;

        try {

            Scanner scanner = new Scanner(new File(MenuToolBar.SETTINGS_PATH));

            opts.lastOpenLocation = scanner.nextLine();
            opts.fontFamily = scanner.nextLine();
            opts.fontSize = scanner.nextInt();
            opts.isDarkMode = scanner.nextBoolean();
            opts.useAutoSave = scanner.nextBoolean();

            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void saveOptions() {

        try {

            PrintStream out = new PrintStream(new FileOutputStream(MenuToolBar.SETTINGS_PATH));

            out.println(opts.lastOpenLocation);
            out.println(opts.fontFamily);
            out.println(opts.fontSize);
            out.println(opts.isDarkMode);
            out.println(opts.useAutoSave);

            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void init(String[]args) {

        frame = new JFrame(TITLE);
        frame.setMinimumSize(new Dimension(775, 300));
        ImageIcon icon = new ImageIcon("img/TextBuddyIcon.png");
        frame.setIconImage(icon.getImage());
        // JPanel panel = new JPanel();

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();

        formattingToolBar = new FormattingToolBar(tabList);
        topMenu = new MenuToolBar(opts, tabList, formattingToolBar);
        tabList.addChangeListener(topMenu);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        topPanel.add(topMenu, BorderLayout.NORTH);
        topPanel.add(formattingToolBar, BorderLayout.SOUTH);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(tabList);
        tabList.setFont(new Font(opts.fontFamily, Font.PLAIN, opts.fontSize));

        // panel.setSize(WIDTH, HEIGHT);
        // panel.setPreferredSize(panel.getSize());
        // //frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(this);
        //frame.pack();
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocation((screenSize.width - frame.getWidth()) / 2, 0);
        frame.setVisible(true);

        for(String arg : args)
            topMenu.loadFile(new File(arg));

    }

    public static void main(String[] args) {

        new App().init(args);
    }

    public static void loadDark() {
        FlatDarkLaf.setup();
        isDarkMode = true;
        opts.isDarkMode = true;
        if(frame != null) SwingUtilities.updateComponentTreeUI(frame);
        if(formattingToolBar != null) formattingToolBar.updateToolbar();
    }

    public static void loadLight() {
        FlatLightLaf.setup();
        isDarkMode = false;
        opts.isDarkMode = false;
        if(frame != null) SwingUtilities.updateComponentTreeUI(frame);
        if(formattingToolBar != null) formattingToolBar.updateToolbar();
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