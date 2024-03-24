package parsing;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.GridBagLayout;

public class WordPanel extends JPanel{
    private JTextArea totWords = new JTextArea(5, 10);
    private JTextArea wordFrequencies = new JTextArea(5,30);
    JTextField searchField;
    JScrollPane sp;
    private BarGraph bg;
    private HashMap<String, Integer> sortedWordList = new HashMap<String, Integer>();

    private String defaultFrequencyText;

    public WordPanel(Parser parser){
        setLayout(new BorderLayout());
        WordFrequency freq = new WordFrequency(parser, this);
        wordFrequencies.setEditable(false);

        parser.addParseObserver(freq);

        totWords.setEditable(false);
        this.add(totWords, BorderLayout.NORTH);

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        middlePanel.add(new JLabel("Search word: "), gbc);

        searchField = new JTextField();
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        middlePanel.add(searchField, gbc);

        SearchBarListener sbl = new SearchBarListener(this);
        searchField.getDocument().addDocumentListener(sbl);

        sp = new JScrollPane(wordFrequencies);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        middlePanel.add(sp, gbc);
        
        bg = new BarGraph();
        bg.setPreferredSize(new Dimension(450, 400));
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        middlePanel.add(bg, gbc);

        this.add(middlePanel);
    }

    public void setTotWords(String s){
        totWords.setText(s);
    }
    public void setFrequencies(String s){
        wordFrequencies.setText(s);
        wordFrequencies.setCaretPosition(0);
    }
    public String get(){
        return totWords.getText();
    }
    public void setBG(int [] data, String [] keys){
        bg.setData(data, keys);
    }

    public void setWordList(HashMap<String, Integer> wordList){
        this.sortedWordList = wordList;
    }
    public String getSearchWord(){
        return searchField.getText();
    }
    public HashMap<String, Integer> getWordList(){
        return this.sortedWordList;
    }

    public void useDefaultFrequencyText() {
        setFrequencies(defaultFrequencyText);
    }

    public void setDefaultFrequencyText(String text) {
        this.defaultFrequencyText = text;
    }

    public int getViewableLines() {
        Font font = wordFrequencies.getFont();
        FontMetrics fontMetrics = wordFrequencies.getFontMetrics(font);

        int numLines = (int)((double)sp.getHeight() / (fontMetrics.getHeight() * 1.1));

        return numLines;
    }
}
