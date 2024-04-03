import javax.swing.*;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class App implements WindowListener {

    public static final String TITLE = "Text Buddy";

    public static final int WIDTH = 1000, HEIGHT = 650;

    private static FileTabList tabList;
    public MenuToolBar topMenu;
    public static FormattingToolBar formattingToolBar;

    public static boolean isDarkMode = true;

    public static JFrame frame;


    // public JToolBar createCardToolBar() {
    //     JToolBar toolBar = new JToolBar();
    //     toolBar.setFloatable(false);

    //     JPanel barPanel = new JPanel();
    //     barPanel.setLayout(new BoxLayout(barPanel, BoxLayout.X_AXIS));

    //     barPanel.add(new PageSwitchButton(tabList, "Editor", "Editor"));
    //     barPanel.add(Box.createHorizontalStrut(5));
    //     barPanel.add(new PageSwitchButton(tabList, "Word Stats", "WordStats"));
    //     barPanel.add(Box.createHorizontalStrut(5));
    //     barPanel.add(new PageSwitchButton(tabList, "Char Stats", "CharStats"));
    //     barPanel.add(Box.createHorizontalStrut(5));
    //     barPanel.add(new PageSwitchButton(tabList, "Sentence Stats", "SentStats"));

    //     toolBar.add(barPanel);

    //     return toolBar;
    // }

    public App() {

        tabList = new FileTabList(this);

    }

    public void init(String[]args) {

        frame = new JFrame(TITLE);
        ImageIcon icon = new ImageIcon("img/TextBuddyIcon.png");
        frame.setIconImage(icon.getImage());
        // JPanel panel = new JPanel();

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();

        formattingToolBar = new FormattingToolBar(tabList);
        topMenu = new MenuToolBar(tabList, formattingToolBar);
        tabList.addChangeListener(topMenu);
        
        topMenu.loadOptions();

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        topPanel.add(topMenu, BorderLayout.NORTH);
        topPanel.add(formattingToolBar, BorderLayout.SOUTH);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(tabList);

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

        FlatLaf.registerCustomDefaultsSource( "themes" );
        loadLight();

        new App().init(args);
    }

    public static void loadDark() {
        FlatDarkLaf.setup();
        isDarkMode = true;
        if(frame != null) SwingUtilities.updateComponentTreeUI(frame);
        if(formattingToolBar != null) formattingToolBar.updateToolbar();
    }

    public static void loadLight() {
        FlatLightLaf.setup();
        isDarkMode = false;
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
        topMenu.saveOptions();
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