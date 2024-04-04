import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.text.Highlighter;

public class FileTabList extends JPanel implements MouseListener{

    private JTabbedPane tabbedPane = new JTabbedPane();

    private String currentCardName = "Editor";

    private App app;

    public static boolean useAutoSave = false;

    public FileTabList(App a, boolean autoSave) {

        useAutoSave = autoSave;

        app = a;
        this.setLayout(new BorderLayout());
        this.add(tabbedPane);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        tabbedPane.addMouseListener(this);

    }

    public void addTab(TabPanel tab) {

        tab.setParent(tabbedPane);
        tab.getEditor().getTextPane().setFont(getFont());
        tabbedPane.addTab(tab.getTitle(), tab);
        tabbedPane.setSelectedComponent(tab);

        tab.changeCard(currentCardName);
    }

    public void setEditorFont(Font font) {

        setFont(font);
        app.topMenu.opts.fontFamily = font.getFamily();
        app.topMenu.opts.fontSize = font.getSize();

        for(Component c : tabbedPane.getComponents()) {
            if(c instanceof TabPanel t) {
                t.getEditor().getTextPane().setFont(font);
            }
        }

    }

    public void toggleAutosave() {

        useAutoSave = !useAutoSave;
        App.opts.useAutoSave = useAutoSave;

        if(!useAutoSave) return;

        for (Component panel : tabbedPane.getComponents()) {
            if (panel instanceof TabPanel tPanel) {

                try {
                    tPanel.save();
                } catch (IOException e) {
                    //@PRINT e.printStackTrace();
                }

            }
        }

    }

    public void save(int index) throws IOException {
        TabPanel t = (TabPanel) tabbedPane.getComponentAt(index);
        t.save();
    }

    public void save() throws IOException {
        save(tabbedPane.getSelectedIndex());
    }

    public void saveAll() throws IOException {

        for (Component panel : tabbedPane.getComponents()) {
            if(panel instanceof TabPanel tPanel) {
                tPanel.save();
            }
        }

    }

    public void close() {

        int ind = tabbedPane.getSelectedIndex();

        if(ind == -1)
            return;
        tabbedPane.remove(ind);

    }

    public void closeExisting(String path) {
        for(Component component : tabbedPane.getComponents()) {
            if(component instanceof TabPanel tab) {
                if(tab.matchesPath(path)) {
                    tabbedPane.remove(component);
                }
            }
        }
    }

    public TabPanel getTabByPath(String path) {

        for (Component component : tabbedPane.getComponents()) {
            if (component instanceof TabPanel tab) {
                if (tab.matchesPath(path))
                    return tab;
            }
        }

        return null;

    }

    public EditorPanel getCurrentEditor() {
        Component component = tabbedPane.getSelectedComponent();
        if(component instanceof TabPanel panel) {
            return panel.getEditor();
        }
        return null;
        //throw new IllegalStateException("Selected component is " + component.getClass() + ", not TabPanel");
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public void moveTab(int ind, int cnt) {

        if (ind < 0)
            return;

        int tot = tabbedPane.getTabCount();
        int dest = ((ind + cnt) % tot + tot) % tot;
        boolean sel = ind == tabbedPane.getSelectedIndex();
        Component c = tabbedPane.getComponentAt(ind);
        tabbedPane.remove(ind);
        tabbedPane.insertTab(c.getName(), null, c, null, dest);

        if (sel)
            tabbedPane.setSelectedIndex(dest);

    }

    public void moveLeft() {
        int ind = tabbedPane.getSelectedIndex();
        moveTab(ind, -1);
    }

    public void moveRight() {
        int ind = tabbedPane.getSelectedIndex();
        moveTab(ind, 1);
    }

    //Change all tabs to show the screen with the specified name
    public void changeCards(String name) {

        for(Component component : tabbedPane.getComponents()) {
            if(component instanceof TabPanel panel) {
                panel.changeCard(name);
            }
        }

        currentCardName = name;
    }

    //Called when the app is being closed
    public void closing() {

        for(Component component : tabbedPane.getComponents()) {
            if(component instanceof TabPanel panel) {
                panel.getEditor().closing();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) {
        //Right click on the JTabbedPane
        if(e.getButton() == MouseEvent.BUTTON3) {
            //Get the tab index that they clicked and if it is not -1 (outside of a tab) it is valid
            int index = tabbedPane.getUI().tabForCoordinate(tabbedPane, e.getX(), e.getY());
            if(index != -1) {
                JPopupMenu popupMenu = new JPopupMenu();

                JMenuItem rightMoveButton = new JMenuItem("Move Right");
                rightMoveButton.addActionListener(event -> moveRight());

                popupMenu.add(rightMoveButton);

                JMenuItem leftMoveButton = new JMenuItem("Move Left");
                leftMoveButton.addActionListener(event -> moveLeft());

                popupMenu.add(leftMoveButton);

                JMenuItem deleteButton = new JMenuItem("Remove Tab");
                deleteButton.addActionListener(event -> {
                    if(tabbedPane.getComponentAt(index) instanceof TabPanel panel) {
                        panel.getEditor().closing();
                        tabbedPane.remove(index);
                    }
                    
                });
                popupMenu.add(deleteButton);

                popupMenu.show(this, e.getX(), e.getY() + popupMenu.getHeight());
            }
        }
    }

    //If any tab has a matching absolute path, select it and return true, otherwise return false
    public boolean tryOpeningPath(String path) {

        Component components[] = tabbedPane.getComponents();
        for(int i = 0; i < components.length; i++) {
            if(components[i] instanceof TabPanel tab) {
                if(tab.matchesPath(path)) {
                    tabbedPane.setSelectedIndex(i);
                    return true;
                }
            }
            
        }

        return false;
    }

    public void removeAllHighlights() {
        for(Component component : tabbedPane.getComponents()) {
            if(component instanceof TabPanel panel) {
                Highlighter h = panel.getEditor().getTextPane().getHighlighter();
                h.removeAllHighlights();
            }
        }
    }

    public void addChangeListener(ChangeListener listener) {
        tabbedPane.addChangeListener(listener);
    }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

}
