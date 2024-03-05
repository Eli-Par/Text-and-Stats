package Parsing.Testing;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import Parsing.*;

public class TestWorker extends SwingWorker<String, String> {

    private static final int TEST_DELAY = 1000;

    private static JTextArea textArea = new JTextArea(50, 50);

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);

        JButton startButton = new JButton("Start Parse");
        panel.add(startButton);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Parser parser = new TestParser();    

                TestWorker worker = new TestWorker(parser);

                parser.addParseListener(worker);

                parser.parse("This would be the text that is currently hard coded");
            }
        });

        panel.add(textArea);

        frame.pack();
        frame.setVisible(true);
    }

    // Should be a separate class

    private Parser parser;

    public TestWorker(Parser parser) {
        this.parser = parser;
    }

    @Override
    protected String doInBackground() {

        ArrayList<Word> words = parser.getWords();
        ArrayList<Sentence> sentences = parser.getSentences();

        String statOutput = "Words:\n";
        for(Word word : words) {
            statOutput += word.getText() + "\n";
        }

        try {
            Thread.sleep(TEST_DELAY);
        } catch(InterruptedException e ) {

        }

        statOutput += "\nSentences:\n";
        for(Sentence sentence : sentences) {
            statOutput += sentence.getText() + "\n";
        }

        return statOutput;
    }

    @Override
    protected void process(List<String> textList) {

        textArea.setText("Processing...");
    }

    @Override
    protected void done() {
        System.out.println("Done run");

        try {
            textArea.setText(get());
        }
        catch(InterruptedException e) {

        }
        catch (ExecutionException e) {

        }
    }
}
