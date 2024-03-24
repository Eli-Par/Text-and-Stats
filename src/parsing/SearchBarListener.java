package parsing;

import java.util.HashMap;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SearchBarListener implements DocumentListener{
    WordPanel panel;

    public SearchBarListener(WordPanel panel){
        this.panel = panel;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        // Handle text insertion
        String word = panel.getSearchWord();
        HashMap<String, Integer> wordList = panel.getWordList();
        String updateString = "";
        for(String s: wordList.keySet()){
            if(s.contains(word)){
                updateString += "'" + s + "' appears " + wordList.get(s) + " times\n";
            }
        }
        panel.setFrequencies(updateString);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        // Handle text removal
        
        String word = panel.getSearchWord();
        HashMap<String, Integer> wordList = panel.getWordList();
        String updateString = "";
        for(String s: wordList.keySet()){
            if(s.contains(word)){
                updateString += "'" + s + "' appears " + wordList.get(s) + " times\n";
            }
        }
        panel.setFrequencies(updateString);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // Handle attribute changes, not applicable for plain text components like JTextField
    }
}
