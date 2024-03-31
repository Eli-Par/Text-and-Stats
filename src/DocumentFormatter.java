import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class DocumentFormatter {
    private JTextPane textPane;
    private DefaultStyledDocument document;
    private EditorPanel.Format fileFormat;
    private EditorPanel panel;

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
            if(document.getCharacterElement(textPane.getCaretPosition()).getAttributes().getAttribute(attributeKey) instanceof Boolean isFormatted) {
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
            if(document.getCharacterElement(textPane.getCaretPosition()).getAttributes().getAttribute(StyleConstants.FontSize) instanceof Integer size) {
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
        format.addAttribute(StyleConstants.FontFamily, family);

        document.setCharacterAttributes(startIndex, endIndex - startIndex, format, false);

        panel.signalEdit();
    }

    public String getSelectedFontFamily() {
        int startIndex = textPane.getSelectionStart();
        int endIndex = textPane.getSelectionEnd();

        //If there is no selection, return the styling at the current caret position
        if(endIndex == startIndex) {
            int caretPosition = textPane.getCaretPosition();
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

}
