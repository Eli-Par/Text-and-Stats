package parsing;

/**
 * Contains data about the word. It contains the text for the word, if it is the last word of a sentence, 
 * if it is a number and if it is a symbol that isn't a part of a word (such as &).
 * The property isLastWordOfSentence will be true for only the last word within a sentence (must have isNumeric and isSymbol be false).
 * 
 * @author Eli Pardalis
 * @version 1.0.0
 */

public class Word {
    private String text;
    private boolean isNumeric;
    private boolean isLastOfSentence;
    private boolean isLastWordOfSentence;
    private boolean isSymbol;

    public Word(String text, boolean isNumeric, boolean isSymbol, boolean isLastOfSentence, boolean isLastWordOfSentence) {
        this.text = text;
        this.isNumeric = isNumeric;
        this.isLastOfSentence = isLastOfSentence;
        this.isLastWordOfSentence = isLastWordOfSentence;
        this.isSymbol = isSymbol;
    }

    public String getText() {
        return text;
    }

    public boolean isNumeric() {
        return isNumeric;
    }

    public boolean isSymbol() {
        return isSymbol;

    }

    public boolean isLastOfSentence() {
        return isLastOfSentence;
    }

    public boolean isLastWordOfSentence() {
        return isLastWordOfSentence;
    }

    protected void setIsLastWordOfSentence(boolean b) {
        isLastWordOfSentence = b;
    }

    //Two words are equal if they have the same text and are both the same type of word (normal, numeric, symbol) and are both 
    //at the end of a sentence or not at the end
    @Override
    public boolean equals(Object o) {
        if(o instanceof Word) {
            Word otherWord = (Word) o;
            return text.equals(otherWord.text) && isNumeric == otherWord.isNumeric && isSymbol == otherWord.isSymbol && isLastOfSentence == otherWord.isLastOfSentence;
        }

        return false;
    }

    @Override
    public String toString() {
        return text + " Number: " + isNumeric + " Symbol: " + isSymbol + " Ends sentence: " + isLastOfSentence + " Word ends sentence: " + isLastWordOfSentence;
    }

}
