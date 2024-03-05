package Parsing.Testing;

import java.util.*;

import javax.swing.SwingWorker;

import Parsing.Parser;
import Parsing.Sentence;
import Parsing.Word;

public class TestParser implements Parser {

    private LinkedList<SwingWorker<?, ?>> workers = new LinkedList<>();

    private ArrayList<Word> words = new ArrayList<>();
    private ArrayList<Sentence> sentences = new ArrayList<>();

    private String text;

    @Override
    public void parse(String text) {

        this.text = text;

        TestParseThread parseThread = new TestParseThread(text, this);

        parseThread.start();

    }

    protected void parseDone(ArrayList<Word> words, ArrayList<Sentence> sentences) {
        this.words = words;
        this.sentences = sentences;

        for(SwingWorker<?, ?> worker : workers) {
            System.out.println("Done parsing, starting workers, num words: " + words.size());
            worker.execute();
            System.out.println("Worker is " + worker.isCancelled());
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
    public void addParseListener(SwingWorker<?, ?> worker) {
        workers.add(worker);
    }

    @Override
    public void removeParseListener(SwingWorker<?, ?> worker) {
        workers.remove(worker);
    }

    @Override
    public String getText() {
        return text;
    }
    
}
