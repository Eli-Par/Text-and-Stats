import java.awt.*;

import javax.swing.*;

public class FontListCellRenderer extends DefaultListCellRenderer {
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof String && !value.equals("---")) {
            String fontFamily = (String) value;
            Font font = new Font(fontFamily, Font.PLAIN, 12); 
            label.setFont(font);
        }

        return label;
    }
}
