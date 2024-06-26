package parsing.testing;

import java.util.*;

import parsing.ParseObserver;
import parsing.Parser;
import parsing.Sentence;
import parsing.Word;

public class TestParser implements Parser {

    private static final boolean ENABLE_PRINT = false;

    private LinkedList<ParseObserver<?, ?>> observers = new LinkedList<>();

    private ArrayList<Word> words = new ArrayList<>();
    private ArrayList<Sentence> sentences = new ArrayList<>();

    private String text;

    private TestParseThread parseThread;

    @Override
    public void parse(String text) {

        this.text = text;

        if(ENABLE_PRINT) //@REMOVED System.out.println("Parse started");

        for(ParseObserver<?, ?> observer : observers) {
            observer.stopThread();
        }

        if(parseThread != null) {
            if(ENABLE_PRINT) //@REMOVED System.out.println("Parse thread stopped");
            parseThread.interrupt();
        }

        for(ParseObserver<?, ?> observer : observers) {
            observer.parseStarted();
        }

        parseThread = new TestParseThread(text, this);

        parseThread.start();

    }

    protected void parseDone(ArrayList<Word> words, ArrayList<Sentence> sentences) {
        this.words = words;
        this.sentences = sentences;

        for(ParseObserver<?, ?> observer : observers) {
            if(ENABLE_PRINT) //@REMOVED System.out.println("Done parsing, starting observers, num words: " + words.size());
            observer.startThread(10);
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
