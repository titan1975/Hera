package pandora.hera;


import java.util.LinkedList;
import java.util.Random;

import pandora.hera.batches.BatchResult;
import pandora.hera.lossfunctions.LossFunction;
import pandora.hera.matrix.Matrix;


public class Engine {
	private Random random = new Random();

	private LinkedList<Transform> transforms = new LinkedList<>();
	private LinkedList<Matrix> weights = new LinkedList<>();
	private LinkedList<Matrix> biases = new LinkedList<>();
    private LossFunction lossFunction= LossFunction.CROSSENTROPY;
	private boolean storeInputError = false;
	
    
    public Matrix runBackwards(BatchResult batchResult, Matrix expected) {
		
	var  transformIt	 = transforms.descendingIterator();
	
	
	if (lossFunction!=LossFunction.CROSSENTROPY|| transforms.getLast()!=Transform.SOFTMAX) {
		
		throw new UnsupportedOperationException("Last fuction must be cross entropy and transform must be softmax");
	}
	
	var ioIt = batchResult.getIo().descendingIterator();
	Matrix softmaxOutput =ioIt.next();
	Matrix error =softmaxOutput.apply((index,value)->
	
	value-expected.get(index)
);
	
	while (transformIt.hasNext()) {
	
		Transform transform = transformIt.next();
		
		switch(transform) {
		case DENSE:
			break;
		case RELU:
			break;
		case SOFTMAX:
			break;
		default:
			throw new UnsupportedOperationException("Not Impelemented");
		}
		
		System.out.println(transform);
	}
	
	if(storeInputError) {
	batchResult.addIo(error);
    }
	
	return error;	
	}
	
	
	
	
	
	
	public void setStoreInputError(boolean storeInputError) {
		this.storeInputError = storeInputError;
	}






	public BatchResult runForwards(Matrix input) {
		BatchResult batchResult = new BatchResult();
		Matrix output = input;
		
		int denseIndex = 0;
		batchResult.addIo(output);
		for (var transform : transforms) {

			if (transform == Transform.DENSE) {
			
				Matrix weight = weights.get(denseIndex);
				Matrix bias = biases.get(denseIndex);

				output = weight.multiply(output).modify((row, col, value) -> value + bias.get(row));
				++denseIndex;
//				System.out.println("          Output from DENSE");
//				System.out.println(output);
//				System.out.println("\n");
		
			
			} else if (transform == Transform.RELU) {

				output = output.modify(value -> value > 0 ? value : 0);
				
//				System.out.println("          Output from RELU");
//				System.out.println(output);
//				System.out.println("\n");

			} else if (transform == Transform.SOFTMAX) {

				output = output.softMax();
//				System.out.println("          Output from WSOFTMAX");
//				System.out.println(output);
//				System.out.println("\n");
			}
		
			batchResult.addIo(output);
		}

		return batchResult;
	}

	public void add(Transform transform, double... params) {

		if (transform == Transform.DENSE) {

			int numberNeurons = (int) params[0];
//            System.out.println("Number of neurons (rows) "+numberNeurons);
//            System.out.println("Number of neurons (rows) "+numberNeurons);
		//	int weightsPerNeuron = weights.size() == 0 ? (int) params[1] : weights.getLast().getRows();
          //  System.out.println("\n");
		
			int weightsPerNeuron;
//private LinkedList<Matrix> weights = new LinkedList<>();
			if (weights.size() == 0) {
			    weightsPerNeuron = (int) params[1];
			} else {
			    weightsPerNeuron = weights.getLast().getRows();
			}
			
			
			//System.out.println("Number of weights Per Neuron  (cols ) "+weightsPerNeuron);
			
			                           // rows        //colums
			Matrix weight = new Matrix(numberNeurons, weightsPerNeuron, i -> random.nextGaussian());
			
//			  System.out.println("             Weight");
//		    	System.out.println(weight);
//			  System.out.println("\n");
			                             //rows   //cols
			  Matrix bias = new Matrix(numberNeurons, 1, i -> random.nextGaussian());

//			  System.out.println("             Bias");
//			  
//			  System.out.println(bias);
//			  System.out.println("\n");
//			
			
			
			weights.add(weight);
			biases.add(bias);

		}

		transforms.add(transform);

	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		int weightIndex = 0;

		for (var t : transforms) {

			builder.append(t);

			if (t == Transform.DENSE) {
				builder.append("   ").append(weights.get(weightIndex).toString(false));
				weightIndex++;

			}

			builder.append("\n");

		}

		return builder.toString();

	}

}