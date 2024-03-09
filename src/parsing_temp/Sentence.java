package parsing_temp;

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

    //Two sentences are equal if they have the same words in the same order
    @Override
    public boolean equals(Object o) {
        if(o instanceof Sentence) {
            Sentence otherSentence = (Sentence) o;

            //Return false if the two sentences have a different number of words
            if(words.length != otherSentence.words.length) {
                return false;
            }

            //Loop through the list of words in the sentences and if there is any difference the sentences aren't equal
            for(int i = 0; i < words.length; i++) {
                if(!words[i].equals(otherSentence.words[i])) {
                    return false;
                }
            }

            //If all the words are the same, then the sentences are equal
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return text;
    }
}
