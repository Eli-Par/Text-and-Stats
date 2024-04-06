package parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

class TextParserThread extends Thread {
    
    private String text;

    private TextParser parser;

    private static ArrayList<String> abbreviations = null;

    private ParseToken.Type[] convertToWordTypes = {ParseToken.Type.WORD, ParseToken.Type.NUMBER, ParseToken.Type.SYMBOL, ParseToken.Type.EMAIL, ParseToken.Type.URL, ParseToken.Type.ACRONYM, ParseToken.Type.ABBREVIATION};
    //private ParseToken.Type[] validEndingWordTypes = {ParseToken.Type.WORD, ParseToken.Type.EMAIL, ParseToken.Type.URL, ParseToken.Type.ACRONYM, ParseToken.Type.ABBREVIATION};

    private TokenMatcher[] sectionMatchers = {
        new TokenMatcher("\"[^\"]+\"|“[^”]+”", ParseToken.Type.QUOTE),
        new TokenMatcher("'[^']+'|‘[^’]’", ParseToken.Type.SINGLE_QUOTE),
        new TokenMatcher("\\([^\\)]+\\)", ParseToken.Type.BRACKET)
    };

    //TokenMatchers that will check for the various types of tokens, ordered by priority
    private TokenMatcher[] matchers = {
        new TokenMatcher("[\\w\\-]+(\\.[\\w\\-]+)*@[\\w\\-]+(\\.[\\w\\-]+)*(\\.[\\w\\-]{2,4})", ParseToken.Type.EMAIL),
        new TokenMatcher("(http(s)?:\\/\\/.)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+\\.~#?&//=]*[-a-zA-Z0-9@:%_\\+~#?&//=])", ParseToken.Type.URL),
        new TokenMatcher("[+\\-]?[0-9]+(\\.[0-9]+)?", ParseToken.Type.NUMBER),
        new TokenMatcher( "([a-zA-Z_'’]+([a-zA-Z_'’]+-)?)+", ParseToken.Type.WORD),
        new TokenMatcher("\\s+", ParseToken.Type.WHITESPACE),
        new TokenMatcher("[\\.][\\.]+", ParseToken.Type.SYMBOL),
        new TokenMatcher("[\\?!\\.]+", ParseToken.Type.PUNCTUATION),
        new TokenMatcher(".", ParseToken.Type.SYMBOL)
    };

    private long startTime;

    //Set the text to parse and the TextParser to return to
    public TextParserThread(String text, TextParser parser, long startTime) {
        this.text = text;
        this.parser = parser;
        this.startTime = startTime;
    }

    @Override
    public void run() {
        //Lists for the words and sentences in the input string
        ArrayList<Word> words = new ArrayList<>();
        ArrayList<Sentence> sentences = new ArrayList<>();

        //Get a list of tokens in the string
        ArrayList<ParseToken> tokens = getTokens();

        if(Thread.interrupted()) {
            Thread.currentThread().interrupt();
            return;
        }

        //An array of tokens encountered so far in the sentence
        ArrayList<ParseToken> tokenBuffer = new ArrayList<>();

        //Loop through all tokens
        for(int i = 0; i < tokens.size(); i++) {
            
            if(Thread.interrupted()) {
                Thread.currentThread().interrupt();
                return;
            }

            //Get the current token and add it to the buffer
            ParseToken currToken = tokens.get(i);
            tokenBuffer.add(currToken);

            //System.out.println("Curr: " + currToken);

            //If the token is a punctuation mark, it might be the end of the sentence
            if(currToken.getType() == ParseToken.Type.PUNCTUATION) {

                //If the current token is a '.', there are 2 or more tokens in the buffer, the second most recent token is a 1 letter word or an acrynym, then merge the punctuation into the acrynym
                if(currToken.getText().equals(".") && tokenBuffer.size() > 1 && ((tokenBuffer.get(tokenBuffer.size()-2).getType() == ParseToken.Type.WORD && tokenBuffer.get(tokenBuffer.size()-2).getText().length() == 1) || tokenBuffer.get(tokenBuffer.size()-2).getType() == ParseToken.Type.ACRONYM)) {
                    ////@REMOVED System.out.print("Merge punct: ");
                    mergeAbbreviation(tokenBuffer);

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
                        sentences.add(new Sentence(sentenceString.trim(), wordListToWordArray(newWords)));
                    }

                    //Clear the buffer for the next sentence
                    tokenBuffer.clear();
                }

            }
            else if(currToken.getType() == ParseToken.Type.WORD ) { //If the token is a word, check if it is included in an acrynym

                //If there is 2 or more tokens in the buffer, the second most recent is an acrynym and the current is 1 letter, add it to the acrynym
                if(tokenBuffer.size() > 1 && tokenBuffer.get(tokenBuffer.size()-2).getType() == ParseToken.Type.ACRONYM  && tokenBuffer.get(tokenBuffer.size()-1).getText().length() == 1) {
                    ////@REMOVED System.out.print("Merge word: ");
                    mergeAbbreviation(tokenBuffer);
                }
            }

        }

        //@Temp print statements
        // System.out.println("\nWords:" + words.size());

        // for(Word word : words) {
        //     System.out.println(word);
        // }

        // System.out.println("\n\nSentence:" + sentences.size());

        // for(Sentence s : sentences) {
        //     System.out.println(s + "\n");
        // }
        //@Temp end temporary print statements

        parser.parseDone(words, sentences, startTime);
    }

    //Joins the last 2 tokens in the buffer into a single acrynym token
    private void mergeAbbreviation(ArrayList<ParseToken> tokenBuffer) {
        //Get and remove the current and previous token from the buffer
        ParseToken currToken = tokenBuffer.remove(tokenBuffer.size() - 1);
        ParseToken prevToken = tokenBuffer.remove(tokenBuffer.size() - 1);

        //Combine the tokens text together
        String tokenString = prevToken.getText() + currToken.getText();

        //Create and add a new token to the buffer with the combined text as an acrynym
        ParseToken newToken = new ParseToken(tokenString, ParseToken.Type.ACRONYM);
        tokenBuffer.add(newToken);

        ////@REMOVED System.out.println("New: " + newToken);
    }

    //Takes an ArrayList of tokens and returns a converted ArrayList of Words
    private ArrayList<Word> tokenBufferToWordList(ArrayList<ParseToken> tokens) {
        ArrayList<Word> newWords = new ArrayList<>();

        //Loop through all tokens and if they are a valid word type, add them as a new word
        for(int i = 0; i < tokens.size(); i++) {
            ParseToken token = tokens.get(i);

            if(token.matchesAType(convertToWordTypes)) {
                newWords.add(new Word(token.getText(), token.getType() == ParseToken.Type.NUMBER, token.getType() == ParseToken.Type.SYMBOL, i == tokens.size() - 2, false));
            }
        }

        //Find the last word within the sentence and flag it as the last
        int index = newWords.size() - 1;
        while(index >= 0 && (newWords.get(index).isSymbol() || newWords.get(index).isNumeric())) {
            index--;
        }
        if(index >= 0) newWords.get(index).setIsLastWordOfSentence(true);
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

        //Get all the tokens from the parse string, with punctuation enabled since this isn't an internal recursive call
        getTokens(tokens, text, true);

        //Add an ending punctuation token to the string to ensure an end is found
        tokens.add(new ParseToken("", ParseToken.Type.PUNCTUATION));

        //Return all tokens found
        return tokens;
    }

    //Returns an array list of abbreviation strings
    private ArrayList<String> loadAbbreviations() {
        if(TextParserThread.abbreviations != null) {
            return abbreviations;
        }

        //Load the file and put each line without whitespace into the array list
        try(Scanner fsc = new Scanner(new File(getClass().getClassLoader().getResource("parsing/abbreviations.txt").getPath().replaceAll("%20", " ")))) {
            //System.out.println(new File("src//parsing//abbreviations.txt"));
            ArrayList<String> abbreviations = new ArrayList<>();
            while(fsc.hasNextLine()) {
                String abbrev = fsc.nextLine().trim();
                if(!abbrev.isBlank()) abbreviations.add(abbrev);
            }
            TextParserThread.abbreviations = abbreviations;
            return abbreviations;
        }
        catch(FileNotFoundException exception) {
            return null;
        }
    }

    private void getTokens(ArrayList<ParseToken> tokens, String input, boolean usePunctuation) {

        //Load all abbreviations
        ArrayList<String> abbreviations = loadAbbreviations();

        //Loop through the entire string and keep track of where it is in the parse
        int startIndex = 0;
        while(startIndex < input.length()) {

            if(Thread.interrupted()) {
                Thread.currentThread().interrupt();
                return;
            }

            //Loop through all the TokenMatchers until a valid match is found
            boolean matchFound = false;

            //Check all abbreviations for a match
            if(abbreviations != null) {
                for(String abbreviation : abbreviations) {
                    //If an abbreviation match is found, add it as a token and move to the next index
                    if(isAbbreviationMatch(input, abbreviation, startIndex)) {
                        tokens.add(new ParseToken(input.substring(startIndex, startIndex + abbreviation.length() + 1), ParseToken.Type.ABBREVIATION));
                        startIndex += abbreviation.length() + 1;
                        matchFound = true;
                        break;
                    }
                }
            }

            //If an abbreviation match is found, continue
            if(matchFound) {
                continue;
            }

            //Check if it is an interior section
            for(TokenMatcher sectionMatcher : sectionMatchers) {
                sectionMatcher.setInput(input);
                sectionMatcher.setStart(startIndex);

                //Get the size of the interior match if there is one
                int matchSize = sectionMatcher.prefixMatch();

                //If there is an interior match, get the tokens inside
                if(matchSize > 0) {
                    //Recursively get the tokens inside the section, applying the correct tokens to surround it
                    if(sectionMatcher.getTokenType() == ParseToken.Type.QUOTE) {
                        tokens.add(new ParseToken("\"", ParseToken.Type.SYMBOL));
                        getTokens(tokens, input.substring(startIndex+1, startIndex + matchSize - 1), false);

                        //If a quote ends in a period, then it is the end of a sentence, otherwise the sentence continues most likely afterwards
                        boolean shouldHavePunctuation = tokens.get(tokens.size() - 1).getText().equals(".");
                        
                        tokens.add(new ParseToken("\"", ParseToken.Type.SYMBOL));

                        //If it is the end of the sentence, add a placeholder punctuation
                        if(shouldHavePunctuation && usePunctuation) {
                            tokens.add(new ParseToken("", ParseToken.Type.PUNCTUATION));
                        }
                    }
                    else if(sectionMatcher.getTokenType() == ParseToken.Type.SINGLE_QUOTE) {
                        tokens.add(new ParseToken("'", ParseToken.Type.SYMBOL));
                        getTokens(tokens, input.substring(startIndex+1, startIndex + matchSize - 1), false);
                        tokens.add(new ParseToken("'", ParseToken.Type.SYMBOL));
                    }
                    else if(sectionMatcher.getTokenType() == ParseToken.Type.BRACKET) {
                        tokens.add(new ParseToken("(", ParseToken.Type.SYMBOL));
                        getTokens(tokens, input.substring(startIndex+1, startIndex + matchSize - 1), false);
                        tokens.add(new ParseToken(")", ParseToken.Type.SYMBOL));
                    }
                    
                    //Move the start index to the end of the section
                    startIndex += matchSize;

                    //Note that a match was found
                    matchFound = true;

                    break;
                }
            }

            //Continue since a match was already found
            if(matchFound) continue;

            for(TokenMatcher matcher : matchers) {

                matcher.setInput(input);

                //Update the matchers start position to the current start
                matcher.setStart(startIndex);

                //Retrieve the size of the match if there is one
                int matchSize = matcher.prefixMatch();

                //If there is more than 0 characters in the match, it is a valid match so add that matched substring to the tokens
                if(matchSize > 0) {
                    //Make a new token of the type of the matcher with the text of the match
                    ParseToken newToken = new ParseToken(input.substring(startIndex, startIndex + matchSize), matcher.getTokenType());
                    
                    //If the token is for punctuation and usePunctuation is false, switch the type to INNER_PUNCTUATION which isn't counted as
                    //real punctuation, and thus doesn't end a sentence.
                    if(newToken.getType() == ParseToken.Type.PUNCTUATION && !usePunctuation) {
                        newToken.setType(ParseToken.Type.INNER_PUNCTUATION);
                        
                    }

                    //Add the token to the list and move the start index to the end of the token in the input
                    tokens.add(newToken);
                    startIndex += matchSize;

                    //Mark that a match was found
                    matchFound = true;

                    break;
                }
                else if(matchSize == 0) { //Check that there isn't a problem with the match
                    //@PRINT System.err.println("Matcher " + matcher.getTokenType() + " matched 0 chars at " + startIndex);
                }
            }

            //If no match was found, output an unmatched err without crashing and go to the next character
            //This ensures it is possible to debug without a bizzare input causing an exception
            if(!matchFound) {
                //@PRINT if(startIndex < input.length()) System.err.println("Unmatched input: '" + input.charAt(startIndex) + "' value: " + ((int)input.charAt(startIndex)) + " at index: " + startIndex);
                startIndex++;
            }
            
        }

        
    }

    //Check if the input at the start index matches the abbreviation
    private boolean isAbbreviationMatch(String input, String abbreviation, int startIndex) {
        
        //The abbreviation must be proceeded by either the start of the string or whitespace
        if(startIndex != 0 && !Character.isWhitespace(input.charAt(startIndex - 1))) {
            return false;
        }

        //All letters in the input from the start index must be in bounds and match the abbreviation
        for(int i = 0; i < abbreviation.length(); i++) {
            if(i + startIndex >= input.length() || Character.toLowerCase(input.charAt(startIndex + i)) != Character.toLowerCase(abbreviation.charAt(i))) {
                return false;
            }
        }

        //The found abbreviation match must end in a .
        if(startIndex + abbreviation.length() >= input.length() || input.charAt(startIndex + abbreviation.length()) != '.') {
            return false;
        }
        
        return true;
    }

}