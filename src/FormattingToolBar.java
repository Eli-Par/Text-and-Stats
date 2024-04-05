import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.StyleConstants;

public class FormattingToolBar extends JPanel implements ChangeListener, CaretListener {
    
    private FileTabList tabList;

    private static final int SPACE_WIDTH = 5;
    private static final int BUTTON_SIZE = 29;

    private JComboBox<String> fontFamilyBox;
    private JComboBox<Integer> fontSizeBox;
    private StylingButton increaseFontButton;
    private StylingButton decreaseFontButton;
    private StylingButton boldButton;
    private StylingButton italicButton;
    private StylingButton underlineButton;
    private StylingButton colorButton;

    private JPanel alignmentPanel;
    private JPanel alignButtonPanel;

    private JLabel alignmentLabel;
    private JRadioButton leftAlignButton;
    private JRadioButton centreAlignButton;
    private JRadioButton rightAlignButton;

    private boolean internalFamilyChange = false;
    private boolean internalSizeChange = false;

    private static ColorDialog colorDialog = null;

    private Action boldAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EditorPanel editorPanel = tabList.getCurrentEditor();
            if(editorPanel != null) {
                editorPanel.getFormatter().toggleFormatSelected(StyleConstants.Bold);
                updateButtonState(StyleConstants.Bold, boldButton);
            }
        }
    };

    private Action italicAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EditorPanel editorPanel = tabList.getCurrentEditor();
            if(editorPanel != null) {
                editorPanel.getFormatter().toggleFormatSelected(StyleConstants.Italic);
                updateButtonState(StyleConstants.Italic, italicButton);
            }
        }
    };

    private Action underlineAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EditorPanel editorPanel = tabList.getCurrentEditor();
            if(editorPanel != null) {
                editorPanel.getFormatter().toggleFormatSelected(StyleConstants.Underline);
                updateButtonState(StyleConstants.Underline, underlineButton);
            }
        }
    };

    public FormattingToolBar(FileTabList tabList) {

        this.setLayout(new GridBagLayout());

        this.tabList = tabList;

        //this.setFloatable(false);

        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        PageSwitchButton editorButton = new PageSwitchButton(tabList, "Editor", "Editor");
        editorButton.setSelected(true);
        viewPanel.add(editorButton);
        viewPanel.add(Box.createHorizontalStrut(SPACE_WIDTH));
        viewPanel.add(new PageSwitchButton(tabList, "Word Stats", "WordStats"));
        viewPanel.add(Box.createHorizontalStrut(SPACE_WIDTH));
        viewPanel.add(new PageSwitchButton(tabList, "Character Stats", "CharStats"));
        viewPanel.add(Box.createHorizontalStrut(SPACE_WIDTH));
        viewPanel.add(new PageSwitchButton(tabList, "Sentence Stats", "SentStats"));

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        gbc2.weightx = 1;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        this.add(viewPanel, gbc2);

        JToolBar bar = new JToolBar();
        gbc2.gridy = 1;
        this.add(bar, gbc2);
        bar.setFloatable(false);

        //this.add(new VerticalLine(20, 30, 1.5f, Color.GRAY));

        bar.add(Box.createHorizontalStrut(SPACE_WIDTH));

        //Formatting buttons
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String availableFonts[] = ge.getAvailableFontFamilyNames();
        String fonts[] = new String[availableFonts.length + 1];

        for(int i = 0; i < availableFonts.length; i++) fonts[i] = availableFonts[i];
        fonts[availableFonts.length - 1] = "---";

        fontFamilyBox = new JComboBox<>(fonts);
        bar.add(fontFamilyBox);
        bar.add(Box.createHorizontalStrut(SPACE_WIDTH));
        fontFamilyBox.addActionListener(this::fontFamilyChange);
        fontFamilyBox.setRenderer(new FontListCellRenderer());

        fontSizeBox = new JComboBox<>(new Integer[] {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72});
        fontSizeBox.setEditable(true);
        bar.add(fontSizeBox);
        bar.add(Box.createHorizontalStrut(SPACE_WIDTH));
        fontSizeBox.addActionListener(this::fontSizeChange);

        increaseFontButton = new StylingButton("F+", BUTTON_SIZE);
        bar.add(increaseFontButton);
        bar.add(Box.createHorizontalStrut(SPACE_WIDTH));
        increaseFontButton.addActionListener(this::increaseFontSize);

        decreaseFontButton = new StylingButton("F-", BUTTON_SIZE);
        bar.add(decreaseFontButton);
        bar.add(Box.createHorizontalStrut(SPACE_WIDTH));
        decreaseFontButton.addActionListener(this::decreaseFontSize);

        boldButton = new StylingButton("<html><b>B</b></html>", BUTTON_SIZE);
        bar.add(boldButton);
        bar.add(Box.createHorizontalStrut(SPACE_WIDTH));
        boldButton.addActionListener(boldAction);
        boldButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control B"), "boldAction");
        boldButton.getActionMap().put("boldAction", boldAction);
        boldButton.setToolTipText("Bold");

        italicButton = new StylingButton("<html><i>I</i></html>", BUTTON_SIZE);
        bar.add(italicButton);
        bar.add(Box.createHorizontalStrut(SPACE_WIDTH));
        italicButton.addActionListener(italicAction);
        italicButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control I"), "italicAction");
        italicButton.getActionMap().put("italicAction", italicAction);
        italicButton.setToolTipText("Italic");

        underlineButton = new StylingButton("<html><u>U</u></html>", BUTTON_SIZE);
        bar.add(underlineButton);
        bar.add(Box.createHorizontalStrut(SPACE_WIDTH));
        underlineButton.addActionListener(underlineAction);
        underlineButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control U"), "underlineAction");
        underlineButton.getActionMap().put("underlineAction", underlineAction);
        underlineButton.setToolTipText("Underline");

        colorButton = new StylingButton("A", BUTTON_SIZE);
        bar.add(colorButton);
        colorButton.addActionListener(this::colorAction);

        bar.add(Box.createHorizontalStrut(SPACE_WIDTH));

        alignmentPanel = new JPanel();
        bar.add(alignmentPanel);
        alignmentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        alignmentLabel = new JLabel("Alignment");
        alignmentLabel.setHorizontalAlignment(SwingConstants.CENTER);
        alignmentPanel.add(alignmentLabel, gbc);

        alignButtonPanel = new JPanel();
        alignButtonPanel.setLayout(new BoxLayout(alignButtonPanel, BoxLayout.X_AXIS));
        ButtonGroup alignmentGroup = new ButtonGroup();
        leftAlignButton = new JRadioButton("Left");
        leftAlignButton.addActionListener(this::leftAlign);
        alignmentGroup.add(leftAlignButton);
        alignButtonPanel.add(leftAlignButton);
        centreAlignButton = new JRadioButton("Centre");
        centreAlignButton.addActionListener(this::centreAlign);
        alignmentGroup.add(centreAlignButton);
        alignButtonPanel.add(centreAlignButton);
        rightAlignButton = new JRadioButton("Right");
        rightAlignButton.addActionListener(this::rightAlign);
        alignmentGroup.add(rightAlignButton);
        alignButtonPanel.add(rightAlignButton);
        gbc.gridy = 1;
        alignmentPanel.add(alignButtonPanel, gbc);

        fontFamilyBox.setMaximumSize(fontFamilyBox.getPreferredSize());
        fontSizeBox.setMaximumSize(fontSizeBox.getPreferredSize());
        alignmentPanel.setMaximumSize(alignmentPanel.getPreferredSize());

        tabList.addChangeListener(this);

        fontFamilyBox.setEnabled(false);
        fontSizeBox.setEnabled(false);
        increaseFontButton.setEnabled(false);
        decreaseFontButton.setEnabled(false);
        boldButton.setEnabled(false);
        italicButton.setEnabled(false);
        underlineButton.setEnabled(false);
        colorButton.setEnabled(false);

        setAlignEnabled(false);

        fontFamilyBox.setFocusable(false);
        fontSizeBox.setFocusable(false);

        leftAlignButton.setSelected(true);

        updateToolbar();
    }

    private void setAlignEnabled(boolean isEnabled) {
        alignmentLabel.setEnabled(isEnabled);
        leftAlignButton.setEnabled(isEnabled);
        centreAlignButton.setEnabled(isEnabled);
        rightAlignButton.setEnabled(isEnabled);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        //Update active state buttons
        updateToolbar();

        if(colorDialog != null) {
            colorDialog.dispose();
            colorDialog = null;
        }
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        updateToolbar();
    }

    public void updateToolbar() {
        //System.out.println("Updating toolbar");

        if(App.isDarkMode) {
            alignButtonPanel.setBackground(new Color(60, 60, 60));
            alignmentPanel.setBackground(new Color(60, 60, 60));
        }
        else {
            alignButtonPanel.setBackground(new Color(210, 210, 210));
            alignmentPanel.setBackground(new Color(210, 210, 210));
        }

        EditorPanel editorPanel = tabList.getCurrentEditor();
        if(editorPanel != null) {
            EditorPanel.Format fileFormat = editorPanel.getFileFormat();

            fontFamilyBox.setEnabled(ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.FontFamily));
            fontSizeBox.setEnabled(ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.FontSize));
            increaseFontButton.setEnabled(ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.FontSize));
            decreaseFontButton.setEnabled(ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.FontSize));
            boldButton.setEnabled(ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.Bold));
            italicButton.setEnabled(ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.Italic));
            underlineButton.setEnabled(ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.Underline));
            colorButton.setEnabled(ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.Foreground));

            setAlignEnabled(ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.Foreground));

            if(ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.FontFamily)) {
                String fontName = editorPanel.getFormatter().getSelectedFontFamily();
                //System.out.println("> " + fontName + " " + (fontName == null));
                internalFamilyChange = true;
                if(fontName != null && !fontName.equals("null")) fontFamilyBox.setSelectedItem(fontName);
                else fontFamilyBox.setSelectedItem("---");
            }

            if(ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.FontSize)) {
                Integer fontSize = editorPanel.getFormatter().getSelectedFontSize();
                //System.out.println("> " + fontName);
                internalSizeChange = true;
                if(fontSize != -1) fontSizeBox.setSelectedItem(fontSize);
                else fontSizeBox.setSelectedItem("");
            }

            if(ValidFormattingSet.isFormatValid(fileFormat, StyleConstants.Alignment)) {
                int align = editorPanel.getFormatter().getAlignment();
                if(align == -1) {
                    // leftAlignButton.setSelected(false);
                    // centreAlignButton.setSelected(false);
                    // rightAlignButton.setSelected(false);
                }
                else if(align == StyleConstants.ALIGN_LEFT) {
                    leftAlignButton.setSelected(true);
                    centreAlignButton.setSelected(false);
                    rightAlignButton.setSelected(false);
                }
                else if(align == StyleConstants.ALIGN_CENTER) {
                    leftAlignButton.setSelected(false);
                    centreAlignButton.setSelected(true);
                    rightAlignButton.setSelected(false);
                }
                else if(align == StyleConstants.ALIGN_RIGHT) {
                    leftAlignButton.setSelected(false);
                    centreAlignButton.setSelected(false);
                    rightAlignButton.setSelected(true);
                }
            }

            updateButtonState(StyleConstants.Bold, boldButton);
            updateButtonState(StyleConstants.Italic, italicButton);
            updateButtonState(StyleConstants.Underline, underlineButton);
            Color textColor = editorPanel.getTextColor();
            colorButton.setText("<html><font color='" + String.format("#%06x", textColor.getRGB() & 0xFFFFFF) + "'><u><b>A</b></u></font></html>");
        }
    }
    public void updateButtonState(Object sc, JButton b){
        EditorPanel editorPanel = tabList.getCurrentEditor();
        if(editorPanel.getFormatter().isSelectionFormatted(sc)){
            if(App.isDarkMode) b.setBackground(new Color(80, 80, 80));
            else b.setBackground(Color.LIGHT_GRAY);
        }else {
            b.setBackground(UIManager.getColor("Button.background")); 
        }
    }

    public void fontFamilyChange(ActionEvent event) {
        if(internalFamilyChange) {
            internalFamilyChange = false;
            //System.out.println("Change ignored: internal");
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

    public void increaseFontSize(ActionEvent event) {
        EditorPanel editorPanel = tabList.getCurrentEditor();
        if(editorPanel != null) {
            editorPanel.getFormatter().increaseFontSize();
            updateToolbar();
        }
    }

    public void decreaseFontSize(ActionEvent event) {
        EditorPanel editorPanel = tabList.getCurrentEditor();
        if(editorPanel != null) {
            editorPanel.getFormatter().decreaseFontSize();
            updateToolbar();
        }
    }

    public void colorAction(ActionEvent event) {
        EditorPanel editorPanel = tabList.getCurrentEditor();
        if(editorPanel != null) {
            if(colorDialog == null) colorDialog = new ColorDialog(editorPanel.getFormatter());
            else colorDialog.setVisible(true);

        }
    }

    public void leftAlign(ActionEvent event) {
        EditorPanel editorPanel = tabList.getCurrentEditor();
        if(editorPanel != null) {
            editorPanel.getFormatter().setAlignment(StyleConstants.ALIGN_LEFT);
        }
    }

    public void centreAlign(ActionEvent event) {
        EditorPanel editorPanel = tabList.getCurrentEditor();
        if(editorPanel != null) {
            editorPanel.getFormatter().setAlignment(StyleConstants.ALIGN_CENTER);
        }
    }

    public void rightAlign(ActionEvent event) {
        EditorPanel editorPanel = tabList.getCurrentEditor();
        if(editorPanel != null) {
            editorPanel.getFormatter().setAlignment(StyleConstants.ALIGN_RIGHT);
        }
    }
}
