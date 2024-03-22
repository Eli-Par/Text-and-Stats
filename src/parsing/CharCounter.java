package parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.LinkedHashMap;
public class CharCounter extends ParseObserver <String, String>{
    
    private Parser parser;

    public CharCounter(Parser parser) {
        this.parser = parser;
    }

    @Override
    protected String backgroundTask() {

        //Publishing something will result in processThread being called at the next opportunity
        publish(null);

        int charCount = 0;
        
        ArrayList<Sentence> sents = parser.getSentences();
        ArrayList<Word> words = parser.getWords();
        HashMap<Character, Integer> chars = new HashMap<Character, Integer>();

        for(Sentence s : sents){
            String sentance = s.getText();
            charCount += sentance.length();

        }
        if(isCancelled()) return null;
        for(Word w: words){
            String finWord = w.getText();
            char [] charArray = finWord.toCharArray();
            for(char c: charArray){
                c = Character.toLowerCase(c);
                if(!chars.containsKey(c)){
                    chars.put(c, 1);
                }else{
                    chars.put(c, chars.get(c)+1);
                }
                
            }
        }
        if(isCancelled()) return null;
        

        
        StatsGUI.setTotChars("Character Count: " + charCount + "\n");
        LinkedHashMap<Character, Integer> sortedChars = chars.entrySet().stream()
                .sorted(Map.Entry.<Character, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        // Construct statOutput with sorted characters
        StringBuilder statOutput = new StringBuilder();
        sortedChars.forEach((c, count) -> statOutput.append(c).append(" appears ").append(count).append(" times\n"));
        if(isCancelled()) return null;
        return statOutput.toString();
    }

    @Override
    protected void processTask(List<String> textList) {
    }

    @Override
    protected void doneTask(String output) {
        System.out.println("!!>>Done run<<!!");
        StatsGUI.setChars(output);

    }

    @Override
    public void parseStarted() {
        StatsGUI.setChars("Parse started...");
    }
}