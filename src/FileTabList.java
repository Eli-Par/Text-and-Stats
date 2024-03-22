import java.awt.*;
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

}
