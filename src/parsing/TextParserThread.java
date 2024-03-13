package parsing;

import java.util.ArrayList;

class TextParserThread extends Thread {
    
    private String text;

    private TextParser parser;

    private TokenMatcher[] matchers = {
        new TokenMatcher("[\\w\\-]+(\\.[\\w\\-]+)*@[\\w\\-]+(\\.[\\w\\-]+)*(\\.[\\w\\-]{2,4})", ParseToken.Type.EMAIL),
        new TokenMatcher("[+\\-]?[0-9]+(\\.[0-9]+)?", ParseToken.Type.NUMBER),
        new TokenMatcher( "[a-zA-Z\\-_']+", ParseToken.Type.WORD),
        new TokenMatcher("\\s+", ParseToken.Type.WHITESPACE),
        new TokenMatcher("[\\?!\\.]+", ParseToken.Type.PUNCTUATION),
        new TokenMatcher(".", ParseToken.Type.SYMBOL)
    };

    public TextParserThread(String text, TextParser parser) {
        this.text = text;
        this.parser = parser;

        for(TokenMatcher matcher : matchers) {
            matcher.setInput(text);
        }
    }

    @Override
    public void run() {
        ArrayList<Word> words = new ArrayList<>();
        ArrayList<Sentence> sentences = new ArrayList<>();

        ArrayList<ParseToken> tokens = getTokens();

        for(ParseToken token : tokens) {
            System.out.println(token);
        }

        parser.parseDone(words, sentences);
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

        //Return all tokens found
        return tokens;
    }
}