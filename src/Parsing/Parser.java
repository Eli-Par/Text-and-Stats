package Parsing;

import java.util.ArrayList;
import javax.swing.SwingWorker;

/**
 * Interface for a parser that provides methods to register a SwingWorker to be started when the parsing is done, 
 * a method to begin parsing a text file and methods to get the data from the last parse.
 * @author Eli Pardalis
 * @version 1.0.0
 */

public interface Parser {

    public String getText();
    
    /**
     * Begin parsing the text that is specified, calling all registered SwingWorkers once done. 
     * Changes the parsers text to the parameter text.
     * @param text
     */
    public void parse(String text);

    public ArrayList<Word> getWords();

    public ArrayList<Sentence> getSentences();

    /**
     * Add a SwingWorker that will be executed once the parser is finished parsing
     * @param worker
     */
    public void addParseListener(SwingWorker<?, ?> worker);

    public void removeParseListener(SwingWorker<?, ?> worker);
    
}
