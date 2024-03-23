package parsing;

public class StringValue {
    private String text;
    private int value;

    public StringValue(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }
}
