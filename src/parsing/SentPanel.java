package parsing;


import java.awt.BorderLayout;

import javax.swing.*;


public class SentPanel extends JPanel{
    private JTextArea stats = new JTextArea(5,30);
    public SentPanel(Parser parser){
        setLayout(new BorderLayout());
        SentObserver sent = new SentObserver(parser, this);
        
        parser.addParseObserver(sent);
        
        stats.setEditable(false);
        JScrollPane sp = new JScrollPane(stats);
        this.add(sp, BorderLayout.CENTER);

    }
    public void setStats(String s){
        stats.setText(s);
    }
}

