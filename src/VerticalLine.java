import java.awt.*;

import javax.swing.*;

public class VerticalLine extends JComponent{

    private int length;
    private int width;
    private float thickness;
    private Color color;

    public VerticalLine(int width, int length, float thickness, Color color) {
        this.length = length;
        this.width = width;
        this.thickness = thickness;
        this.color = color;

        setPreferredSize(new Dimension(width, length));
        setMaximumSize(new Dimension(width, length));
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setStroke(new BasicStroke(thickness));
        g2d.setColor(color);
        g2d.drawLine(width / 2, 0, width / 2, length);
    }
}
