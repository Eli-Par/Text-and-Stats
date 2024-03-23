package parsing;

import javax.swing.*;

public class CharPanel extends JPanel{
    private JTextArea charCount = new JTextArea(5,30);
    private JTextArea totChars = new JTextArea(5, 10);
    private Parser parser;

    public CharPanel(Parser parser){
        this.parser = parser;
        CharCounter chars = new CharCounter(parser, this);
        parser.addParseObserver(chars);
        charCount.setEditable(false);
        
        totChars.setEditable(false);
        this.add(totChars);
        
        JScrollPane scrollPane = new JScrollPane(charCount);
        this.add(scrollPane);

    }
    
    public void setChars(String s){
        charCount.setText(s);
    }
    public void setTotChars(String s){
        totChars.setText(s);
    }


}
