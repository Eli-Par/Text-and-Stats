import java.awt.*;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import parsing.*;

public class TabPanel extends JPanel {

    private String title;

    JPanel panels[];
    String panelNames[];
    private JPanel windowPanel;
    private CardLayout cardLayout;

    private JTabbedPane parent;

    public Parser parser;

    public TabPanel(File path, String title) {

        super();

        panels  = new JPanel[4];
        panelNames = new String[4];
        this.title = title;
        setName(this.title);
        parser = new TextParser();
        parent = null;

        //Setup window panels
        panels[0] = new EditorPanel(this, path);
        panelNames[0] = "Editor";

        panels[1] = new WordPanel(parser);
        panelNames[1] = "WordStats";
        panels[2] = new CharPanel(parser);
        panelNames[2] = "CharStats";
        panels[3] = new SentPanel(parser);
        panelNames[3] = "SentStats";

        windowPanel = new JPanel();
        cardLayout = new CardLayout();
        windowPanel.setLayout(cardLayout);

        this.setLayout(new BorderLayout());
        for(int i = 0; i < panelNames.length; i++) {
            windowPanel.add(panelNames[i], panels[i]);
        }
        this.add(windowPanel);

        textChanged();
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
        if(getEditor() == null) {
            System.out.println("No editor found");
            return;
        }
        parser.parse(getEditor().getPlainText());
    }

    public EditorPanel getEditor() {
        return (EditorPanel) panels[0];
    }

    //Change the tab to show the screen with the specified name
    public void changeCard(String name) {
        cardLayout.show(windowPanel, name);
    }

    public boolean matchesPath(String path) {
        return getEditor().getPath().getAbsolutePath().equals(path);
    }

}
