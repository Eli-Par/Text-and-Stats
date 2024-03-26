import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

import javax.swing.*;

public class FileTabList extends JPanel implements MouseListener{

    private JTabbedPane tabbedPane = new JTabbedPane();
    
    private ArrayList<TabPanel> tabs = new ArrayList<>();

    private String currentCardName = "Editor";

    private App app;

    public FileTabList(App a) {

        app = a;
        this.setLayout(new BorderLayout());
        this.add(tabbedPane);
        addKeyListener(app);
        tabbedPane.addKeyListener(app);

        tabbedPane.addMouseListener(this);

    }

    public void addTab(TabPanel tab) {

        tab.setParent(tabbedPane);
        tab.addKeyListener(app);
        tab.getEditor().getTextPane().setFont(getFont());
        tabs.add(tab);
        tabbedPane.addTab(tab.getTitle(), tab);
        tabbedPane.setSelectedComponent(tab);

        tab.changeCard(currentCardName);
    }

    public void setEditorFont(Font font) {

        setFont(font);
        app.opts.fontFamily = font.getFamily();
        app.opts.fontSize = font.getSize();

        for(Component c : tabbedPane.getComponents()) {
            TabPanel t = (TabPanel)c;
            t.getEditor().getTextPane().setFont(font);
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
            TabPanel tPanel = (TabPanel) panel;
            tPanel.save();
        }

    }

    public void close() {

        int ind = tabbedPane.getSelectedIndex();

        if(ind == -1)
            return;
        tabbedPane.remove(ind);

    }

    public EditorPanel getCurrentEditor() {
        Component component = tabbedPane.getSelectedComponent();
        if(component instanceof TabPanel panel) {
            return panel.getEditor();
        }
        return null;
        //throw new IllegalStateException("Selected component is " + component.getClass() + ", not TabPanel");
    }

    //Change all tabs to show the screen with the specified name
    public void changeCards(String name) {
        for(TabPanel tabPanel : tabs) {
            tabPanel.changeCard(name);
        }

        currentCardName = name;
    }

    //Called when the app is being closed
    public void closing() {
        for(TabPanel panel : tabs) {
            panel.getEditor().closing();
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
                rightMoveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        if(index < tabbedPane.getTabCount() - 1) {
                            int selectedIndex = tabbedPane.getSelectedIndex();

                            resetAllTabs();

                            TabPanel panel = tabs.remove(index);
                            tabs.add(index + 1, panel);

                            addAllTabs();

                            if(index == selectedIndex) tabbedPane.setSelectedIndex(index + 1);
                            else tabbedPane.setSelectedIndex(selectedIndex);
                        }
                    }
                });

                if(index >= tabbedPane.getTabCount() - 1) {
                    rightMoveButton.setEnabled(false);
                }

                popupMenu.add(rightMoveButton);

                JMenuItem leftMoveButton = new JMenuItem("Move Left");
                leftMoveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        if(index > 0) {
                            int selectedIndex = tabbedPane.getSelectedIndex();

                            resetAllTabs();

                            TabPanel panel = tabs.remove(index);
                            tabs.add(index - 1, panel);

                            addAllTabs();

                            if(index == selectedIndex) tabbedPane.setSelectedIndex(index - 1);
                            else tabbedPane.setSelectedIndex(selectedIndex);
                        }
                    }
                });

                if(index == 0) {
                    leftMoveButton.setEnabled(false);
                }

                popupMenu.add(leftMoveButton);

                JMenuItem deleteButton = new JMenuItem("Remove Tab");
                deleteButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        tabs.get(index).getEditor().closing();
                        tabbedPane.remove(index);
                        tabs.remove(index);
                    }
                });
                popupMenu.add(deleteButton);

                popupMenu.show(this, e.getX(), e.getY() + popupMenu.getHeight());
            }
        }
    }

    //Remove all tabs from the JTabbedPanel but do not remove them from the tabs ArrayList
    private void resetAllTabs() {
        while(tabbedPane.getTabCount() > 0) {
            tabbedPane.removeTabAt(0);
        }
    }

    //Add all tabs from the ArrayList back into the JTabbedPanel
    private void addAllTabs() {
        for(TabPanel panel : tabs) {
            tabbedPane.addTab((panel.getEditor().isSaved() ? "" : "*") + panel.getTitle(), panel);
        }
    }

    //If any tab has a matching absolute path, select it and return true, otherwise return false
    public boolean tryOpeningPath(String path) {

        for(int i = 0; i < tabs.size(); i++) {
            if(tabs.get(i).matchesPath(path)) {
                tabbedPane.setSelectedIndex(i);
                return true;
            }
        }

        return false;
    }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

}
