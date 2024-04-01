import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorDialog extends JDialog {

    private static JColorChooser colorChooser = null;

    private JLabel previewLabel = new JLabel(" This is sample text! ");

    private JDialog dialog;

    public ColorDialog(DocumentFormatter formatter) {
        super((JFrame) null, "Pick a color");
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        dialog = this;

        this.setLayout(new GridBagLayout());

        if(colorChooser == null) {
            colorChooser = new JColorChooser();

            AbstractColorChooserPanel[] colorPanels = colorChooser.getChooserPanels();
            for(AbstractColorChooserPanel cp : colorPanels) {
               cp.setColorTransparencySelectionEnabled(false);
            }

            JPanel panel = new JPanel();
            panel.add(previewLabel);
            colorChooser.setPreviewPanel(panel);

            previewLabel.setOpaque(true);
            previewLabel.setBackground(Color.WHITE);
            previewLabel.setFont(new Font("Sans-Serif", Font.PLAIN, 16));

            colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    previewLabel.setForeground(colorChooser.getColor());
                }
            });
        }
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        this.add(colorChooser, gbc);

        JButton okayButton = new JButton("Okay");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(okayButton, gbc);
        okayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formatter.setSelectedTextColor(colorChooser.getColor());
                dialog.dispose();
            }
        });

        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);

    }
}
