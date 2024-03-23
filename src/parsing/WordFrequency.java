package parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class WordFrequency extends ParseObserver <String, String>{
    
    private Parser parser;
    private WordPanel panel;

    public WordFrequency(Parser parser, WordPanel textPanel) {
        this.parser = parser;
        this.panel = textPanel;
    }

    @Override
    protected String backgroundTask() {

        //Publishing something will result in processThread being called at the next opportunity
        publish(null);

        ArrayList<Word> words = parser.getWords();
        HashMap<String, Integer> wFrequencies = new HashMap<String, Integer>();
        String wordStr;
        int totWords = 0;

        int wordCount = 0;
        
        if(isCancelled()) return null;
        for(int i =0; i< words.size(); i++){
            if(!words.get(i).isNumeric() && !words.get(i).isSymbol()) {
                wFrequencies.putIfAbsent(words.get(i).getText(), 0);
            }
        }
        if(isCancelled()) return null;
        for(Word w:words){
            if(!w.isNumeric() && !w.isSymbol()) {
                wordStr = w.getText();
                wFrequencies.put(wordStr, wFrequencies.get(wordStr) + 1);

                wordCount++;
            }
        }
        if(isCancelled()) return null;

        
        LinkedHashMap<String, Integer> sortedWords = wFrequencies.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        StringBuilder frequencyWords = new StringBuilder();

        ArrayList<StringValue> frequencies = new ArrayList<>();

        int [] arr = new int[words.size()];
        String [] keys = new String[words.size()];
        int i = 0;
        for (String word : sortedWords.keySet()) {
            int f = sortedWords.get(word);
            
            frequencyWords.append("'" + word+ "'").append(" appears ").append(f).append(" times\n");
            frequencies.add(new StringValue(word, f));

            arr[i] = f;
            keys[i] = word;
            totWords++;
            i++;
        }

        panel.setFrequencyList(frequencies);
        
        panel.setBG(arr,keys);

        // Throughout the background task, isCancelled should be called to check if the task has been stopped
        // If it has, simply returning null is fine since the observer will not allow this result to reach the
        // other methods on the swing event disbatch thread
        if(isCancelled()) return null;
        int totalLength =0;
        for(Word w: words){
            if(!w.isNumeric() && !w.isSymbol()) {
                String finWord = w.getText();
                totalLength += finWord.length();
            }
        }
        
        if(isCancelled()) return null;
        String tWordString = "";
        tWordString = "Words: " + wordCount;
        tWordString += "\nThere are "+ totWords +" unique Words";
        tWordString += "\nThe Average Word Length is " + totalLength/wordCount;
        panel.setTotWords(tWordString);

        String statOutput = frequencyWords.toString().trim();

        System.out.println("Background task returning");
        return statOutput;
    }

    @Override
    protected void processTask(List<String> textList) {
    }

    @Override
    protected void doneTask(String output) {
        System.out.println("!!>>Done run<<!!");
        panel.setFrequencies(output);

    }

    @Override
    public void parseStarted() {
        panel.setFrequencies("Parse started...");
    }
}

