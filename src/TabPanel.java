import java.awt.*;

import javax.swing.*;

public class TabPanel extends JPanel {

    private String title;

    public TabPanel(String text, String title) {
        super();

        this.title = title;

        JTextArea area = new JTextArea();
        area.setLineWrap(true);
        area.setText(text);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        this.setLayout(new BorderLayout());
        this.add(scroll);
    }

    public String getTitle() {
        return title;
    }
}
