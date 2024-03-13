import parsing.*;

public class TextParserTester {
    public static void main(String[] args) {
        TextParser parser = new TextParser();

        parser.parse("Hi everybody. How are you?\nTemperature: -25.5. I can be reached at my pardali1.fake.word@cs.uwindsor.ca & I will not respond!!! I am... really confused. E. Pardalis is in the house! Sentence with no period");
    }
}
