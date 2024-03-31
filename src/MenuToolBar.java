import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import static util.GUI.boolQuery;
import static util.algobase.lower_bound;

import java.awt.*;

import javax.swing.*;


public class MenuToolBar extends JMenuBar {
    
    private static FileTabList tabList;
    public static final String SETTINGS_PATH = System.getProperty("user.home") + "/.comp2800-settings";
    public static Font MENU_FONT = new Font("Tahoma", Font.PLAIN, 16);

    private FormattingToolBar formattingToolBar;

    UserOptions opts;
    public MenuToolBar(FileTabList tl, FormattingToolBar formattingToolBar) {
        tabList = tl;
        this.formattingToolBar = formattingToolBar;
        opts = new UserOptions();

        JMenu fileMenu = new JMenu("File");
        JMenu vMenu = new JMenu("View");
        JMenu editMenu = new JMenu("Edit");

        JMenuItem save = new JMenuItem("Save");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem saveAll = new JMenuItem("Save All");
        JMenuItem saveAs = new JMenuItem("Save As");
        JMenuItem ne = new JMenuItem("New Text File");

        JMenuItem font = new JMenuItem("Font");
        JMenuItem zoomIn = new JMenuItem("Zoom In");
        JMenuItem zoomOut = new JMenuItem("Zoom Out");
        JMenuItem zoomDefault = new JMenuItem("Zoom Reset");
        JMenuItem moveLeft = new JMenuItem("Move Tab Left");
        JMenuItem moveRight = new JMenuItem("Move Tab Right");

        JMenuItem find = new JMenuItem("Find");
        JMenuItem replaceFirst = new JMenuItem("Replace First");
        JMenuItem replaceAll = new JMenuItem("Replace All");
        JMenuItem undo = new JMenuItem("Undo");
        JMenuItem redo = new JMenuItem("Redo");

        save.setAccelerator(KeyStroke.getKeyStroke("control S"));
        open.setAccelerator(KeyStroke.getKeyStroke("control O"));
        ne.setAccelerator(KeyStroke.getKeyStroke("control N"));
        saveAll.setAccelerator(KeyStroke.getKeyStroke("control shift S"));

        zoomIn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, InputEvent.CTRL_DOWN_MASK));
        zoomOut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_DOWN_MASK));
        zoomDefault.setAccelerator(KeyStroke.getKeyStroke("control 0"));

        moveRight.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK));
        moveLeft.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_DOWN_MASK));

        find.setAccelerator(KeyStroke.getKeyStroke("control F"));
        replaceAll.setAccelerator(KeyStroke.getKeyStroke("control R"));
        undo.setAccelerator(KeyStroke.getKeyStroke("control Z"));
        redo.setAccelerator(KeyStroke.getKeyStroke("control Y"));

        fileMenu.setFont(MENU_FONT);
        fileMenu.add(ne);
        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(saveAll);
        fileMenu.add(saveAs);

        vMenu.setFont(MENU_FONT);
        vMenu.add(font);
        vMenu.add(zoomIn);
        vMenu.add(zoomOut);
        vMenu.add(zoomDefault);
        vMenu.add(moveLeft);
        vMenu.add(moveRight);

        editMenu.setFont(MENU_FONT);
        editMenu.add(find);
        editMenu.add(replaceFirst);
        editMenu.add(replaceAll);
        editMenu.add(undo);
        editMenu.add(redo);
        
        this.add(fileMenu);
        this.add(editMenu);
        this.add(vMenu);

        open.addActionListener(this::onOpen);
        save.addActionListener(this::onSave);
        saveAll.addActionListener(this::onSaveAll);
        ne.addActionListener(this::onNew);
        saveAs.addActionListener(this::onSaveAs);
        font.addActionListener(this::fontSetter);

        find.addActionListener(this::onFind);
        replaceFirst.addActionListener(this::replaceFirst);
        replaceAll.addActionListener(this::onReplaceAll);
        undo.addActionListener(this::onUndo);
        redo.addActionListener(this::onRedo);

        zoomIn.addActionListener(this::onZoomIn);
        zoomOut.addActionListener(this::onZoomOut);
        zoomDefault.addActionListener(this::onZoomReset);
        moveLeft.addActionListener(e -> tabList.moveLeft());
        moveRight.addActionListener(e -> tabList.moveRight());
        
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

    public void onOpen(ActionEvent e) {

        File[]farr = fileSelector( "Open");

        if(farr == null) return;

        for(File f : farr) {
            if(!checkOpenedFile(f, false))
                continue;
            //Load the file only if it is not already open
            if(!tabList.tryOpeningPath(f.getAbsolutePath())) {
                loadFile(f);
            }
        }

    }

    public void onNew(ActionEvent e) {

        File[]farr = fileSelector( "New");

        if(farr == null) return;

        for(File f : farr) {
            if(!checkOpenedFile(f, true))
                return;

            if(!tabList.tryOpeningPath(f.getAbsolutePath())) {
                loadFile(f);
            }
        }

    }

    public void fontSetter(ActionEvent e) {
        new FontSetter(tabList);
    }
    public void onFind(ActionEvent e){
        JTextField tf = new JTextField();
        int option = JOptionPane.showConfirmDialog(null, tf, "Find", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String text = tf.getText();
            tabList.getCurrentEditor().find(text);
        }
    }

    public void onReplaceAll(ActionEvent e){
        JTextField findTf = new JTextField();
        JTextField replaceTf = new JTextField();
        Object[] msg = {"Find: ", findTf, "Replace with: ", replaceTf};
        int option = JOptionPane.showConfirmDialog(null, msg , "Replace All", JOptionPane.OK_CANCEL_OPTION);
        if(option == JOptionPane.OK_OPTION){
            tabList.getCurrentEditor().replaceAll(findTf.getText(), replaceTf.getText());
        }
    }

    public void onFindAndReplace(ActionEvent e) {

    }

    public void onUndo(ActionEvent e){
        tabList.getCurrentEditor().undo();
    }

    public void onRedo(ActionEvent e){
        tabList.getCurrentEditor().redo();
    }

    public void replaceFirst(ActionEvent e){
        JTextField findTf = new JTextField();
        JTextField replaceTf = new JTextField();
        Object[] msg = {"Find: ", findTf, "Replace with: ", replaceTf};
        int option = JOptionPane.showConfirmDialog(null, msg, "Replace First Instance", JOptionPane.OK_CANCEL_OPTION);
        if(option == JOptionPane.OK_OPTION){
            tabList.getCurrentEditor().replaceFirst(findTf.getText(), replaceTf.getText());
        }
    }

    public void onZoomIn(ActionEvent e) {

        var editor = tabList.getCurrentEditor();
        if(editor == null)
            return;

        JTextPane area = editor.getTextPane();
        Font f = area.getFont();
        int ind = lower_bound(FontSetter.FONT_SIZES, f.getSize()) + 1;

        if(ind < FontSetter.FONT_SIZES.length)
            area.setFont(new Font(f.getFamily(), Font.PLAIN, FontSetter.FONT_SIZES[ind]));

    }

    public void onZoomOut(ActionEvent e) {

        var editor = tabList.getCurrentEditor();
        if(editor == null)
            return;

        JTextPane area = editor.getTextPane();
        Font f = area.getFont();
        int ind = lower_bound(FontSetter.FONT_SIZES, f.getSize()) - 1;

        if(ind >= 0)
            area.setFont(new Font(f.getFamily(), Font.PLAIN, FontSetter.FONT_SIZES[ind]));

    }

    public void onZoomReset(ActionEvent e) {

        var editor = tabList.getCurrentEditor();
        if(editor == null)
            return;

        editor.getTextPane().setFont(tabList.getFont());

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

    public void onSaveAs(ActionEvent e) {

        EditorPanel ed = tabList.getCurrentEditor();
        if(ed == null)
            return;

        File[]farr = fileSelector( "Save As");
        if(farr == null)
            return;

        for(File f : farr) {

            if (f.exists()) {

                boolean ans = boolQuery(f.getName() + " already exists, do you want to overwrite it?");

                if (ans)
                    ans = boolQuery("Triple check: " + f.getName() + " will be lost forever (a long time)!");

                if (!ans)
                    continue;

            }

            TabPanel tab = tabList.getTabByPath(f.getAbsolutePath());
            if(tab != null) {
                tab.getEditor().getTextPane().setText(ed.getTextPane().getText());
                ed = tab.getEditor();
                tabList.getTabbedPane().setSelectedComponent(tab);
            } else {
                ed.setPath(f);
                tabList.getTabbedPane().setTitleAt(tabList.getTabbedPane().getSelectedIndex(), f.getName());
            }

            try {
                ed.save();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

    }
    public boolean checkOpenedFile(File file, boolean b) {
        
        if (file == null)
            return false;
        
        boolean cont;

        if (file.exists() == b) {

            if(b)
                cont = boolQuery(file.getParent() + " already exists. Do you want to open it?");
            else
                cont = boolQuery(file.getPath() + " does not exist. Do you want to create it?");

            if (!cont)
                return false;

            if (!b) {
                try {
                    new FileOutputStream(file).close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return true;

        }

        return true;

    }
    public File[]fileSelector(String title) {

        JFileChooser chooser = new JFileChooser(opts.lastOpenLocation);
        JFrame frame = new JFrame("Select File");

        chooser.setMultiSelectionEnabled(true);
        chooser.setDialogTitle(title);

        int act = chooser.showOpenDialog(frame);
        if (act == JFileChooser.APPROVE_OPTION)
            return chooser.getSelectedFiles();

        return null;

    }
    public void loadFile(File f) {

        String title = f.getName();
        opts.lastOpenLocation = f.getParent();

        AsyncTabFactory factory = new AsyncTabFactory(formattingToolBar, f, title) {
            @Override
            protected void tabCreated(TabPanel panel) {
                tabList.addTab(panel);
            }
        };

        factory.execute();

    }
    
}
