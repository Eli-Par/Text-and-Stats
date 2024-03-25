package parsing;

import java.util.ArrayList;
import java.util.LinkedList;

public class TextParser implements Parser {
    
    private LinkedList<ParseObserver<?, ?>> observers = new LinkedList<>();

    private ArrayList<Word> words = new ArrayList<>();
    private ArrayList<Sentence> sentences = new ArrayList<>();

    private String text;

    private TextParserThread parseThread;

    @Override
    public void parse(String text) {

        long startTime = System.currentTimeMillis();

        this.text = text;

        //Stop all running parse observers, since there is updated information to use instead
        for(ParseObserver<?, ?> observer : observers) {
            observer.stopThread();
        }

        //Stop the current parse if it is active
        if(parseThread != null) {
            parseThread.interrupt();
        }

        //Notify all parse observers that the parse has started
        for(ParseObserver<?, ?> observer : observers) {
            observer.parseStarted();
        }

        //Create a new parse thread to run the parsing on
        parseThread = new TextParserThread(text, this, startTime);

        //Start the new parse thread
        parseThread.start();

    }

    //Called when the parse is done
    //Updates the word and sentence lists and starts all parse observers processing
    protected void parseDone(ArrayList<Word> words, ArrayList<Sentence> sentences, long startTime) {

        this.words = words;
        this.sentences = sentences;

        for(ParseObserver<?, ?> observer : observers) {
            observer.startThread(startTime);
        }
    }

    @Override
    public ArrayList<Word> getWords() {
        return words;
    }

    @Override
    public ArrayList<Sentence> getSentences() {
        return sentences;
    }

    @Override
    public void addParseObserver(ParseObserver<?, ?> observer) {
        observers.add(observer);
    }

    @Override
    public void removeParseObserver(ParseObserver<?, ?> observer) {
        observers.remove(observer);
    }

    @Override
    public String getText() {
        return text;
    }
}