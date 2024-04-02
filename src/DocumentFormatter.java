import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class DocumentFormatter {
    private JTextPane textPane;
    private DefaultStyledDocument document;
    private EditorPanel.Format fileFormat;
    private EditorPanel panel;
    
    public static final int FONT_SIZES[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72, 80};

    public DocumentFormatter(JTextPane textPane, DefaultStyledDocument document, EditorPanel.Format fileFormat, EditorPanel panel) {
        this.textPane = textPane;
        this.document = document;
        this.fileFormat = fileFormat;
        this.panel = panel;
    }

    //Changes the selected texts format to the opposite of what it currently is, toggling the style. Will change the word that the cursor
    public void toggleFormatSelected(Object attributeKey) {

        //Check that the formatting is valid for this file type
        if(!ValidFormattingSet.isFormatValid(fileFormat, attributeKey)) {
            return;
        }

        int startIndex = textPane.getSelectionStart();
        int endIndex = textPane.getSelectionEnd();

        //If no selection is picked, get the entire word
        if(endIndex == startIndex) {
            int wordStart = textPane.getCaretPosition();
            int wordEnd = wordStart + 1;

            while(wordStart > 0 && !Character.isWhitespace(textPane.getText().charAt(wordStart - 1))) {
                wordStart--;
            }

            while(wordEnd < textPane.getText().length() && !Character.isWhitespace(textPane.getText().charAt(wordEnd))) {
                wordEnd++;
            }

            startIndex = wordStart;
            endIndex = wordEnd;
        }

        boolean setFormat = !isSelectionFormatted(attributeKey);
        SimpleAttributeSet style = new SimpleAttributeSet();
        style.addAttribute(attributeKey, setFormat);
        document.setCharacterAttributes(startIndex, endIndex - startIndex, style, false);

        panel.signalEdit();
    }

    //Returns if the selection is mostly the specified attribute or not. Only works on attributes with boolean return values.
    //If no selection exists, returns the value for the character immediately after the cursor
    public boolean isSelectionFormatted(Object attributeKey) {
        int startIndex = textPane.getSelectionStart();
        int endIndex = textPane.getSelectionEnd();

        //If there is no selection, return the styling at the current caret position
        if(endIndex == startIndex) {
            int caretPosition = textPane.getCaretPosition() - 1;
            if(caretPosition < 0) caretPosition = 0;
            if(document.getCharacterElement(caretPosition).getAttributes().getAttribute(attributeKey) instanceof Boolean isFormatted) {
                return isFormatted;
            }
            return false;
        }

        int formatCount = 0;

        for(int i = startIndex; i < endIndex; i++) {
            if(document.getCharacterElement(i).getAttributes().getAttribute(attributeKey) instanceof Boolean isFormatted) {
                if(isFormatted) formatCount++;
            }
        }

        return formatCount > (endIndex - startIndex) / 2;
    }

    public void setSelectedFontSize(int size) {
        //Check that the formatting is valid for this file type
        if(!ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.FontSize)) {
            return;
        }

        int startIndex = textPane.getSelectionStart();
        int endIndex = textPane.getSelectionEnd();

        //If no selection is picked, get the entire word
        if(endIndex == startIndex) {
            int wordStart = textPane.getCaretPosition();
            int wordEnd = wordStart + 1;

            while(wordStart > 0 && !Character.isWhitespace(textPane.getText().charAt(wordStart - 1))) {
                wordStart--;
            }

            while(wordEnd < textPane.getText().length() && !Character.isWhitespace(textPane.getText().charAt(wordEnd))) {
                wordEnd++;
            }

            startIndex = wordStart;
            endIndex = wordEnd;
        }

        SimpleAttributeSet format = new SimpleAttributeSet();
        format.addAttribute(StyleConstants.FontSize, size);

        document.setCharacterAttributes(startIndex, endIndex - startIndex, format, false);

        panel.signalEdit();
    }

    public int getSelectedFontSize() {
        int startIndex = textPane.getSelectionStart();
        int endIndex = textPane.getSelectionEnd();

        //If there is no selection, return the styling at the current caret position
        if(endIndex == startIndex) {
            int caretPosition = textPane.getCaretPosition() - 1;
            if(caretPosition < 0) caretPosition = 0;
            if(document.getCharacterElement(caretPosition).getAttributes().getAttribute(StyleConstants.FontSize) instanceof Integer size) {
                return size;
            }
            return -1;
        }

        int fontSize = -1;

        for(int i = startIndex; i < endIndex; i++) {
            if(document.getCharacterElement(i).getAttributes().getAttribute(StyleConstants.FontSize) instanceof Integer size) {
                if(fontSize == -1) {
                    fontSize = size;
                }
                else if(fontSize != size) {
                    return -1;
                }
            }
        }

        return fontSize;
    }

    public void setSelectedFontFamily(String family) {
        //Check that the formatting is valid for this file type
        if(!ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.FontFamily)) {
            return;
        }

        int startIndex = textPane.getSelectionStart();
        int endIndex = textPane.getSelectionEnd();

        //If no selection is picked, get the entire word
        if(endIndex == startIndex) {
            int wordStart = textPane.getCaretPosition();
            int wordEnd = wordStart + 1;

            while(wordStart > 0 && !Character.isWhitespace(textPane.getText().charAt(wordStart - 2))) {
                wordStart--;
            }

            while(wordEnd < textPane.getText().length() && !Character.isWhitespace(textPane.getText().charAt(wordEnd))) {
                wordEnd++;
            }

            startIndex = wordStart;
            endIndex = wordEnd;
        }

        SimpleAttributeSet format = new SimpleAttributeSet();
        format.addAttribute(StyleConstants.FontFamily, family);

        document.setCharacterAttributes(startIndex, endIndex - startIndex, format, false);

        panel.signalEdit();
    }

    public String getSelectedFontFamily() {
        int startIndex = textPane.getSelectionStart();
        int endIndex = textPane.getSelectionEnd();

        //If there is no selection, return the styling at the current caret position
        if(endIndex == startIndex) {
            int caretPosition = textPane.getCaretPosition() - 1;
            if(caretPosition < 0) caretPosition = 0;
            if(document.getCharacterElement(caretPosition).getAttributes().getAttribute(StyleConstants.FontFamily) instanceof String family) {
                return family;
            }
            return null;
        }

        String familyName = null;

        for(int i = startIndex; i < endIndex; i++) {
            if(document.getCharacterElement(i).getAttributes().getAttribute(StyleConstants.FontFamily) instanceof String family) {
                if(familyName == null) {
                    familyName = family;
                }
                else if(!familyName.equals(family)) {
                    return null;
                }
            }
        }

        return familyName;
    }

    public void setSelectedTextColor(Color color) {
        //Check that the formatting is valid for this file type
        if(!ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.Foreground)) {
            return;
        }

        int startIndex = textPane.getSelectionStart();
        int endIndex = textPane.getSelectionEnd();

        //If no selection is picked, get the entire word
        if(endIndex == startIndex) {
            int wordStart = textPane.getCaretPosition();
            int wordEnd = wordStart + 1;

            while(wordStart > 0 && !Character.isWhitespace(textPane.getText().charAt(wordStart - 1))) {
                wordStart--;
            }

            while(wordEnd < textPane.getText().length() && !Character.isWhitespace(textPane.getText().charAt(wordEnd))) {
                wordEnd++;
            }

            startIndex = wordStart;
            endIndex = wordEnd;
        }

        SimpleAttributeSet format = new SimpleAttributeSet();
        format.addAttribute(StyleConstants.Foreground, color);

        document.setCharacterAttributes(startIndex, endIndex - startIndex, format, false);

        panel.signalEdit();
    }

    public void increaseFontSize() {
        //Check that the formatting is valid for this file type
        if(!ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.FontSize)) {
            return;
        }

        int startIndex = textPane.getSelectionStart();
        int endIndex = textPane.getSelectionEnd();

        //If no selection is picked, get the entire word
        if(endIndex == startIndex) {
            int wordStart = textPane.getCaretPosition();
            int wordEnd = wordStart + 1;

            while(wordStart > 0 && !Character.isWhitespace(textPane.getText().charAt(wordStart - 1))) {
                wordStart--;
            }

            while(wordEnd < textPane.getText().length() && !Character.isWhitespace(textPane.getText().charAt(wordEnd))) {
                wordEnd++;
            }

            startIndex = wordStart;
            endIndex = wordEnd;
        }

        int setSize = 100;
        
        for(int i = startIndex; i < endIndex; i++) {
            if(document.getCharacterElement(i).getAttributes().getAttribute(StyleConstants.FontSize) instanceof Integer currSize) {
                int nextSize = currSize + 1;
                
                if(nextSize > 80) {
                    int lastDigit = currSize % 10;
                    nextSize = (currSize - lastDigit) + 10;
                }
                else {
                    for(int validSize : FONT_SIZES) {
                        if(validSize >= nextSize) {
                            nextSize = validSize;
                            break;
                        }
                    }
                }

                if(nextSize - currSize < setSize) {
                    setSize = nextSize - currSize;
                }

                // SimpleAttributeSet format = new SimpleAttributeSet();
                // format.addAttribute(StyleConstants.FontSize, nextSize);
                // document.setCharacterAttributes(i, 1, format, false);
            }
        }

        for(int i = startIndex; i < endIndex; i++) {
            if(document.getCharacterElement(i).getAttributes().getAttribute(StyleConstants.FontSize) instanceof Integer currSize) {
                SimpleAttributeSet format = new SimpleAttributeSet();
                format.addAttribute(StyleConstants.FontSize, setSize + currSize);
                document.setCharacterAttributes(i, 1, format, false);
            }
        }

        panel.signalEdit();
    }

    public void decreaseFontSize() {
        //Check that the formatting is valid for this file type
        if(!ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.FontSize)) {
            return;
        }

        int startIndex = textPane.getSelectionStart();
        int endIndex = textPane.getSelectionEnd();

        //If no selection is picked, get the entire word
        if(endIndex == startIndex) {
            int wordStart = textPane.getCaretPosition();
            int wordEnd = wordStart + 1;

            while(wordStart > 0 && !Character.isWhitespace(textPane.getText().charAt(wordStart - 1))) {
                wordStart--;
            }

            while(wordEnd < textPane.getText().length() && !Character.isWhitespace(textPane.getText().charAt(wordEnd))) {
                wordEnd++;
            }

            startIndex = wordStart;
            endIndex = wordEnd;
        }

        int setSize = 100;
        
        for(int i = startIndex; i < endIndex; i++) {
            if(document.getCharacterElement(i).getAttributes().getAttribute(StyleConstants.FontSize) instanceof Integer currSize) {
                int nextSize = currSize - 1;
                
                if(nextSize > 80) {
                    int lastDigit = currSize % 10;
                    nextSize = (currSize - lastDigit) - 10;
                }
                else {
                    for(int k = FONT_SIZES.length - 1; k >= 0; k--) {
                        int validSize = FONT_SIZES[k];
                        if(validSize <= nextSize) {
                            nextSize = validSize;
                            break;
                        }
                    }
                }

                if(currSize - nextSize < setSize) {
                    setSize = currSize - nextSize;
                }

                // SimpleAttributeSet format = new SimpleAttributeSet();
                // format.addAttribute(StyleConstants.FontSize, nextSize);
                // document.setCharacterAttributes(i, 1, format, false);
            }
        }

        for(int i = startIndex; i < endIndex; i++) {
            if(document.getCharacterElement(i).getAttributes().getAttribute(StyleConstants.FontSize) instanceof Integer currSize) {
                SimpleAttributeSet format = new SimpleAttributeSet();
                format.addAttribute(StyleConstants.FontSize, currSize - setSize);
                document.setCharacterAttributes(i, 1, format, false);
            }
        }

        panel.signalEdit();
    }

    public SimpleAttributeSet getConsistentFormat(StyledDocument document, int startIndex, int endIndex) {

        SimpleAttributeSet set = new SimpleAttributeSet();

        addAttributeToFormat(set, StyleConstants.Bold, document, startIndex, endIndex);
        addAttributeToFormat(set, StyleConstants.Italic, document, startIndex, endIndex);
        addAttributeToFormat(set, StyleConstants.Underline, document, startIndex, endIndex);
        addAttributeToFormat(set, StyleConstants.FontSize, document, startIndex, endIndex);
        addAttributeToFormat(set, StyleConstants.FontFamily, document, startIndex, endIndex);
        addAttributeToFormat(set, StyleConstants.Foreground, document, startIndex, endIndex);

        return set;
    }

    private void addAttributeToFormat(SimpleAttributeSet set, Object key, StyledDocument document, int startIndex, int endIndex) {
        Object value = document.getCharacterElement(startIndex).getAttributes().getAttribute(key);
        for(int i = startIndex + 1; i < endIndex; i++) {
            Object charVal = document.getCharacterElement(i).getAttributes().getAttribute(key);
            if(!value.equals(charVal)) {
                return;
            }
        }

        set.addAttribute(key, value);
    }

}
