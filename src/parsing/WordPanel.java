package parsing;

import java.awt.*;

import javax.swing.*;

public class WordPanel extends JPanel{
    private JTextArea totWords = new JTextArea(5, 10);
    private JTextArea wordFrequencies = new JTextArea(5,30);
    private BarGraph bg;

    public WordPanel(Parser parser){
        setLayout(new BorderLayout());
        WordFrequency freq = new WordFrequency(parser, this);
        wordFrequencies.setEditable(false);

        parser.addParseObserver(freq);

        totWords.setEditable(false);
        this.add(totWords, BorderLayout.NORTH);

        JScrollPane sp = new JScrollPane(wordFrequencies);
        this.add(sp, BorderLayout.WEST);
        
        bg = new BarGraph();
        this.add(bg, BorderLayout.CENTER);
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
    public void setBG(int [] data, String [] keys){
        bg.setData(data, keys);
    }
}
