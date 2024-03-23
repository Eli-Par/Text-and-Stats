package parsing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;

public class WordPanel extends JPanel{
    private JTextArea totWords = new JTextArea(5, 10);
    private JTextArea wordFrequencies = new JTextArea(5,30);
    private Parser parser;

    public WordPanel(Parser parser){
        this.parser = parser;

        this.setLayout(new GridBagLayout());
        
        WordFrequency freq = new WordFrequency(parser, this);
        wordFrequencies.setEditable(false);

        parser.addParseObserver(freq);

        totWords.setEditable(false);
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.weightx = 1;
        gbc1.weighty = 1;
        gbc1.fill = GridBagConstraints.BOTH;
        gbc1.insets = new Insets(5, 5, 5, 5);
        this.add(totWords, gbc1);

        JScrollPane sp = new JScrollPane(wordFrequencies);
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 1;
        gbc2.gridy = 0;
        gbc2.weightx = 1;
        gbc2.weighty = 1;
        gbc2.fill = GridBagConstraints.BOTH;
        gbc2.insets = new Insets(5, 5, 5, 5);
        this.add(sp, gbc2);
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
