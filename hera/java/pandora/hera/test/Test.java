package pandora.hera.test;

import java.util.Random;

import pandora.hera.matrix.Matrix;

public class Test {

	
	Test (Jasper jasper){
	
	
		
	}
	public static void main(String[] args) {
		Random random = new Random();
		Matrix input = new Matrix(2,7 ,i-> random .nextGaussian());
		System.out.println(input);
		
		
		
	var	result= input.modify(i->3);
	
	
	System.out.println(result);
	}

}
