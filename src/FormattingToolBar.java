import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.StyleConstants;

public class FormattingToolBar extends JToolBar implements ChangeListener, CaretListener {
    
    private FileTabList tabList;

    private static final int SPACE_WIDTH = 5;
    private static final int BUTTON_SIZE = 29;

    private JComboBox<String> fontFamilyBox;
    private JComboBox<Integer> fontSizeBox;
    private StylingButton boldButton;
    private StylingButton italicButton;
    private StylingButton underlineButton;

    private boolean internalFamilyChange = false;
    private boolean internalSizeChange = false;

    private Action boldAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EditorPanel editorPanel = tabList.getCurrentEditor();
            if(editorPanel != null) {
                editorPanel.getFormatter().toggleFormatSelected(StyleConstants.Bold);
            }
        }
    };

    private Action italicAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EditorPanel editorPanel = tabList.getCurrentEditor();
            if(editorPanel != null) {
                editorPanel.getFormatter().toggleFormatSelected(StyleConstants.Italic);
            }
        }
    };

    private Action underlineAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EditorPanel editorPanel = tabList.getCurrentEditor();
            if(editorPanel != null) {
                editorPanel.getFormatter().toggleFormatSelected(StyleConstants.Underline);
            }
        }
    };

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
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String availableFonts[] = ge.getAvailableFontFamilyNames();
        String fonts[] = new String[availableFonts.length + 1];

        for(int i = 0; i < availableFonts.length; i++) fonts[i] = availableFonts[i];
        fonts[availableFonts.length - 1] = "---";

        fontFamilyBox = new JComboBox<>(fonts);
        this.add(fontFamilyBox);
        this.add(Box.createHorizontalStrut(SPACE_WIDTH));
        fontFamilyBox.addActionListener(this::fontFamilyChange);
        fontFamilyBox.setRenderer(new FontListCellRenderer());

        fontSizeBox = new JComboBox<>(new Integer[] {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72});
        fontSizeBox.setEditable(true);
        this.add(fontSizeBox);
        this.add(Box.createHorizontalStrut(SPACE_WIDTH));
        fontSizeBox.addActionListener(this::fontSizeChange);

        this.add(new StylingButton("F+", BUTTON_SIZE));
        this.add(Box.createHorizontalStrut(SPACE_WIDTH));

        this.add(new StylingButton("F-", BUTTON_SIZE));
        this.add(Box.createHorizontalStrut(SPACE_WIDTH));

        boldButton = new StylingButton("<html><b>B</b></html>", BUTTON_SIZE);
        this.add(boldButton);
        this.add(Box.createHorizontalStrut(SPACE_WIDTH));
        boldButton.addActionListener(boldAction);
        boldButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control B"), "boldAction");
        boldButton.getActionMap().put("boldAction", boldAction);
        boldButton.setToolTipText("Bold");

        italicButton = new StylingButton("<html><i>I</i></html>", BUTTON_SIZE);
        this.add(italicButton);
        this.add(Box.createHorizontalStrut(SPACE_WIDTH));
        italicButton.addActionListener(italicAction);
        italicButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control I"), "italicAction");
        italicButton.getActionMap().put("italicAction", italicAction);
        italicButton.setToolTipText("Italic");

        underlineButton = new StylingButton("<html><u>U</u></html>", BUTTON_SIZE);
        this.add(underlineButton);
        this.add(Box.createHorizontalStrut(SPACE_WIDTH));
        underlineButton.addActionListener(underlineAction);
        underlineButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control U"), "underlineAction");
        underlineButton.getActionMap().put("underlineAction", underlineAction);
        underlineButton.setToolTipText("Underline");

        this.add(new StylingButton("CS", BUTTON_SIZE));

        fontFamilyBox.setMaximumSize(fontFamilyBox.getPreferredSize());
        fontSizeBox.setMaximumSize(fontSizeBox.getPreferredSize());

        tabList.addChangeListener(this);

        fontFamilyBox.setEnabled(false);
        fontSizeBox.setEnabled(false);
        boldButton.setEnabled(false);
        italicButton.setEnabled(false);
        underlineButton.setEnabled(false);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        //Update active state buttons
        updateToolbar();
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        updateToolbar();
    }

    public void updateToolbar() {
        //System.out.println("Updating toolbar");
        EditorPanel editorPanel = tabList.getCurrentEditor();
        if(editorPanel != null) {
            EditorPanel.Format fileFormat = editorPanel.getFileFormat();

            fontFamilyBox.setEnabled(ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.FontFamily));
            fontSizeBox.setEnabled(ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.FontSize));
            boldButton.setEnabled(ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.Bold));
            italicButton.setEnabled(ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.Italic));
            underlineButton.setEnabled(ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.Underline));

            if(ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.FontFamily)) {
                String fontName = editorPanel.getFormatter().getSelectedFontFamily();
                //System.out.println("> " + fontName);
                internalFamilyChange = true;
                if(fontName != null) fontFamilyBox.setSelectedItem(fontName);
                else fontFamilyBox.setSelectedItem("---");
            }

            if(ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.FontSize)) {
                Integer fontSize = editorPanel.getFormatter().getSelectedFontSize();
                //System.out.println("> " + fontName);
                internalSizeChange = true;
                if(fontSize != -1) fontSizeBox.setSelectedItem(fontSize);
                else fontSizeBox.setSelectedItem("");
            }
        }
    }

    public void fontFamilyChange(ActionEvent event) {
        if(internalFamilyChange) {
            internalFamilyChange = false;
            return;
        }

        if(((String)fontFamilyBox.getSelectedItem()).equals("---")) {
            return;
        }

        EditorPanel editorPanel = tabList.getCurrentEditor();
        if(editorPanel != null) {
            editorPanel.getFormatter().setSelectedFontFamily((String) fontFamilyBox.getSelectedItem());
        }
    }

    public void fontSizeChange(ActionEvent event) {
        if(internalSizeChange) {
            internalSizeChange = false;
            return;
        }

        EditorPanel editorPanel = tabList.getCurrentEditor();
        if(editorPanel != null) {
            if(fontSizeBox.getSelectedItem() instanceof Integer size) {
                if(size <= 0) size = 1;
                editorPanel.getFormatter().setSelectedFontSize(size);
            }
            else {
                fontSizeBox.setSelectedIndex(0);
            }
        }
    }
}
