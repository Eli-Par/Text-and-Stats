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

    public EditorPanel getCurrentEditor() {
        Component component = tabbedPane.getSelectedComponent();
        if(component instanceof TabPanel) {
            return ((TabPanel) component).getEditor();
        }

        System.err.println("Tab list contained non TabPanel component");
        return null;
    }

}
