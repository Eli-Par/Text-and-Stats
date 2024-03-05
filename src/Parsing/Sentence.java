package parsing;

/**
 * Contains information about a sentence. It contains the text and an array of Word objects that compose the sentence.
 * 
 * @author Eli Pardalis
 * @version 1.0.0
 */

public class Sentence {
    private String text;
    private Word[] words;

    public Sentence(String text, Word[] words) {
        this.text = text;
        this.words = words;
    }

    public String getText() {
        return text;
    }

    public Word[] getWords() {
        return words;
    }
}
