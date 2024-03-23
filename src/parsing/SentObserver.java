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

        for(Sentence s: sents){
            String sString = s.getText();
            if(!sentFreq.containsKey(sString)){
                sentFreq.put(sString, 1);
            }else{
                sentFreq.put(sString, sentFreq.get(sString)+1);
            }
        }
        
        LinkedHashMap<String, Integer> sortedWords = sentFreq.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                
        for(String s: sortedWords.keySet()){
            if(sentFreq.get(s)>1){
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
        panel.setStats("Processing tokens...");
    }

    @Override
    protected void doneTask(String output) {
        System.out.println("!!>>Done run<<!!");

        panel.setStats(output);
    }

    @Override
    public void parseStarted() {
        panel.setStats("Parse started...");
    }
}
