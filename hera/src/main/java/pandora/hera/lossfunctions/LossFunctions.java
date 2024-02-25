package pandora.hera.lossfunctions;


import pandora.hera.matrix.Matrix;
import pandora.hera.matrix.Matrix.IndexValueProducer;


public class LossFunctions {
	
//	// Apply an IndexValueProducer to each element of the matrix
//    public Matrix apply(IndexValueProducer producer) {
//        Matrix result = new Matrix(rows, cols);
//
//        for (int i = 0; i < a.length; i++) {
//            result.a[i] = producer.produce(i, a[i]);
//        }
//
//        return result;
//    }

	
	
	
	
	public static Matrix crossEntropy(Matrix expected, Matrix actual) {	
		
		
//		 System.out.println("CrossEntropy  Expected");
//		 System.out.println(expected);
//		
//		 System.out.println("CrossEntropy Actual");
//		 System.out.println(actual);
//		
		
		Matrix result= actual.apply((index, value)->{
		
		double a = -expected.get(index);
		double b = Math.log(value);
			
			double doubleExpected = a * b;
			
			return doubleExpected ;
	
		
		}).sumColumns();
		
		return result;
	}
}