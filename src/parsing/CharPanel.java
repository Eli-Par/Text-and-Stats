package parsing;

import java.awt.BorderLayout;

import javax.swing.*;

public class CharPanel extends JPanel{
    private JTextArea charCount = new JTextArea(5,30);
    private JTextArea totChars = new JTextArea(5, 10);
    private BarGraph bg;

    public CharPanel(Parser parser){
        setLayout(new BorderLayout());
        CharCounter chars = new CharCounter(parser, this);
        parser.addParseObserver(chars);
        charCount.setEditable(false);
        
        totChars.setEditable(false);
        this.add(totChars, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(charCount);
        this.add(scrollPane, BorderLayout.WEST);

        bg = new BarGraph();
        this.add(bg, BorderLayout.CENTER);

    }
    
    public void setChars(String s){
        charCount.setText(s);
    }
    public void setTotChars(String s){
        totChars.setText(s);
    }

    public void setBG(int [] data, String [] keys){
        bg.setData(data, keys);
    }

}
