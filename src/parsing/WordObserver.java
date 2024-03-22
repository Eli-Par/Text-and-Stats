package parsing;

import java.util.ArrayList;
import java.util.List;

public class WordObserver extends ParseObserver <String, String>{
    
    private Parser parser;

    public WordObserver(Parser parser) {
        this.parser = parser;
    }

    @Override
    protected String backgroundTask() {

        //Publishing something will result in processThread being called at the next opportunity
        publish(null);

        ArrayList<Word> words = parser.getWords();
        ArrayList<Sentence> sents = parser.getSentences();

        String statOutput = "Words: " + words.size() +"\n";
        statOutput += "Sentences: " + sents.size();

        // Throughout the background task, isCancelled should be called to check if the task has been stopped
        // If it has, simply returning null is fine since the observer will not allow this result to reach the
        // other methods on the swing event disbatch thread
        if(isCancelled()) return null;

        System.out.println("Background task returning");

        return statOutput;
    }

    @Override
    protected void processTask(List<String> textList) {
        StatsGUI.setStats("Processing tokens...");
    }

    @Override
    protected void doneTask(String output) {
        System.out.println("!!>>Done run<<!!");

        StatsGUI.setStats(output);
    }

    @Override
    public void parseStarted() {
        StatsGUI.setStats("Parse started...");
    }
}
