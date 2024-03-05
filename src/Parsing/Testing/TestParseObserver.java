package Parsing.Testing;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import Parsing.*;

public class TestParseObserver extends ParseObserver<String, String> {

    private static final int TEST_DELAY = 5000;

    private static final boolean ENABLE_PRINT = false;

    private static JTextArea textArea = new JTextArea(50, 50);

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);

        //Create the parser
        Parser parser = new TestParser();  

        //An action listener what contains the a class that extends ParseObserver
        StartParseAction parseAction = new StartParseAction(parser);  

        JButton startButton = new JButton("Start Parse");
        panel.add(startButton);
        startButton.addActionListener(parseAction);

        panel.add(textArea);

        frame.pack();
        frame.setVisible(true);
    }

    // Should be a separate class
    // ---------------------------
    // Example of a class that extends ParseObserver

    private Parser parser;

    public TestParseObserver(Parser parser) {
        this.parser = parser;
    }

    @Override
    protected String backgroundTask() {

        //Publishing something will result in processThread being called at the next opportunity
        publish(null);

        ArrayList<Word> words = parser.getWords();
        ArrayList<Sentence> sentences = parser.getSentences();

        String statOutput = "Words:\n";
        for(Word word : words) {
            statOutput += word.getText() + "\n";

            if(isCancelled()) return null;
        }

        //Add artificial delay
        try {
            Thread.sleep(TEST_DELAY);
        } catch(InterruptedException e) {

        }

        // Throughout the background task, isCancelled should be called to check if the task has been stopped
        // If it has, simply returning null is fine since the observer will not allow this result to reach the
        // other methods on the swing event disbatch thread
        if(isCancelled()) return null;

        statOutput += "\nSentences:\n";
        for(Sentence sentence : sentences) {
            statOutput += sentence.getText() + "\n";

            if(isCancelled()) return null;
        }

        if(ENABLE_PRINT) System.out.println("Background task returning");

        return statOutput;
    }

    @Override
    protected void processTask(List<String> textList) {
        textArea.setText("Processing tokens...");
    }

    @Override
    protected void doneTask(String output) {
        if(ENABLE_PRINT) System.out.println("!!>>Done run<<!!");

        textArea.setText(output);
    }

    @Override
    public void parseStarted() {
        textArea.setText("Parse started...");
    }
}
