package parsing;

class ParseToken {

    //Enum for all ParseToken types
    protected static enum Type {WORD, NUMBER, SYMBOL, PUNCTUATION, WHITESPACE, EMAIL, INVALID};

    private Type type;
    private String text;
    
    public ParseToken(String text, Type type) {
        this.text = text;
        this.type = type;
    }

    protected String getText() {
        return text;
    }

    protected Type getType() {
        return type;
    }
    
    protected boolean matchesAType(Type[] types) {
        for(Type t : types) {
            if(getType() == t) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "'" + text + "'" + " Type: " + type;
    }
}