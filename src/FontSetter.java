import javax.swing.*;
import java.awt.*;

public class FontSetter {

    public static final Integer[] FONT_SIZES = {8, 9, 10, 11, 12, 13, 14, 16, 18, 20, 22, 24, 28, 32, 36, 48, 72};

    private FileTabList tabList;

    private final JComboBox<Integer> sizeCB;
    private final JComboBox<String> fontCB;
    private final JPanel panel;

    public FontSetter(FileTabList tabs) {

        tabList = tabs;

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font current = tabList.getFont();

        sizeCB = new JComboBox<>(FONT_SIZES);
        sizeCB.setSelectedItem(current.getSize());
        fontCB = new JComboBox<>(ge.getAvailableFontFamilyNames());
        fontCB.setSelectedItem(current.getFamily());

        panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Font Type: "));
        panel.add(fontCB);
        panel.add(new JLabel("Font Size: "));
        panel.add(sizeCB);

        int op = JOptionPane.showConfirmDialog(null, panel, "Set Font", JOptionPane.OK_CANCEL_OPTION);
        if(op == JOptionPane.OK_OPTION){
            updateFont();
        }
    }

    public void updateFont() {
        tabList.setEditorFont(new Font((String) fontCB.getSelectedItem(), Font.PLAIN, Math.max(1, Math.min((Integer) sizeCB.getSelectedItem(), 96))));
    }
}