package parsing;

import java.util.regex.*;

class TokenMatcher {

    private Pattern pattern;
    private Matcher matcher;
    private String input;
    private ParseToken.Type tokenType;

    //Take the patern and the type and compile the Pattern for the regex
    TokenMatcher(String patternString, ParseToken.Type type) {
        pattern = Pattern.compile(patternString);

        this.tokenType = type;
    }

    //Sets the matchers input string to the specified string
    protected void setInput(String input) {
        this.input = input;

        matcher = pattern.matcher(input);
    }
    
    //Sets the matchers region to match within to be from the specified index until the end of the string
    protected void setStart(int start) {
        matcher.region(start, input.length());
    }

    //Return the size of the match within the string if there is one, otherwise return -1 for no match
    protected int prefixMatch() {
        if(matcher.lookingAt()) {
            return matcher.end() - matcher.start();
        }

        return -1;
    }

    //Return the type of the token this matcher matches
    protected ParseToken.Type getTokenType() {
        return tokenType;
    }
}
