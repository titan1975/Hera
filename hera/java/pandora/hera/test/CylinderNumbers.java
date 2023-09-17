package pandora.hera.test;

public class CylinderNumbers {

    public static void main(String[] args) {
        int radius = 10; // Cylinder radius
        int paperSize = 30; // Size of the paper

        // Create a 2D array to represent the paper
        char[][] paper = new char[paperSize][paperSize];

        // Initialize the paper with spaces
        for (int i = 0; i < paperSize; i++) {
            for (int j = 0; j < paperSize; j++) {
                paper[i][j] = ' ';
            }
        }

        // Place numbers on the cylinder
        int currentNumber = 1;
        int centerX = paperSize / 2;
        int centerY = paperSize / 2;

        for (int angle = 0; angle < 360; angle += 10) {
            double radians = Math.toRadians(angle);
            int x = (int) (centerX + radius * Math.cos(radians));
            int y = (int) (centerY + radius * Math.sin(radians));

            if (x >= 0 && x < paperSize && y >= 0 && y < paperSize) {
                // Place the number on the paper
                paper[y][x] = Character.forDigit(currentNumber, 10);
                currentNumber++;
            }
        }

        // Print the paper with numbers
        for (int i = 0; i < paperSize; i++) {
            for (int j = 0; j < paperSize; j++) {
                System.out.print(paper[i][j]);
            }
            System.out.println();
        }
    }
}
