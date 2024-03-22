import java.awt.*;
import java.io.IOException;
import java.util.*;

import javax.swing.*;

public class FileTabList extends JPanel{

    private JTabbedPane tabbedPane = new JTabbedPane();
    
    private ArrayList<TabPanel> tabs = new ArrayList<>();

    private App app;

    public FileTabList(App a) {

        app = a;
        this.setLayout(new BorderLayout());
        this.add(tabbedPane);
        tabbedPane.addKeyListener(app);

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

}
