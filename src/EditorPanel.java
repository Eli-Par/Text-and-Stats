import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.charset.Charset;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import com.inet.jortho.FileUserDictionary;
import com.inet.jortho.SpellChecker;

public class EditorPanel extends JPanel implements DocumentListener{

    private TabPanel tab;

    @Deprecated
    private JTextArea area = new JTextArea("Empty"); //@TODO Remove this

    private File path;

    private boolean saved;
    private UndoManager undoer;

    public static enum Format {PLAIN, RTF};

    public static Format[] styledFormats = {Format.RTF};

    private Format fileFormat;

    private EditorKit editorKit;

    private StyledDocument document;

    private JTextPane textPane;

    public EditorPanel(TabPanel tab, File file) {

        super();

        updateFormat(file); //Sets the fileFormat based on the inputted file

        System.out.println(">" + fileFormat);

        this.tab = tab;
        path = file;
        saved = true;

        this.setLayout(new BorderLayout());

        textPane = new JTextPane();

        document = new DefaultStyledDocument(new StyleContext());

        if(fileFormat == Format.RTF) 
        {
            editorKit = new RTFEditorKit();
        }
        else 
        {
            editorKit = new DefaultEditorKit();
        }

        textPane.setDocument(document);

        try {
            editorKit.read(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")), document, 0);
        }
        catch(Exception exception) {
            System.out.println(exception.getStackTrace());
        }

        JScrollPane scroll = new JScrollPane(textPane);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(scroll);

        document.addDocumentListener(this);

        SpellChecker.setUserDictionaryProvider(new FileUserDictionary());      
        SpellChecker.registerDictionaries(this.getClass().getResource("/dictionary"), "en");
        SpellChecker.register(textPane, true, false, true, true);

        undoer = new UndoManager();
        document.addUndoableEditListener(undoer);

    }

    private void updateFormat(File file) {
        if(file.getName().contains(".")) {
            String extension = file.getName().substring(file.getName().lastIndexOf('.'), file.getName().length());
            
            if(extension.equals(".rtf")) fileFormat = Format.RTF;
            else fileFormat = Format.PLAIN;
        }
    }

    public String getPlainText() {
        try {
            return document.getText(0, document.getLength());
        }
        catch(BadLocationException exception) {
            return null;
        }
    }

    public void setPath(File p) {
        path = p;
    }

    public File getPath() {
        return path;
    }

    public void save() throws IOException {

        try {
            editorKit.write(new FileOutputStream(path), document, 0, document.getLength());
        }
        catch(BadLocationException exception) {
            System.out.println(exception.getStackTrace());
        }

        if(!saved) {
            saved = true;
            tab.savedIndicator();
        }

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
        textPane.addKeyListener(l);
    }

    //@TODO Remove once all features are ported to JTextPane
    @Deprecated
    public JTextArea getTextArea() {
        return area;
    }

    public void find(String text){
        // get the text
        String content = getPlainText();
        // create a highlighter to highlight the text
        Highlighter h = textPane.getHighlighter();
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.CYAN);
        h.removeAllHighlights();

        // loop through all occurrences and highlight them
        int i = content.indexOf(text);
        boolean caretNotSet = true;
        if((i > textPane.getCaretPosition() || i == 0 && textPane.getCaretPosition() == 0) && caretNotSet) 
        {
            textPane.setCaretPosition(i);
            caretNotSet = false;
        }
        while(i != -1){
            try{
                h.addHighlight(i, i+text.length(), painter);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            i = content.indexOf(text, i+1);
            if(i > textPane.getCaretPosition() && caretNotSet) 
        {
            textPane.setCaretPosition(i);
            caretNotSet = false;
        }
        }

        // delete highlighted text when mouse is clicked
        textPane.addMouseListener(new MouseAdapter() {
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
        String content = getPlainText();

        // find the first occurrence of find and replace it with replace
        int i = content.indexOf(find);
        if(i != 1){
            content = content.substring(0, i) + replace + content.substring(i+find.length());
            textPane.setText(content);
        }
    }

    public void replaceAll(String find, String replace){
        // get the text and replace all occurrences of find
        String content = getPlainText();
        content = content.replaceAll(find, replace);
        textPane.setText(content);
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
