import java.awt.*;

import javax.swing.*;

public class TabPanel extends JPanel {

    private String title;

    JPanel panels[] = new JPanel[1];
    String panelNames[] = new String[1];

    private JPanel windowPanel;

    public TabPanel(String text, String title) {
        super();

        this.title = title;

        //Setup window panels
        panels[0] = new EditorPanel(this, text);
        panelNames[0] = "Editor";

        windowPanel = new JPanel();
        windowPanel.setLayout(new CardLayout());

        this.setLayout(new BorderLayout());
        for(int i = 0; i < panelNames.length; i++) {
            windowPanel.add(panelNames[i], panels[i]);
        }
        this.add(windowPanel);
    }

    public String getTitle() {
        return title;
    }

    //Called whenever the text area has its text changed
    public void textChanged() {
        //Parsing can go here
    }

}
