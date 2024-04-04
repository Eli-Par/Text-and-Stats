import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

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

    //@Deprecated
    //private JTextArea area = new JTextArea("Empty"); //@TODO Remove this

    private File path;

    private boolean saved;
    private UndoManager undoer;

    public static enum Format {PLAIN, RTF};

    public static Format[] styledFormats = {Format.RTF};

    private Format fileFormat;

    private EditorKit editorKit;

    private DefaultStyledDocument document;

    private JTextPane textPane;

    private DocumentFormatter formatter;

    String findText;
    int currI = 0;
    Highlighter.Highlight[] highlights;
    boolean first = true;

    public EditorPanel(TabPanel tab, File file) {

        super();

        

        updateFormat(file); //Sets the fileFormat based on the inputted file

        System.out.println(">" + fileFormat);

        this.tab = tab;
        path = file;
        saved = true;

        this.setLayout(new BorderLayout());

        textPane = new JTextPane();

        //Create a new DefaultStyledDocument
        document = new DefaultStyledDocument(new StyleContext());

        //Create the correct editor kit for the file format
        if(fileFormat == Format.RTF) 
        {
            editorKit = new RTFEditorKit();
        }
        else 
        {
            editorKit = new DefaultEditorKit();
        }

        //Set the textPane to display the document
        textPane.setDocument(document);

        //Load the file using the editor kit
        try {
            editorKit.read(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8), document, 0);
        }
        catch(Exception exception) {
            exception.printStackTrace();
        }

        //Remove the ending character since an extra newline is added
        if(editorKit instanceof RTFEditorKit) {
            try {
                document.replace(document.getLength() - 1, 1, "", null);
            }
            catch(Exception exception) {
                exception.printStackTrace();
            }
        }

        //Add a scroll bar
        JScrollPane scroll = new JScrollPane(textPane);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(scroll);

        document.addDocumentListener(this);

        SpellChecker.setUserDictionaryProvider(new FileUserDictionary());      
        SpellChecker.registerDictionaries(this.getClass().getResource("/dictionary"), "en");
        SpellChecker.register(textPane, true, false, true, true);

        undoer = new UndoManager();
        document.addUndoableEditListener(undoer);

        formatter = new DocumentFormatter(textPane, document, fileFormat, this);

    }

    public DocumentFormatter getFormatter() {
        return formatter;
    }

    public Format getFileFormat() {
        return fileFormat;
    }

    //Updates the format of the editor panel based on the file extension
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

    public void toggleAutosave() {
        
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

    public void checkSaving() {

        if(FileTabList.useAutoSave) {
            try {
                save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (saved) {
            tab.notSavedIndicator();
            saved = false;
        }

    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        tab.textChanged();
        checkSaving();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        tab.textChanged();
        checkSaving();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        //Do nothing
    }

    //@TODO Phase out accessing the text externally
    public JTextPane getTextPane() {
        return textPane;
    }

    public void find(String text){

        // create a highlighter to highlight the text
        Highlighter h = textPane.getHighlighter();
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.CYAN);
        h.removeAllHighlights();

        findText = text;

        if(Objects.equals(text, "")){
            highlights = null;
            return;
        }

        // get the text
        String content = getPlainText();

        // loop through all occurrences and highlight them
        int i = content.indexOf(text);
        while(i != -1){
            try{
                h.addHighlight(i, i+text.length(), painter);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            i = content.indexOf(text, i+1);
        }
        currI = 0;
        first = true;
        highlights = h.getHighlights();
    }

    /**
     * @param dir -1 for backwards, 1 for forwards
     */
    public void nav(int dir){

        Highlighter h = textPane.getHighlighter();
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.CYAN);
        h.removeAllHighlights();
        if(highlights == null) return;
        if(highlights.length == 0) return;

        try {
            // cycle forwards until end then jump back to top
            if (dir == 1) {
                if (currI >= highlights.length-1) currI = 0;
                else if(!first) currI++;
                h.addHighlight(highlights[currI].getStartOffset(), highlights[currI].getEndOffset(), painter);
            // cycle backwards until start then jump back to bottom
            } else if (dir == -1) {
                if (currI <= 0) currI = highlights.length - 1;
                else currI--;
                h.addHighlight(highlights[currI].getStartOffset(), highlights[currI].getEndOffset(), painter);
            }
            textPane.setCaretPosition(highlights[currI].getStartOffset());
            first = false;

        }catch (BadLocationException e){
            e.printStackTrace();
        }
    }

    public void replaceInstance(String replace){

        if(Objects.equals(replace, "")) return;

        if(highlights == null || highlights.length == 0) return;

        int start = highlights[currI].getStartOffset();
        int end = highlights[currI].getEndOffset();
        if(start < end){
            try{
                document.replace(start, (end-start), replace, formatter.getConsistentFormat(document, start, end));
                Highlighter h = textPane.getHighlighter();
                h.removeAllHighlights();
            }catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    public void replaceAll(String find, String replace){

        if (find.isEmpty())
            return;

        // get the text and replace all occurrences of find
        String content = getPlainText();
        int selstart = textPane.getSelectionStart();
        int selend = textPane.getSelectionEnd();

        if (selstart == selend) {
            selstart = 0;
            selend = content.length();
        }

        int i = content.indexOf(find, selstart);
        while(i != -1 && i + find.length() <= selend) {
            try {
                document.replace(i, find.length(), replace, formatter.getConsistentFormat(document, i, i + find.length()));
            }
            catch(BadLocationException exception) {
                exception.printStackTrace();
            }

            content = getPlainText();
            i = content.indexOf(find, i + replace.length());
            selend += replace.length() - find.length();
        }
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

    public void signalEdit() {
        checkSaving();    
    }
    public Color getTextColor(){
        int caretPosition = textPane.getCaretPosition();
        Element element = document.getCharacterElement(caretPosition);
        AttributeSet attributes = element.getAttributes();
        Color color = StyleConstants.getForeground(attributes);
        float[] values = new float[3];
        values = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), values);
        if(values[0] > 40/360f && values[0] < 68/360f){
            values[0] = 40/360f;
        }else if(values[0] >= 68/360f && values[0] < 100/360f){
            values[0] = 100/360f;
        }
        Color finalColor = new Color(Color.HSBtoRGB(values[0], 1, 1));

        return finalColor;
    }

}
