import java.awt.Dimension;

import javax.swing.*;

public class StylingButton extends JButton {

    public StylingButton(String title, int size) {
        super(title);

        this.setPreferredSize(new Dimension(size, size));
        this.setMaximumSize(new Dimension(size, size));
    }
    public StylingButton(Icon icon, int size){
        super(icon);

        this.setPreferredSize(new Dimension(size, size));
        this.setMaximumSize(new Dimension(size, size));

    }

}
