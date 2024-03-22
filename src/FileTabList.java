import java.awt.*;
import java.io.IOException;
import java.util.*;

import javax.swing.*;

public class FileTabList extends JPanel{

    private JTabbedPane tabbedPane = new JTabbedPane();
    
    private ArrayList<TabPanel> tabs = new ArrayList<>();

    public FileTabList() {
        this.setLayout(new BorderLayout());
        this.add(tabbedPane);
    }

    public void addTab(TabPanel tab) {
        tabs.add(tab);
        tabbedPane.addTab(tab.getTitle(), tab);
        tabbedPane.setSelectedComponent(tab);
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

}
