package util;
import javax.swing.JOptionPane;

public class GUI {

    public static boolean boolQuery(String prompt) {
        return JOptionPane.showConfirmDialog(null, prompt) == JOptionPane.YES_OPTION;
    }

    private GUI() {}

}
