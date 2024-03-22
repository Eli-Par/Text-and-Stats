package parsing;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import java.awt.*;

import parsing.testing.StartParseAction;
import parsing.testing.TestParser;

public class StatsGUI {

     private static JTextArea stats = new JTextArea(5,30);
     private static JTextArea wordFrequencies = new JTextArea(5,30);
     private static JTextArea charCount = new JTextArea(5,30);
     private static JTextArea totChars = new JTextArea(5, 10);

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());


        JPanel wordPanel = new JPanel(new GridBagLayout());
        JPanel charPanel = new JPanel(new GridBagLayout());
        JPanel sentPanel = new JPanel(new GridBagLayout());
        JPanel pButton = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTabbedPane tabs = new JTabbedPane();

        
        tabs.addTab("Word Statistics", wordPanel);
        tabs.addTab("Character Statistics", charPanel);
        tabs.addTab("Sentence Statistics", sentPanel);

        //Create the parser
        Parser parser = new TextParser();  

        WordObserver words = new WordObserver(parser);
        WordFrequency freq = new WordFrequency(parser);
        CharCounter chars = new CharCounter(parser);

        parser.addParseObserver(words);
        parser.addParseObserver(freq);
        parser.addParseObserver(chars);
        //An action listener what contains the a class that extends ParseObserver
        StartParseAction parseAction = new StartParseAction(parser);  

        JButton startButton = new JButton("Start Parse");
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        pButton.add(startButton);

        startButton.addActionListener(parseAction);

        gbc.gridx = 1;

        stats.setEditable(false);
        wordFrequencies.setEditable(false);
        charCount.setEditable(false);
        wordPanel.add(wordFrequencies, gbc);

        charPanel.add(totChars);
        gbc.gridx = 1;
        
        JScrollPane scrollPane = new JScrollPane(charCount);
        charPanel.add(scrollPane, gbc);
        gbc.gridx = 0;
        sentPanel.add(stats, gbc);

        frame.add(pButton, BorderLayout.NORTH);
        frame.add(tabs, BorderLayout.CENTER);

        frame.setSize(600, 400); 
        frame.setVisible(true);
    }
    public static void setStats(String s){
        stats.setText(s);
    }
    public static void setFrequencies(String s){
        wordFrequencies.setText(s);
    }
    public static void setChars(String s){
        charCount.setText(s);
    }
    public static void setTotChars(String s){
        totChars.setText(s);
    }
}
