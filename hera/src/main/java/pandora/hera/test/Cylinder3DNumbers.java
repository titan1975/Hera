package pandora.hera.test;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class Cylinder3DNumbers extends JPanel {

    private int numNumbers = 50;
    private int radius = 100;
    private int centerX = 300;
    private int centerY = 300;

    public Cylinder3DNumbers() {
        setPreferredSize(new Dimension(600, 600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 1; i <= numNumbers; i++) {
            double angle = Math.toRadians((360.0 / numNumbers) * (i - 1));
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));

            // Create a transformation to rotate and position the numbers
            AffineTransform transform = new AffineTransform();
            transform.translate(x, y);
            transform.rotate(angle + Math.PI / 2); // Rotate by 90 degrees

            g2d.setTransform(transform);

            // Display the numbers
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString(Integer.toString(i), -5, 5);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cylindrical Numbers with Swing");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new Cylinder3DNumbers());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
