import parsing.*;

public class TextParserTester {
    public static void main(String[] args) {
        TextParser parser = new TextParser();

        //parser.parse("A.C.A.D.F. is an abbreviation. I find it quite weird.");
        parser.parse("I said, \"She says, 'Quotes inside!', which surprises me.\"");
        //parser.parse("I say, \"Hello\". (Hi. More words.) and a few more. I say, 'Single quotes!'.");
        //parser.parse("I say, \"Hello my friend. How are you\". \"Doing well!\", they respond.");
        //parser.parse("Hi everybody. How are you?\nTemperature: -25.5. I can be reached at my pardali1.fake.word@cs.uwindsor.ca & I will not respond!!! I am... really confused. E. Pardalis is in the house! Sentence with no period");
    }
}
