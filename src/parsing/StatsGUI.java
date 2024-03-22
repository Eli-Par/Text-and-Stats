package parsing;

import javax.swing.*;

import parsing.testing.StartParseAction;

import java.awt.*;


public class StatsGUI {


    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        //Create the parser
        Parser parser = new TextParser(); 
        JPanel p = new JPanel(); 

        TextStatsPanel ts = new TextStatsPanel(parser);
        //An action listener what contains the a class that extends ParseObserver
        StartParseAction parseAction = new StartParseAction(parser);  

        JButton startButton = new JButton("Start Parse");
        p.add(startButton);

        startButton.addActionListener(parseAction);

        frame.add(p, BorderLayout.NORTH);
        frame.add(ts, BorderLayout.CENTER);

        frame.setSize(600, 400); 
        frame.setVisible(true);
    }
}
