import java.awt.*;
import java.io.IOException;
import java.util.*;

import javax.swing.*;

public class FileTabList extends JPanel{

    private JTabbedPane tabbedPane = new JTabbedPane();
    
    private ArrayList<TabPanel> tabs = new ArrayList<>();

    private String currentCardName = "Editor";

    private App app;

    public FileTabList(App a) {

        app = a;
        this.setLayout(new BorderLayout());
        this.add(tabbedPane);
        tabbedPane.addKeyListener(app);

    }

    public void addTab(TabPanel tab) {

        tab.setParent(tabbedPane);
        tab.addKeyListener(app);
        tabs.add(tab);
        tabbedPane.addTab(tab.getTitle(), tab);
        tabbedPane.setSelectedComponent(tab);

        tab.changeCard(currentCardName);
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
        throw new IllegalStateException("Selected component is " + component.getClass() + ", not TabPanel");
    }

    //Change all tabs to show the screen with the specified name
    public void changeCards(String name) {
        for(TabPanel tabPanel : tabs) {
            tabPanel.changeCard(name);
        }

        currentCardName = name;
    }

}
