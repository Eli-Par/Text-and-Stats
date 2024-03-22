import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FontSetter {

    public static final Integer[]FONT_SIZES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16, 18, 20, 22, 24, 28, 32, 36, 48, 72};

    private FileTabList tabList;

    private final JComboBox<Integer>sizeBox;

    private final JTextField familyField;

    private final JFrame frame;

    public void updateFont(ActionEvent e) {
        tabList.setEditorFont(new Font(familyField.getText(), Font.PLAIN, (Integer) sizeBox.getSelectedItem()));
        frame.dispose();
    }

    public FontSetter(FileTabList tabs) {

        tabList = tabs;

        frame = new JFrame("Set Font");
        JPanel panel = new JPanel();
        Font f = new Font("Tahoma", Font.PLAIN, 36);

        sizeBox = new JComboBox<>(FONT_SIZES);
        familyField = new JTextField(20);
        JButton setButton = new JButton("Change Font");

        sizeBox.setFont(f);
        familyField.setFont(f);
        setButton.setFont(f);

        sizeBox.setEditable(true);
        setButton.addActionListener(this::updateFont);

        panel.add(familyField);
        panel.add(sizeBox);
        panel.add(setButton);

        frame.add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

    }

}