package parsing;

import javax.swing.*;

public class WordPanel extends JPanel{
    private JTextArea totWords = new JTextArea(5, 10);
    private JTextArea wordFrequencies = new JTextArea(5,30);
    private Parser parser;

    public WordPanel(Parser parser){
        this.parser = parser;
        
        WordFrequency freq = new WordFrequency(parser, this);
        wordFrequencies.setEditable(false);

        parser.addParseObserver(freq);

        totWords.setEditable(false);
        this.add(totWords);

        JScrollPane sp = new JScrollPane(wordFrequencies);
        this.add(sp);
    }

    public void setTotWords(String s){
        totWords.setText(s);
    }
    public void setFrequencies(String s){
        wordFrequencies.setText(s);
    }
    public String get(){
        return totWords.getText();
    }
}
