package parsing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.SwingWorker;

public class SearchWorker extends SwingWorker<String, String> {
    WordPanel panel;
    public SearchWorker(WordPanel panel) {
        this.panel = panel;
    }

    @Override
    protected String doInBackground() throws Exception {
        String word = panel.getSearchWord().trim().toLowerCase();

        if(word.isBlank()) {
            panel.useDefaultFrequencyText();
            return null;
        }

        HashMap<String, Integer> wordList = panel.getWordList();
        String updateString = "";

        ArrayList<SearchItem> items = new ArrayList<>();
        for(String s: wordList.keySet()){
            if(isCancelled()) return null;
            items.add(new SearchItem(s, wordList.get(s)));
        }

        for(SearchItem item : items) {
            item.calculateDistance(word);
        }

        Collections.sort(items);

        int numLinesToDisplay = panel.getViewableLines();

        for(int i = 0; i <= numLinesToDisplay; i++) {
            updateString += items.get(i).toString();
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

    //Use a variation on hammer distance as an approximation of edit distance for pruning
    private int hammeringDistance(String str1, String str2) {
        int minLength = Math.min(str1.length(), str2.length());
        int cost = Math.abs(str1.length() - str2.length());

        for(int i = 0; i < minLength; i++) {
            if(str1.charAt(i) != str2.charAt(i)) {
                cost++;
            }
        }

        return cost;
    }

}
