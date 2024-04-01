import java.awt.*;

import javax.swing.*;

public class FontListCellRenderer extends DefaultListCellRenderer {
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        Font defaultFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12); // Default fallback font


        if (value instanceof String && !value.equals("---")) {
            String fontFamily = (String) value;
            Font font = new Font(fontFamily, Font.PLAIN, 12); 
            
            if (font.canDisplayUpTo(label.getText()) == -1) {
                label.setFont(font); 
            } else {
                label.setFont(defaultFont); 
            }
        }

        return label;
    }
}
