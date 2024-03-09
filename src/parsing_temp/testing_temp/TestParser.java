package parsing_temp.testing_temp;

import java.util.*;

import parsing_temp.ParseObserver;
import parsing_temp.Parser;
import parsing_temp.Sentence;
import parsing_temp.Word;

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

        if(ENABLE_PRINT) System.out.println("Parse started");

        for(ParseObserver<?, ?> observer : observers) {
            observer.stopThread();
        }

        if(parseThread != null) {
            if(ENABLE_PRINT) System.out.println("Parse thread stopped");
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
            if(ENABLE_PRINT) System.out.println("Done parsing, starting observers, num words: " + words.size());
            observer.startThread();
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
