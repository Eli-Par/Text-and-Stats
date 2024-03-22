import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

public class TabPanel extends JPanel {

    private String title;

    JPanel panels[] = new JPanel[1];
    String panelNames[] = new String[1];

    private JPanel windowPanel;
    private CardLayout cardLayout;

    public TabPanel(File path, String text, String title) {
        super();

        this.title = title;

        //Setup window panels
        panels[0] = new EditorPanel(this, path, text);
        panelNames[0] = "Editor";

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
