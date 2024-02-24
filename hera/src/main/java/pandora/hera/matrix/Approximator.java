package pandora.hera.matrix;

import java.util.function.Function;

public class Approximator {

public static Matrix gradient(Matrix input,Function <Matrix,Matrix> transform)	{
	
	final double INC = 0.000001;
	
	Matrix loss1 =transform.apply(input);
	
	Matrix result= new Matrix(input.getRows(),input.getCols(),i ->0);
	
//	System.out.println("Approximator gradient INPUT");
//	
//	System.out.println(input);
//	
//	System.out.println("Approximator gradient loss1)");
//	
//	System.out.println(loss1);
	
	input.forEach((row,col,index,value)->{
	
	
		
		Matrix incremented = input.addIncrement(row, col, INC);
		
//		System.out.println("Approximator gradient incremented!! ROW = "+row+"  COL=  "+col+" INDEX= "+index+" VALUE = "+value );
//		System.out.println(incremented);
		
		
		Matrix loss2 = transform.apply(incremented);
		
//		System.out.println("Approximator gradient loss2");
//		System.out.println(loss2);
		
		double rate = (loss2.get(col)-loss1.get(col))/INC;
		
		result.set(row, col, rate);
	
		
	});
	
//	System.out.println("Approximator gradient result");
//	System.out.println(result);
//	
	return result;
}
//	public static Matrix gradient(Matrix input, Function<Matrix, Matrix> transform) {
//		final double INC = 0.000001;
//		
//		Matrix loss1 = transform.apply(input);
//		assert loss1.getCols() == input.getCols() : "Input/loss columns not equal";
//		assert loss1.getRows() == 1 : "Transform does not return one single row";
//		System.out.println(input);
//		System.out.println(loss1);
//		input.forEach((row, col, index, value) -> {
//
//			System.out.printf("%12.5f", value);
//
//			Matrix incremented = input.addIncrement(row, col, INC);
//			
//			Matrix loss2 = transform.apply(incremented);
//		
//			double rate = (loss2.get(col)-loss1.get(col))/INC;
//		
//		});
//
//		return null;
//	}
}
