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

    File file;

    public TabPanel(File path, String text, String title) {

        super();

        file = path;

        panels  = new JPanel[5];
        panelNames = new String[5];
        this.title = title;
        parser = new TextParser();
        parent = null;

        //Setup window panels
        panels[0] = new EditorPanel(this, path, text);
        panelNames[0] = "Editor";

        //@Debug Setup window with just a label for testing
        panels[1] = new JPanel();
        panels[1].add(new JLabel("A screen just for testing"));
        panelNames[1] = "TestLabel";

        panels[2] = new WordPanel(parser);
        panelNames[2] = "WordStats";
        panels[3] = new CharPanel(parser);
        panelNames[3] = "CharStats";
        panels[4] = new SentPanel(parser);
        panelNames[4] = "SentStats";

        windowPanel = new JPanel();
        cardLayout = new CardLayout();
        windowPanel.setLayout(cardLayout);

        this.setLayout(new BorderLayout());
        for(int i = 0; i < panelNames.length; i++) {
            windowPanel.add(panelNames[i], panels[i]);
        }
        this.add(windowPanel);

        parser.parse(text);
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
        parser.parse(getEditor().getTextArea().getText());
    }

    public EditorPanel getEditor() {
        return (EditorPanel) panels[0];
    }

    //Change the tab to show the screen with the specified name
    public void changeCard(String name) {
        cardLayout.show(windowPanel, name);
    }

    public boolean matchesPath(String path) {
        return file.getAbsolutePath().equals(path);
    }

}
