package parsing;

import java.util.HashMap;

import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SearchBarListener implements DocumentListener{
    WordPanel panel;

    private SwingWorker<String, String> worker = null;

    public SearchBarListener(WordPanel panel){
        this.panel = panel;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateSearchResults();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateSearchResults();
    }

    private void updateSearchResults() {

        if(worker != null) {
            worker.cancel(true);
        }

        worker = new SwingWorker<>() {

            @Override
            protected String doInBackground() throws Exception {
                String word = panel.getSearchWord().trim();

                if(word.isBlank()) {
                    panel.useDefaultFrequencyText();
                    return null;
                }

                HashMap<String, Integer> wordList = panel.getWordList();
                String updateString = "";
                for(String s: wordList.keySet()){
                    if(isCancelled()) return null;
                    if(s.contains(word)){
                        updateString += "'" + s + "' appears " + wordList.get(s) + " times\n";
                    }
                }

                if(isCancelled()) return null;

                return updateString;
            }

            @Override
            protected void done() {
                try {
                    String text = get();
                    if(text != null) panel.setFrequencies(text);
                }
                catch(Exception exception) {

                }
            }
            
        };

        worker.execute();

    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // Handle attribute changes, not applicable for plain text components like JTextField
    }
}
