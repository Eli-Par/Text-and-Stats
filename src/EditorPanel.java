import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.charset.Charset;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import com.inet.jortho.FileUserDictionary;
import com.inet.jortho.SpellChecker;

public class EditorPanel extends JPanel implements DocumentListener{

    private TabPanel tab;

    private JTextArea area;

    private File path;

    private boolean saved;
    private UndoManager undoer;

    public EditorPanel(TabPanel tab, File p) {

        super();

        StringBuilder sBuilder = new StringBuilder();
        try {

            InputStreamReader  in = new InputStreamReader (new FileInputStream(p), Charset.forName("UTF-8"));
            char[]fileData = new char[1024];
            int cnt;

            while ((cnt = in.read(fileData)) > 0)
                sBuilder.append(new String(fileData, 0, cnt));

            in.close();

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String text = sBuilder.toString();

        this.tab = tab;
        path = p;
        saved = true;

        this.setLayout(new BorderLayout());

        area = new JTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setText(text);
        area.setCaretPosition(0);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(scroll);

        area.getDocument().addDocumentListener(this);

        SpellChecker.setUserDictionaryProvider(new FileUserDictionary());      
        SpellChecker.registerDictionaries(this.getClass().getResource("/dictionary"), "en");
        SpellChecker.register(area, true, false, true, true);

        undoer = new UndoManager();
        area.getDocument().addUndoableEditListener(undoer);

    }

    public void setPath(File p) {
        path = p;
    }

    public File getPath() {
        return path;
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

    public void find(String text){
        // get the text
        String content = area.getText();
        // create a highlighter to highlight the text
        Highlighter h = area.getHighlighter();
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.CYAN);
        h.removeAllHighlights();

        // loop through all occurrences and highlight them
        int i = content.indexOf(text);
        boolean caretNotSet = true;
        if((i > area.getCaretPosition() || i == 0 && area.getCaretPosition() == 0) && caretNotSet) 
        {
            area.setCaretPosition(i);
            caretNotSet = false;
        }
        while(i != -1){
            try{
                h.addHighlight(i, i+text.length(), painter);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            i = content.indexOf(text, i+1);
            if(i > area.getCaretPosition() && caretNotSet) 
        {
            area.setCaretPosition(i);
            caretNotSet = false;
        }
        }

        // delete highlighted text when mouse is clicked
        area.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                h.removeAllHighlights();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                h.removeAllHighlights();
            }
        });
    }

    public void replaceFirst(String find, String replace){
        // get the text
        String content = area.getText();

        // find the first occurrence of find and replace it with replace
        int i = content.indexOf(find);
        if(i != 1){
            content = content.substring(0, i) + replace + content.substring(i+find.length());
            area.setText(content);
        }
    }

    public void replaceAll(String find, String replace){
        // get the text and replace all occurrences of find
        String content = area.getText();
        content = content.replaceAll(find, replace);
        area.setText(content);
    }

    public void undo(){
        try{
            if(undoer.canUndo()) undoer.undo();
        }catch(CannotUndoException e) {
            e.printStackTrace();
        }
    }

    public void redo(){
        try{
            if(undoer.canRedo()) undoer.redo();
        }catch(CannotRedoException e){
            e.printStackTrace();
        }
    }
}
