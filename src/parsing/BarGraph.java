package parsing;

import javax.swing.*;
import java.awt.*;

public class BarGraph extends JPanel {
    private int[] data;
    private String[] keys;

    public BarGraph(){
        
    }
    public BarGraph(int[] data, String [] keys) {
        this.data = data;
        this.keys = keys;
    }

    @Override
    protected void paintComponent(Graphics g) {

        if(data == null || keys == null) {
            return;
        }
        
        super.paintComponent(g);
        int barWidth = 24;
        int panelHeight = getHeight() - 20;

        int x = 0;
        // Draw bars
        for (int i = 0; i < 10; i++) {
            int barHeight = (int) ((double) data[i] / getMaxValue(data) * panelHeight);
            int y = panelHeight - barHeight;
            g.setColor(Color.blue);
            g.fillRect(x, y, barWidth, barHeight);
            g.setColor(Color.black);
            g.drawRect(x, y, barWidth, barHeight);
            g.setColor(Color.black);
            g.drawString(keys[i], x + barWidth / 2 - keys[i].length(), panelHeight + 15); // Adjust position as needed
        
            x += 16 + barWidth + keys[i].length();
        }
    }

    private int getMaxValue(int[] array) {
        int max = Integer.MIN_VALUE;
        for (int value : array) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
    public void setData(int[] data, String[] keys){
        this.data = data;
        this.keys = keys;
        repaint();
    }
}
