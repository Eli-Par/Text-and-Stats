package parsing;

import java.util.ArrayList;

class TextParserThread extends Thread {
    
    private String text;

    private TextParser parser;

    //TokenMatchers that will check for the various types of tokens, ordered by priority
    private TokenMatcher[] matchers = {
        new TokenMatcher("[\\w\\-]+(\\.[\\w\\-]+)*@[\\w\\-]+(\\.[\\w\\-]+)*(\\.[\\w\\-]{2,4})", ParseToken.Type.EMAIL),
        new TokenMatcher("[+\\-]?[0-9]+(\\.[0-9]+)?", ParseToken.Type.NUMBER),
        new TokenMatcher( "([a-zA-Z_']+-?|-?[a-zA-Z_']+)+", ParseToken.Type.WORD),
        new TokenMatcher("\\s+", ParseToken.Type.WHITESPACE),
        new TokenMatcher("[\\.][\\.]+", ParseToken.Type.SYMBOL),
        new TokenMatcher("[\\?!\\.]+", ParseToken.Type.PUNCTUATION),
        new TokenMatcher(".", ParseToken.Type.SYMBOL)
    };

    //Set the text to parse and the TextParser to return to
    public TextParserThread(String text, TextParser parser) {
        this.text = text;
        this.parser = parser;

        //Setup the input for all the TokenMatchers
        for(TokenMatcher matcher : matchers) {
            matcher.setInput(text);
        }
    }

    @Override
    public void run() {
        //Lists for the words and sentences in the input string
        ArrayList<Word> words = new ArrayList<>();
        ArrayList<Sentence> sentences = new ArrayList<>();

        //Get a list of tokens in the string
        ArrayList<ParseToken> tokens = getTokens();

        //An array of tokens encountered so far in the sentence
        ArrayList<ParseToken> tokenBuffer = new ArrayList<>();

        //Loop through all tokens
        for(int i = 0; i < tokens.size(); i++) {
            //Get the current token and add it to the buffer
            ParseToken currToken = tokens.get(i);
            tokenBuffer.add(currToken);

            //System.out.println("> " + currToken);

            //If the token is a punctuation mark, it might be the end of the sentence
            if(currToken.getType() == ParseToken.Type.PUNCTUATION) {

                if(i - 1 >= 0 && tokens.get(i-1).getType() == ParseToken.Type.WORD && tokens.get(i-1).getText().length() == 1) {
                    tokenBuffer.remove(tokenBuffer.size() - 1);
                    ParseToken prevToken = tokenBuffer.remove(tokenBuffer.size() - 1);

                    ParseToken newToken = new ParseToken(prevToken.getText() + currToken.getText(), ParseToken.Type.WORD);
                    tokenBuffer.add(newToken);

                }
                else { //It is the end of the sentence

                    //Convert the buffer of tokens to a list of words and add them to the words array list
                    ArrayList<Word> newWords = tokenBufferToWordList(tokenBuffer);
                    for(Word w : newWords) {
                        words.add(w);
                    }

                    //Convert the buffer into the string for the sentence
                    String sentenceString = tokenListToString(tokenBuffer).trim();

                    //Only add the sentence if it contains actual text
                    if(!sentenceString.isBlank()) 
                    {
                        sentences.add(new Sentence(sentenceString, wordListToWordArray(newWords)));
                    }

                    //Clear the buffer for the next sentence
                    tokenBuffer.clear();
                }

            }

        }

        //@Temp print statements
        System.out.println("\nWords:" + words.size());

        for(Word word : words) {
            System.out.println(word);
        }

        System.out.println("\n\nSentence:" + sentences.size());

        for(Sentence s : sentences) {
            System.out.println(s);
        }
        //@Temp end temporary print statements

        parser.parseDone(words, sentences);
    }

    //Takes an ArrayList of tokens and returns a converted ArrayList of Words
    private ArrayList<Word> tokenBufferToWordList(ArrayList<ParseToken> tokens) {
        ArrayList<Word> newWords = new ArrayList<>();

        //Loop through all tokens and if they are a valid word type, add them as a new word
        for(int i = 0; i < tokens.size(); i++) {
            ParseToken token = tokens.get(i);

            if(token.matchesAType(new ParseToken.Type[] {ParseToken.Type.WORD, ParseToken.Type.NUMBER, ParseToken.Type.SYMBOL, ParseToken.Type.EMAIL})) {
                newWords.add(new Word(token.getText(), token.getType() == ParseToken.Type.NUMBER, token.getType() == ParseToken.Type.SYMBOL, i == tokens.size() - 2));
            }
        }

        return newWords;
    }

    //Take a list of tokens and combine their text into an output string
    private String tokenListToString(ArrayList<ParseToken> tokens) {
        String output = "";
        for(ParseToken token : tokens) {
            output += token.getText();
        }

        return output;
    }

    //Take a array list of words and output an array of the same words
    private Word[] wordListToWordArray(ArrayList<Word> words) {
        Word[] array = new Word[words.size()];

        for(int i = 0; i < words.size(); i++) {
            array[i] = words.get(i);
        }

        return array;
    }

    //Retrieve all ParseTokens associated with the parse string and return them in an ArrayList
    private ArrayList<ParseToken> getTokens() {
        //Array list of tokens
        ArrayList<ParseToken> tokens = new ArrayList<>();

        //Loop through the entire string and keep track of where it is in the parse
        int startIndex = 0;
        while(startIndex < text.length()) {

            //Loop through all the TokenMatchers until a valid match is found
            boolean matchFound = false;
            for(TokenMatcher matcher : matchers) {

                //Update the matchers start position to the current start
                matcher.setStart(startIndex);

                //Retrieve the size of the match if there is one
                int matchSize = matcher.prefixMatch();

                //If there is more than 0 characters in the match, it is a valid match so add that matched substring to the tokens
                if(matchSize > 0) {
                    tokens.add(new ParseToken(text.substring(startIndex, startIndex + matchSize), matcher.getTokenType()));
                    startIndex += matchSize;

                    matchFound = true;

                    break;
                }
                else if(matchSize == 0) { //Check that there isn't a problem with the match
                    System.err.println("Matcher " + matcher.getTokenType() + " matched 0 chars at " + startIndex);
                }
            }

            //If no match was found, output an unmatched err without crashing and go to the next character
            //This ensures it is possible to debug without a bizzare input causing an exception
            if(!matchFound) {
                if(startIndex < text.length()) System.err.println("Unmatched input: '" + text.charAt(startIndex) + "' value: " + ((int)text.charAt(startIndex)) + " at index: " + startIndex);
                startIndex++;
            }
            
        }

        //Add an ending punctuation token to the string to ensure an end is found
        tokens.add(new ParseToken("", ParseToken.Type.PUNCTUATION));

        //Return all tokens found
        return tokens;
    }
}