package parsing;

import java.awt.*;
import java.awt.GridBagLayout;

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

        
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        JScrollPane scrollPane = new JScrollPane(charCount);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        middlePanel.add(scrollPane, gbc);

        bg = new BarGraph();
        bg.setPreferredSize(new Dimension(450, 400));
        bg.setMinimumSize(new Dimension(350, 400));
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        middlePanel.add(bg, gbc);

        this.add(middlePanel);

    }
    
    public void setChars(String s){
        charCount.setText(s);
        charCount.setCaretPosition(0);
    }
    public void setTotChars(String s){
        totChars.setText(s);
    }

    public void setBG(int [] data, String [] keys){
        bg.setData(data, keys);
    }

}
