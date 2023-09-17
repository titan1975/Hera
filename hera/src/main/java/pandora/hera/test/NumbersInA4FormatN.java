package pandora.hera.test;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class NumbersInA4FormatN {

    public static void main(String[] args) {
        // Create a new JFrame
        JFrame frame = new JFrame("Numbers in A4 Format");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a JPanel to display the numbers
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Set the font and color
                g.setFont(new Font("Arial", Font.PLAIN, 24));
                g.setColor(Color.BLACK);

                // Calculate the position to start displaying numbers
                int x = 100; // X-coordinate
                int y = 100; // Y-coordinate

                // Generate and display numbers from 1 to 50
                for (int i = 1; i <= 50; i++) {
                    String number = Integer.toString(i);
                    g.drawString(number, x, y);

                    // Move to the next position
                    x += 60; // Adjust as needed for spacing
                    if (i % 10 == 0) {
                        x = 100;
                        y += 60; // Adjust as needed for spacing
                    }
                }
            }
        };

        // Add the panel to the frame
        frame.add(panel);

        // Set the size of the panel to A4 size (595 x 842 pixels)
        panel.setPreferredSize(new Dimension(595, 842));

        // Pack the frame to fit the panel
        frame.pack();

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        // Make the frame visible
        frame.setVisible(true);
    }
}
