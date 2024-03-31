import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

public class FormattingToolBar extends JToolBar implements ChangeListener {
    
    private FileTabList tabList;

    private static final int SPACE_WIDTH = 5;

    private static final int BUTTON_SIZE = 29;

    public FormattingToolBar(FileTabList tabList) {

        this.tabList = tabList;

        this.setFloatable(false);

        this.add(new PageSwitchButton(tabList, "Editor", "Editor"));
        this.add(Box.createHorizontalStrut(SPACE_WIDTH));
        this.add(new PageSwitchButton(tabList, "Word Stats", "WordStats"));
        this.add(Box.createHorizontalStrut(SPACE_WIDTH));
        this.add(new PageSwitchButton(tabList, "Char Stats", "CharStats"));
        this.add(Box.createHorizontalStrut(SPACE_WIDTH));
        this.add(new PageSwitchButton(tabList, "Sentence Stats", "SentStats"));

        this.add(new VerticalLine(20, 30, 1.5f, Color.GRAY));

        //Formatting buttons
        JComboBox<String> comboBox = new JComboBox<>(new String[] {"Times New Roman"});
        this.add(comboBox);
        this.add(Box.createHorizontalStrut(SPACE_WIDTH));
        this.add(new StylingButton("F+", BUTTON_SIZE));
        this.add(Box.createHorizontalStrut(SPACE_WIDTH));
        this.add(new StylingButton("F-", BUTTON_SIZE));
        this.add(Box.createHorizontalStrut(SPACE_WIDTH));
        this.add(new StylingButton("B", BUTTON_SIZE));
        this.add(Box.createHorizontalStrut(SPACE_WIDTH));
        this.add(new StylingButton("I", BUTTON_SIZE));
        this.add(Box.createHorizontalStrut(SPACE_WIDTH));
        this.add(new StylingButton("U", BUTTON_SIZE));
        this.add(Box.createHorizontalStrut(SPACE_WIDTH));
        this.add(new StylingButton("CS", BUTTON_SIZE));

        comboBox.setMaximumSize(comboBox.getPreferredSize());

        tabList.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        //Update active state buttons
        
    }
}
