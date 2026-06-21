package client.gui.components;

import javax.swing.*;
import java.awt.*;

public class MetricBar extends JPanel {
    private final JLabel labelTitle;
    private final JProgressBar progressBar;
    private final JLabel labelValue;

    public MetricBar(String title, Color barColor) {
        setLayout(new BorderLayout(10, 5));
        setOpaque(false);

        labelTitle = new JLabel(title);
        labelTitle.setPreferredSize(new Dimension(80, 20));
        labelTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setForeground(barColor);
        progressBar.setStringPainted(false);
        progressBar.setPreferredSize(new Dimension(200, 20));
        
        // Estilização simples para flat appearance
        progressBar.setBorderPainted(false);
        progressBar.setBackground(new Color(60, 63, 65));

        labelValue = new JLabel("0%");
        labelValue.setPreferredSize(new Dimension(40, 20));
        labelValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelValue.setHorizontalAlignment(SwingConstants.RIGHT);

        add(labelTitle, BorderLayout.WEST);
        add(progressBar, BorderLayout.CENTER);
        add(labelValue, BorderLayout.EAST);
    }

    public void setValue(int value) {
        progressBar.setValue(value);
        labelValue.setText(value + "%");
        
        // Update color based on value for warnings
        if (value >= 90) {
            progressBar.setForeground(new Color(231, 76, 60)); // Red
        } else if (value >= 75) {
            progressBar.setForeground(new Color(241, 196, 15)); // Yellow
        } else {
            // reset to green if it's fine, although original color was passed in constructor
            // but just keeping it dynamic is nice.
            progressBar.setForeground(new Color(46, 204, 113)); // Green
        }
    }
}
