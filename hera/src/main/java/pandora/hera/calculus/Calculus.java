package pandora.hera.calculus;

import java.util.function.DoubleFunction;

public class Calculus {
	
	
	private static final double INC = 0.00001;
	
	public static double func1(double x) {
		
		return 3.7 * x+5.3;
		
	}
	
public static double func2(double x) {
		
		return x * x;
		
	}

public static double func3(double x) {
	
	return func2(func1(x));
	
}
	
public static double func3(double y1,double y2) {
	
	return y1 * y2 +4.34 * y1;
	
}
	
public static double differentiate(DoubleFunction<Double> func,double x) {
	
	double output1 = func.apply(x);
	double output2 = func.apply(x+INC);
	
	return  (output2-output1)/INC;
	
}	

public static void main(String[] args) {
		
// for (double x=-2;x<2;x+=0.1)	{
//	 
//	double gradient = differentiate(Calculus::func2, x);
//	 
//	 System.out.printf("%.2f\t%.2f\n",x,gradient);
//	 
// }
double x=2.76;
double y1=func1(x);
double y2 = func2(x);
double z= func3(y1,y2);

double dy1dx = differentiate(Calculus::func1, x);
double dy2dx = differentiate(Calculus::func2, x);
double dzdy1 = differentiate(y->Calculus.func3(y, y2), y1);
double dzdy2 = differentiate(y->Calculus.func3(y1, y), y2);

double dzdx= dzdy1*dy1dx+ dzdy2*dy2dx;
System.out.println(dzdx);

}

}
