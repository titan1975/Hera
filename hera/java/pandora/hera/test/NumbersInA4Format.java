package pandora.hera.test;

public class NumbersInA4Format {

    public static void main(String[] args) {
        int start = 1;
        int end = 50;

        // Calculate the number of rows and columns for A4 format
        int rows = 11;  // Approximately 11 rows fit on an A4 page
        int columns = 4;  // Approximately 4 columns fit on an A4 page

        int currentNumber = start;

        for (int row = 1; row <= rows; row++) {
            for (int col = 1; col <= columns; col++) {
                if (currentNumber <= end) {
                    // Print the current number
                    System.out.printf("%-5d", currentNumber);
                    currentNumber++;
                } else {
                    // Print an empty space if there are no more numbers
                    System.out.print("      ");
                }
            }
            // Move to the next row
            System.out.println();
        }
    }
}
