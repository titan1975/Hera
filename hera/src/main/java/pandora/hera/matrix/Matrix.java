package pandora.hera.matrix;

import java.util.Arrays;
import java.util.Objects;

public class Matrix {

    private static final String NUMBER_FORMAT = "%+12.5f";
    private static final double TOLERANCE = 0.000000001f;
    private int rows;
    private int cols;

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }
   
    
    public void set(int row, int col, double value) {
		a[row * cols + col] = value;
	}
    
    public double get(int row,int col, double value) {
       return a[row*col+col];
    }
    public double get(int row, int col) {
		return a[row * cols + col];
	}
    public Matrix addIncrement(int row, int col, double inecrement) {
    	
    	Matrix result =apply((index,value)->a[index]);
    	
    	double originalValue = get(row,col);
    	double newlValue = originalValue+inecrement;
    	
    	result.set(row, col, newlValue);
    	
    	return result;
    }
    // Functional Interfaces used for various operations on the matrix
    @FunctionalInterface
    public interface Producer {
        double produce(int index);
    }

    @FunctionalInterface
    public interface IndexValueProducer {
        double produce(int index, double value);
    }

    @FunctionalInterface
    public interface ValueProducer {
        double produce(double value);
    }

    @FunctionalInterface
    public interface IndexValueConsumer {
        void consume(int index, double value);
    }

    @FunctionalInterface
    public interface RowColValueConsumer {
        void consume(int rows, int cols, double value);
    }

    
    @FunctionalInterface
    public interface RowColIndexValueConsumer {
        void consume(int rows, int cols, int index,double value);
    }

    @FunctionalInterface
    public interface RowColProducer {
        double produce(int row, int col, double value);
    }

    private double[] a;

    // Constructor to create a matrix with specified rows and columns
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        a = new double[rows * cols];
    }

    // Constructor to create a matrix with specified rows, columns, and a producer
    public Matrix(int rows, int cols, Producer producer) {
        this(rows, cols);

        for (int i = 0; i < a.length; i++) {
            a[i] = producer.produce(i);
        }
    }

    // Apply an IndexValueProducer to each element of the matrix
    public Matrix apply(IndexValueProducer producer) {
        Matrix result = new Matrix(rows, cols);

        for (int i = 0; i < a.length; i++) {
            result.a[i] = producer.produce(i, a[i]);
        }

        return result;
    }

    // Modify each element of the matrix using a ValueProducer
    public Matrix modify(ValueProducer producer) {
        for (int i = 0; i < a.length; i++) {
            a[i] = producer.produce(a[i]);
        }
        System.out.println(a.length);
        return this;
    }

    // Compute the softmax of the matrix
    public Matrix softMax() {
        Matrix result = new Matrix(rows, cols, i -> Math.exp(a[i]));
        Matrix colsum = result.sumColumns();
        result.modify((row, col, value) -> {
            return value / colsum.get(col);
        });
        return result;
    }

    // Sum the columns of the matrix
    public Matrix sumColumns() {
        Matrix result = new Matrix(1, cols);
        int index = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                result.a[col] += a[index++];
            }
        }
        return result;
    }

    // Modify each element of the matrix using a RowColProducer
    public Matrix modify(RowColProducer producer) {
        int index = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                a[index] = +producer.produce(row, col, a[index]);
                ++index;
            }
        }
        return this;
    }

    // Multiply two matrices
    public Matrix multiply(Matrix m) {
        Matrix result = new Matrix(rows, m.cols);
        assert cols == m.rows : "Cannot multiply; wrong number of rows vs cols";
        for (int row = 0; row < result.rows; row++) {
            for (int col = 0; col < result.cols; col++) {
                for (int n = 0; n < cols; n++) {
                    result.a[row * result.cols + col] += a[row * cols + n] * m.a[col + n * m.cols];
                }
            }
        }
        return result;
    }

    public double get(int index) {
        return a[index];
    }

    // Override hashCode to compare Matrix objects
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(a);
        result = prime * result + Objects.hash(cols, rows);
        return result;
    }

    // Override equals to compare Matrix objects
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Matrix other = (Matrix) obj;

        for (int i = 0; i < a.length; i++) {
            if (Math.abs(a[i] - other.a[i]) > TOLERANCE) {
                return false;
            }
        }

        return true;
    }

    // Override toString to print the matrix
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int index = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                builder.append(String.format(NUMBER_FORMAT, a[index]));
                index++;
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    // An overloaded toString method to optionally show matrix values
    public String toString(boolean showValues) {
        if (showValues) {
            return toString();
        } else {
            return rows + " * " + cols;
        }
    }

    // Perform an action for each index and value in the matrix
    public void forEach(IndexValueConsumer consumer) {
        for (int i = 0; i < a.length; i++) {
            consumer.consume(i, a[i]);
        }
    }

    // Perform an action for each row, column, and value in the matrix
    public void forEach(RowColValueConsumer consumer) {
        int index = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                consumer.consume(row, col, a[index++]);
            }
        }
    }




//Perform an action for each row, column, and value in the matrix
public void forEach(RowColIndexValueConsumer consumer) {
    int index = 0;
    for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
            consumer.consume(row, col,index, a[index++]);
        }
    }
}
}