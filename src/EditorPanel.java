import java.awt.*;
import java.awt.event.KeyListener;
import java.io.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.inet.jortho.FileUserDictionary;
import com.inet.jortho.SpellChecker;

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
        area.setWrapStyleWord(true);
        area.setText(text);

        // JPanel panel = new JPanel();
        // panel.setLayout(new BorderLayout());
        // panel.add(area);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(scroll);

        area.getDocument().addDocumentListener(this);

        SpellChecker.setUserDictionaryProvider(new FileUserDictionary());      
        SpellChecker.registerDictionaries(this.getClass().getResource("/dictionary"), "en");
        SpellChecker.register(area, true, false, true, true);

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

    //Is called before the tab the editor is in is closed
    public void closing() {
        if(!saved) {
            String[] options = {"Save", "Don't Save"}; 
            int result = JOptionPane.showOptionDialog(
               this,
               "Are you sure you don't want to save " + path.getName() + "?", 
               "Want to save?",            
               JOptionPane.YES_NO_OPTION,
               JOptionPane.WARNING_MESSAGE,
               null,
               options,
               options[0]
            );
            if(result == JOptionPane.YES_OPTION) {
                try {
                    save();
                }
                catch(IOException exception) {
                    JOptionPane.showMessageDialog(this, "There was a problem saving " + path.getName(), "Problem saving file", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public boolean isSaved() {
        return saved;
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
