package parsing;


import javax.swing.*;


public class SentPanel extends JPanel{
    private static JTextArea stats = new JTextArea(5,30);
    private Parser parser;
    public SentPanel(Parser parser){
        this.parser = parser;
        
        SentObserver sent = new SentObserver(parser, this);
        
        parser.addParseObserver(sent);
        
        stats.setEditable(false);
        this.add(stats);

    }
    public void setStats(String s){
        stats.setText(s);
    }
}

