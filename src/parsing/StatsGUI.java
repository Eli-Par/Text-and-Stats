package parsing;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.*;

import parsing.testing.StartParseAction;
import parsing.testing.TestParser;

public class StatsGUI {

     private static JTextArea stats = new JTextArea(5,30);
     private static JTextArea wordFrequencies = new JTextArea(5,30);
     private static JTextArea charCount = new JTextArea(5,30);

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);


        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        frame.add(panel);

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
        panel.add(startButton, gbc);
        startButton.addActionListener(parseAction);

        gbc.gridx = 1;

        stats.setEditable(false);
        wordFrequencies.setEditable(false);
        charCount.setEditable(false);
        panel.add(stats, gbc);
        gbc.gridy = 1;
        panel.add(wordFrequencies, gbc);
        gbc.gridy = 2;
        panel.add(charCount, gbc);

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
}