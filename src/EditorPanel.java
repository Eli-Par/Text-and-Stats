import java.awt.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class EditorPanel extends JPanel implements DocumentListener{

    private TabPanel tab;

    private JTextArea area;

    public EditorPanel(TabPanel tab, String text) {
        super();

        this.tab = tab;

        this.setLayout(new BorderLayout());

        area = new JTextArea();
        area.setLineWrap(true);
        area.setText(text);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(area);

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(scroll);

        area.getDocument().addDocumentListener(this);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        tab.textChanged();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        tab.textChanged();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        //Do nothing
    }
}
