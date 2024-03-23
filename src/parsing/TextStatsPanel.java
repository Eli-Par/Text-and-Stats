package parsing;
import javax.swing.*;
import java.awt.*;

import parsing.testing.StartParseAction;
public class TextStatsPanel extends JPanel{
    
    private static JTextArea stats = new JTextArea(5,30);
    private static JTextArea wordFrequencies = new JTextArea(5,30);
    private static JTextArea charCount = new JTextArea(5,30);
    private static JTextArea totChars = new JTextArea(5, 10);
    private static JTextArea totWords = new JTextArea(5, 10);
    private Parser parser;

    public TextStatsPanel(Parser parser){
        this.parser = parser;
        JPanel wordPanel = new JPanel(new GridBagLayout());
        JPanel charPanel = new JPanel(new GridBagLayout());
        JPanel sentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTabbedPane tabs = new JTabbedPane();
   
        tabs.addTab("Word Statistics", wordPanel);
        tabs.addTab("Character Statistics", charPanel);
        tabs.addTab("Sentence Statistics", sentPanel);

        SentObserver words = new SentObserver(parser, this);
        WordFrequency freq = new WordFrequency(parser, this);
        CharCounter chars = new CharCounter(parser, this);

        parser.addParseObserver(words);
        parser.addParseObserver(freq);
        parser.addParseObserver(chars);

        gbc.gridx = 1;
        stats.setEditable(false);
        wordFrequencies.setEditable(false);
        charCount.setEditable(false);

        totWords.setEditable(false);
        wordPanel.add(totWords);
        gbc.gridx = 1;
        JScrollPane sp = new JScrollPane(wordFrequencies);
        wordPanel.add(sp, gbc);
        gbc.gridx =0;

        totChars.setEditable(false);
        charPanel.add(totChars);
        gbc.gridx = 1;
        
        JScrollPane scrollPane = new JScrollPane(charCount);
        charPanel.add(scrollPane, gbc);
        gbc.gridx = 0;
        sentPanel.add(stats, gbc);
        this.add(tabs);
    }

    public void setStats(String s){
        stats.setText(s);
    }
    public void setFrequencies(String s){
        wordFrequencies.setText(s);
    }
    public void setChars(String s){
        charCount.setText(s);
    }
    public void setTotChars(String s){
        totChars.setText(s);
    }
    public void setTotWords(String s){
        totWords.setText(s);
    }

}
