package Parsing.Testing;

import java.util.ArrayList;
import java.util.Arrays;

import Parsing.Sentence;
import Parsing.Word;

class TestParseThread extends Thread {

    private static final int MILLIS_DELAY_WORDS = 100;
    private static final int MILLIS_DELAY_SENTENCES = 200;

    @SuppressWarnings("unused")
    private String text;

    private TestParser parser;

    TestParseThread(String text, TestParser parser) {
        this.text = text;
        this.parser = parser;
        
    }

    @Override
    public void run() {
        ArrayList<Word> words = new ArrayList<>();
        ArrayList<Sentence> sentences = new ArrayList<>();

        Word[] localWords = {
            new Word("It", false, false, false),
            new Word("was", false, false,false),
            new Word("a", false, false,false),
            new Word("lovely", false, false,false),
            new Word("day", false, false,true),
            new Word("Birds", false, false,false),
            new Word("chirping", false, false,false),
            new Word("and", false, false, false),
            new Word("the", false, false,false),
            new Word("sun", false, false,false),
            new Word("shining", false, false,true),
            new Word("The", false, false,false),
            new Word("temperature", false, false,false),
            new Word("is", false, false, false),
            new Word("a", false, false, false),
            new Word("hot", false, false, false),
            new Word("30.5", true, false, false),
            new Word("degrees", false, false, false),
            new Word("celsius", false, false, true)

        };

        Sentence[] localSentences = {
            new Sentence("It was a lovely day.", Arrays.copyOfRange(localWords, 0, 5)),
            new Sentence("Birds chirping and the sun shining!", Arrays.copyOfRange(localWords, 5, 11)),
            new Sentence("The temperature is a hot 30.5 degrees celsius.", Arrays.copyOfRange(localWords, 11, 18))
        };

        for(Word word : localWords) {
            words.add(word);
            try {
                Thread.sleep(MILLIS_DELAY_WORDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(Sentence sentence : localSentences) {
            sentences.add(sentence);
            try {
                Thread.sleep(MILLIS_DELAY_SENTENCES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        parser.parseDone(words, sentences);
    }
}
