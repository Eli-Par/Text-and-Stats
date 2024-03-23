import java.awt.*;
import java.awt.event.KeyListener;
import java.io.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class EditorPanel extends JPanel implements DocumentListener{

    private TabPanel tab;

    private JTextArea area;

    private File path;

    private boolean saved;

    public EditorPanel(TabPanel tab, File p, String text) {

        super();

        this.tab = tab;
        path = p;
        saved = true;

        this.setLayout(new BorderLayout());

        area = new JTextArea();
        area.setLineWrap(true);
        area.setText(text);

        // JPanel panel = new JPanel();
        // panel.setLayout(new BorderLayout());
        // panel.add(area);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(scroll);

        area.getDocument().addDocumentListener(this);

    }

    public void save() throws IOException {

        PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream(path)));

        if (!saved) {
            saved = true;
            tab.savedIndicator();
        }

        out.print(area.getText());
        out.close();

    }

    @Override
    public void insertUpdate(DocumentEvent e) {

        tab.textChanged();
        if (saved) {
            tab.notSavedIndicator();
            saved = false;
        }

    }

    @Override
    public void removeUpdate(DocumentEvent e) {

        tab.textChanged();
        if (saved) {
            tab.notSavedIndicator();
            saved = false;
        }

    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        //Do nothing
    }

    @Override
    public void addKeyListener(KeyListener l) {
        super.addKeyListener(l);
        area.addKeyListener(l);
    }

    public JTextArea getTextArea() {
        return area;
    }

}
