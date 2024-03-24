package parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.LinkedHashMap;
public class CharCounter extends ParseObserver <String, String>{
    
    private Parser parser;
    private CharPanel panel;

    public CharCounter(Parser parser, CharPanel textPanel) {
        this.parser = parser;
        this.panel = textPanel;
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
            if(isCancelled()) return null;

            String sentance = s.getText();
            charCount += sentance.length();

        }
        if(isCancelled()) return null;
        
        for(Word w: words){
            if(isCancelled()) return null;

            String finWord = w.getText();
            char [] charArray = finWord.toCharArray();
            for(char c: charArray){
                if(isCancelled()) return null;

                c = Character.toLowerCase(c);
                if(!chars.containsKey(c)){
                    chars.put(c, 1);
                }else{
                    chars.put(c, chars.get(c)+1);
                }
                
            }
        }
        if(isCancelled()) return null;

        
        panel.setTotChars("Character Count: " + charCount + "\n");
        LinkedHashMap<Character, Integer> sortedChars = chars.entrySet().stream()
                .sorted(Map.Entry.<Character, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


        int [] arr = new int[words.size()];
        String [] keys = new String[words.size()];
        int i = 0;
        for(char c: sortedChars.keySet()){
            if(isCancelled()) return null;
            
            arr[i] = sortedChars.get(c);
            keys[i] = "" + c;
            i++;
        }
        panel.setBG(arr,keys);

        // Construct statOutput with sorted characters
        StringBuilder statOutput = new StringBuilder();
        sortedChars.forEach((c, count) -> statOutput.append("'" + c + "'").append(" appears ").append(count).append(" times\n"));
        if(isCancelled()) return null;
        return statOutput.toString();
    }

    @Override
    protected void processTask(List<String> textList) {
    }

    @Override
    protected void doneTask(String output) {
        System.out.println("!!>>Done run<<!!");
        panel.setChars(output);

    }

    @Override
    public void parseStarted() {
        panel.setChars("Parse started...");
    }
}