package parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class WordFrequency extends ParseObserver <String, String>{
    
    private Parser parser;

    public WordFrequency(Parser parser) {
        this.parser = parser;
    }

    @Override
    protected String backgroundTask() {

        //Publishing something will result in processThread being called at the next opportunity
        publish(null);

        ArrayList<Word> words = parser.getWords();
        HashMap<String, Integer> wFrequencies = new HashMap<String, Integer>();
        String wordStr;
        int most = 0;
        int secMost = 0;
        int thirdMost = 0;
        String mostFrequent = "";
        String secMostFrequent = "";
        String thirdMostFrequent = "";
        int totWords = 0;
        
        if(isCancelled()) return null;
        for(int i =0; i< words.size(); i++){
            wFrequencies.putIfAbsent(words.get(i).getText(), 0);
        }
        if(isCancelled()) return null;
        for(Word w:words){
            wordStr = w.getText();
            wFrequencies.put(wordStr, wFrequencies.get(wordStr) + 1);
        }
        if(isCancelled()) return null;
        for(String word : wFrequencies.keySet()){
            int f = wFrequencies.get(word);
            if(f>=most){
                thirdMost = secMost;
                thirdMostFrequent = secMostFrequent;
                secMost = most;
                secMostFrequent = mostFrequent;
                most = f;
                mostFrequent = word;
            }else if(f>=secMost){
                thirdMost = secMost;
                thirdMostFrequent = secMostFrequent;
                secMost = f;
                secMostFrequent = word;
            }else if(f>= thirdMost){
                thirdMost = f;
                thirdMostFrequent = word;
            }
            totWords++;
        }

        // Throughout the background task, isCancelled should be called to check if the task has been stopped
        // If it has, simply returning null is fine since the observer will not allow this result to reach the
        // other methods on the swing event disbatch thread
        if(isCancelled()) return null;

        System.out.println("Background task returning");
        
        String statOutput = "Most Frequent Word: '" + mostFrequent + "' appears " + most +" times\n";
        statOutput += "2nd most Frequent Word: '" + secMostFrequent + "' appears " +secMost +" times\n";
        statOutput += "3rd most Frequent Word: '" + thirdMostFrequent + "' appears " +thirdMost +" times\n";
        statOutput += "\nThere are "+ totWords +" unique Words";
        return statOutput;
    }

    @Override
    protected void processTask(List<String> textList) {
    }

    @Override
    protected void doneTask(String output) {
        System.out.println("!!>>Done run<<!!");
        StatsGUI.setFrequencies(output);

    }

    @Override
    public void parseStarted() {
        StatsGUI.setFrequencies("Parse started...");
    }
}

