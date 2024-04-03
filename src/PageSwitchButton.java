import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class PageSwitchButton extends JToggleButton implements ActionListener {
    
    private FileTabList tabList;
    private String cardName;

    private static ArrayList<PageSwitchButton> switchButtons = new ArrayList<>();

    public PageSwitchButton(FileTabList tabList, String text, String cardName) {
        this.setText(text);
        this.cardName = cardName;
        this.tabList = tabList;

        this.addActionListener(this);

        switchButtons.add(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        tabList.changeCards(cardName);
        for(PageSwitchButton button : switchButtons) {
            if(button != this) button.setSelected(false);
        }
        this.setSelected(true);
    }
}
