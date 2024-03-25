package parsing;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class SentObserver extends ParseObserver <String, String>{
    
    private Parser parser;
    private SentPanel panel;

    public SentObserver(Parser parser, SentPanel textPanel) {
        this.parser = parser;
        this.panel = textPanel;
    }

    @Override
    protected String backgroundTask() {

        //Publishing something will result in processThread being called at the next opportunity
        publish(null);

        ArrayList<Sentence> sents = parser.getSentences();

        String statOutput = "Sentences: " + sents.size();

        HashMap<String, Integer> sentFreq = new HashMap<String, Integer>();

        int longestSentence = 0;
        String longestSentenceString = "";

        for(Sentence s: sents){
            if(isCancelled()) return null;

            if(s.getWords().length > longestSentence) {
                longestSentence = s.getWords().length;
                longestSentenceString = s.getText();
            }

            String sString = s.getText();
            if(!sentFreq.containsKey(sString)){
                sentFreq.put(sString, 1);
            }else{
                sentFreq.put(sString, sentFreq.get(sString)+1);
            }
        }

        statOutput += "\nLongest sentence is " + longestSentence + " words long and is '" + longestSentenceString + "'\n";
        
        LinkedHashMap<String, Integer> sortedWords = sentFreq.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                
        for(String s: sortedWords.keySet()){
            if(isCancelled()) return null;

            if(sentFreq.get(s)>1 && !s.equals(".") ){
                statOutput += "\n\"" + s + "\" appears " + sentFreq.get(s) + " times";
            }
        }

        // Throughout the background task, isCancelled should be called to check if the task has been stopped
        // If it has, simply returning null is fine since the observer will not allow this result to reach the
        // other methods on the swing event disbatch thread
        if(isCancelled()) return null;

        System.out.println("Background task returning");

        return statOutput;
    }

    @Override
    protected void processTask(List<String> textList) {
        //panel.setStats("Processing tokens...");
    }

    @Override
    protected void doneTask(String output) {
        System.out.println("!!>>Done run<<!!");

        panel.setStats(output);
    }

    @Override
    public void parseStarted() {
        panel.setStats("Processing the text. Please wait a moment.");
    }
}
