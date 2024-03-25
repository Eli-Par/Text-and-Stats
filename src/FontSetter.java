import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FontSetter {

    public static final Integer[] FONT_SIZES = {8, 9, 10, 11, 12, 13, 14, 16, 18, 20, 22, 24, 28, 32, 36, 48, 72};

    private FileTabList tabList;

    private final JComboBox<Integer> size;
    private final JTextField fontTF;
    private final JButton button;
    private final JPanel panel;

    public void updateFont(ActionEvent e) {
        tabList.setEditorFont(new Font(fontTF.getText(), Font.PLAIN, Math.max(1, Math.min((Integer) size.getSelectedItem(), 96))));
    }

    public FontSetter(FileTabList tabs) {

        tabList = tabs;

        size = new JComboBox<>(FONT_SIZES);
        size.setSelectedItem(36);

        fontTF = new JTextField("Tahoma", 20);
        button = new JButton("Change");

        panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Font Type: "));
        panel.add(fontTF);
        panel.add(new JLabel("Font Size: "));
        panel.add(size);

        int op = JOptionPane.showConfirmDialog(null, panel, "Set Font", JOptionPane.OK_CANCEL_OPTION);
        if(op == JOptionPane.OK_OPTION){
            updateFont(null);
        }
    }

}