package parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
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
        int mostFreqChar = 0;
        int secMost = 0;
        int thirdMost = 0;
        char frequentChar = ' ';
        char secMostFrequent = ' ';
        char thirdMostFrequent = ' ';

        for(Sentence s : sents){
            String sentance = s.getText();
            charCount += sentance.length();

        }
        for(Word w: words){
            String finWord = w.getText();
            char [] charArray = finWord.toCharArray();
            for(char c: charArray){
                c = Character.toLowerCase(c);
                if(!chars.containsKey(c)){
                    chars.put(c, 0);
                }else{
                    chars.put(c, chars.get(c)+1);
                }
                
            }
        }
        for(char c: chars.keySet()){
            if(chars.get(c) >= mostFreqChar){
                thirdMost = secMost;
                thirdMostFrequent = secMostFrequent;
                secMost = mostFreqChar;
                secMostFrequent = frequentChar;
                mostFreqChar = chars.get(c);
                frequentChar = c;
            }else if(chars.get(c)>= secMost){
                thirdMost = secMost;
                thirdMostFrequent = secMostFrequent;
                secMost = chars.get(c);
                secMostFrequent = c;
            }else if(chars.get(c)>= thirdMost){
                thirdMost = chars.get(c);
                thirdMostFrequent = c;
            }
        }

        String statOutput = "Character Count: " + charCount;
        statOutput += "\n\nMost Frequent Char: '" + frequentChar+ "' occurs " + mostFreqChar +" times";
        statOutput += "\n2nd most Frequent Char: '" + secMostFrequent+ "' occurs " + secMost +" times";
        statOutput += "\n3rd most Frequent Char: '" + thirdMostFrequent+ "' occurs " + thirdMost +" times";
        return statOutput;
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