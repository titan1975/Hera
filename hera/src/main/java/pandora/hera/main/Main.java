package pandora.hera.main;

import java.util.Random;
import java.util.function.Function;

import pandora.hera.Engine;
import pandora.hera.Transform;
import pandora.hera.lossfunctions.LossFunctions;
import pandora.hera.matrix.Approximator;
import pandora.hera.matrix.Matrix;

public class Main {

	private static Random random = new Random();

	public static void multiplyDouble() {

		Matrix matrix = new Matrix(3, 4, i -> 2 * (i - 6));

		double x = 0.5;
		Matrix result = matrix.apply((index, value) -> x * value);

		System.out.println(matrix.toString());
		System.out.println(result);
	}
	public static void testCrossEntropy() {
		double[] expectedValues = { 1, 0, 0, 0, 0, 1, 0, 1, 0 };
		Matrix expected = new Matrix(3, 3, i -> expectedValues[i]);
           System.out.println(expected );
		Matrix actual = new Matrix(3, 3, i -> 0.05 * i * i).softMax();
		 System.out.println(actual);
		
			Matrix result= LossFunctions.crossEntropy(expected, actual);
			System.out.println(result);
		 

		actual.forEach((row, col, index, value) -> {
			
			double expectedValue = expected.get(index);

			double loss = result.get(col);

			if (expectedValue > 0.9) {
				System.out.println(Math.abs(Math.log(value) + loss) < 0.001);
			}
		});
	}
	
	public static void testApproximator(Matrix ... inMATRIX) {
		
		final int rows =3;
		final int cols=4;
		
		
		System.out.println("Create Input");
		Matrix input = new Matrix(rows, cols , i -> random.nextGaussian()).softMax();
		
		System.out.println("Input");
		System.out.println(input);
		
		 
		// Create the expected Matrix WITH ZEROS
		Matrix expected = new Matrix(rows, cols , i -> 0);
		
		 
		 System.out.println("Expected before the loop");
		 System.out.println(expected);
			
		 
		 // LOOP THE COLS AND RANDOMLY SET 1 TO EACH COL
		 for (int col = 0; col < cols; col++) {
            
			 int randomRow = random.nextInt(rows);
			
			// System.out.println("RandomRow "+randomRow);
		
				
			
			expected.set(randomRow,col,1);
        
			//System.out.println(expected);
		 
		 
		 }

//			public static Matrix crossEntropy(Matrix expected, Matrix actual) {	
//				
//				return actual.apply((index, value)->{
//				
//					return -expected.get(index) * Math.log(value);
//			
//				
//				}).sumColumns();
//			}
		
		 System.out.println("Expected");
		 System.out.println(expected);
	//public static Matrix gradient(Matrix input,Function <Matrix,Matrix> transform)
		 Matrix result=	Approximator.gradient(input, in-> {
			
			 //public static Matrix crossEntropy(Matrix expected, Matrix actual)
			 Matrix crossEntropy =LossFunctions.crossEntropy(expected, in);
			 
			 return crossEntropy;
			
		});
	
	
//	input.forEach((index,value)->{
//		
//		
//		double resultValue = result.get(index);
//		double expectedValue = expected.get(index);
//	
//	
//	
//	
//	
//	});
	System.out.println("result");
		System.out.println(result);
	}
	
	
	public static void startEngineOld() {

		Engine engine = new Engine();

//		engine.add(Transform.DENSE);
//		engine.add(Transform.RELU);
//		engine.add(Transform.DENSE);
//		engine.add(Transform.SOFTMAX);
//		
//		System.out.println(engine);

		int inputSize = 7;

		int layer1Size = 6;

		int layer2Size = 4;

		Matrix input = new Matrix(inputSize, 1, i -> random.nextGaussian());

		Matrix layer1Weights = new Matrix(layer1Size, input.getRows(), i -> random.nextGaussian());

		Matrix layer1Biases = new Matrix(layer1Size, 1, i -> random.nextGaussian());

		
		
		Matrix layer2Weights = new Matrix(layer2Size, layer1Weights.getRows(), i -> random.nextGaussian());

		Matrix layer2Biases = new Matrix(layer2Size, 1, i -> random.nextGaussian());

var output =input;
	
output = layer1Weights.multiply(output);

output = output.modify((row,col,value)-> value+ layer1Biases.get(row));

output = output.modify(value-> value > 0 ? value:0);


output = layer2Weights.multiply(output);

output = output.modify((row,col,value)-> value+ layer2Biases.get(row));

output = output.softMax();
System.out.println(output);
	
	}

	
	
	
	public static Matrix multiplyMatrices(Matrix m1, Matrix m2) {
		Matrix result = null;
		try {
			result = m1.multiply(m2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public static void firstTry() {

		Matrix inputs = new Matrix(1500, 1500, i -> random.nextDouble());
		Matrix weights = new Matrix(1500, 1500, i -> random.nextGaussian());
		Matrix biases = new Matrix(1500, 1, i -> random.nextGaussian());

		try {
			Matrix result = weights.multiply(inputs).modify((row, col, value) -> value + biases.get(row));
			// System.out.println(result);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void testToString() {

		Matrix matrix = new Matrix(3, 4, i -> i * 2);
		System.out.println(matrix.toString());

		String text = matrix.toString();

		var rows = text.split("\n");

		for (var row : rows) {

			var values = row.split("\\s+");

			for (var textValue : values) {

				if (textValue.isBlank())
					continue;
				if (!textValue.equals("+0,00000")) {

					var doubleValue = Double.valueOf(textValue.replace(",", "."));

				}
				System.out.println(textValue.toString());
			}
		}

	}

	public static void testRelu() {

		final int numberOfNeurons = 5;
		final int numberOfInputs = 6;
		final int inputSize = 4;

		Matrix inputs = new Matrix(inputSize, numberOfInputs, i -> random.nextDouble());
		Matrix weights = new Matrix(numberOfNeurons, inputSize, i -> random.nextGaussian());
		Matrix biases = new Matrix(numberOfNeurons, 1, i -> random.nextGaussian());

		try {
			Matrix result1 = weights.multiply(inputs).modify((row, col, value) -> value + biases.get(row));
			Matrix result2 = weights.multiply(inputs).modify((row, col, value) -> value + biases.get(row))
					.modify(value -> value > 0 ? value : 0);

			result2.forEach((index, value) -> {

				System.out.println(index + "," + value);

			});

			System.out.println(inputs);
			System.out.println("\n");
			System.out.println(weights);
			System.out.println("\n");
			System.out.println(biases);
			System.out.println("\n" + "Result");
			System.out.println(result1);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		// testToString();
//		Matrix m1 = new Matrix(1, 4, i -> 2*(i - 4));
//		Matrix m2 = new Matrix(4, 3, i -> 2*(i - 6));
//	    Matrix myResult=	multiplyMatrices(m1, m2);
//		System.out.println(m1);
//		// multiplyDouble();

//		long startTime = System.currentTimeMillis();
//
//		firstTry();
//
//		long endTime = System.currentTimeMillis();
//		long duration = endTime - startTime;
//
//		System.out.println("Time taken to execute firstTry: " + duration + " milliseconds");

		// testRelu();
		//startEngine();
		//testCrossEntropy();
		
		Double [] in = {};
		testApproximator(null);
 
	}
}
