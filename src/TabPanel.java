import java.awt.*;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

public class TabPanel extends JPanel {

    private String title;

    JPanel panels[] = new JPanel[2];
    String panelNames[] = new String[2];

    private JPanel windowPanel;
    private CardLayout cardLayout;

    private JTabbedPane parent;

    public TabPanel(File path, String text, String title) {

        super();
        this.title = title;
        parent = null;

        //Setup window panels
        panels[0] = new EditorPanel(this, path, text);
        panelNames[0] = "Editor";

        //@Debug Setup window with just a label for testing
        panels[1] = new JPanel();
        panels[1].add(new JLabel("A screen just for testing"));
        panelNames[1] = "TestLabel";

        windowPanel = new JPanel();
        cardLayout = new CardLayout();
        windowPanel.setLayout(cardLayout);

        this.setLayout(new BorderLayout());
        for(int i = 0; i < panelNames.length; i++) {
            windowPanel.add(panelNames[i], panels[i]);
        }
        this.add(windowPanel);
    }

    public String getTitle() {
        return title;
    }

    public void setParent(JTabbedPane p) {
        parent = p;
    }

    @Override
    public void addKeyListener(KeyListener l) {

        super.addKeyListener(l);
        for (JPanel p : panels)
            p.addKeyListener(l);

    }

    public void notSavedIndicator() {

        int ind = parent.indexOfComponent(this);
        String title = parent.getTitleAt(ind);

        parent.setTitleAt(ind, "*" + title);

    }

    public void savedIndicator() {

        int ind = parent.indexOfComponent(this);
        String title = parent.getTitleAt(ind);

        parent.setTitleAt(ind,  title.substring(1));

    }

    public void save() throws IOException {
        ((EditorPanel)panels[0]).save();
    }

    //Called whenever the text area has its text changed
    public void textChanged() {
        //Parsing can go here
    }

    public EditorPanel getEditor() {
        return (EditorPanel) panels[0];
    }

    //Change the tab to show the screen with the specified name
    public void changeCard(String name) {
        cardLayout.show(windowPanel, name);
    }

}
