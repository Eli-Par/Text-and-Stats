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

        worker = new SearchWorker(panel);

        worker.execute();

    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // Handle attribute changes, not applicable for plain text components like JTextField
    }
}
