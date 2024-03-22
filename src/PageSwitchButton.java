import java.awt.event.*;

import javax.swing.*;

public class PageSwitchButton extends JButton implements ActionListener {
    
    private FileTabList tabList;
    private String cardName;

    public PageSwitchButton(FileTabList tabList, String text, String cardName) {
        this.setText(text);
        this.cardName = cardName;
        this.tabList = tabList;

        this.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        tabList.changeCards(cardName);
    }
}
