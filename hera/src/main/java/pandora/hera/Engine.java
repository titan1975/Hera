package pandora.hera;


import java.util.LinkedList;
import java.util.Random;

import pandora.hera.batches.BatchResult;
import pandora.hera.lossfunctions.LossFunction;
import pandora.hera.lossfunctions.LossFunctions;
import pandora.hera.matrix.Matrix;


public class Engine {
	private Random random = new Random();

	private LinkedList<Transform> transforms = new LinkedList<>();
	private LinkedList<Matrix> weights = new LinkedList<>();
	private LinkedList<Matrix> biases = new LinkedList<>();
    private LossFunction lossFunction= LossFunction.CROSSENTROPY;
	private boolean storeInputError = false;
	
    
	public void evaluate (BatchResult batchResult, Matrix expected) {
		
		if (lossFunction!=LossFunction.CROSSENTROPY) {
			
			throw new UnsupportedOperationException("Only cross entropy is supported");
		}
		
	double loss =	LossFunctions.crossEntropy(expected,batchResult.getOutput()).averageColumn().get(0);
	
	Matrix predictions = batchResult.getOutput().getGreatestRowNumbers();
	Matrix actual = expected.getGreatestRowNumbers();
	
	int correct =0;
	
	
	for (int i = 0; i < actual.getCols(); i++) {
		
		if ((int)actual.get(i)==(int)predictions.get(i)) {
			
			++correct;
		}
		
	}
	
	double percentCorrect =(100* correct)/actual.getCols();
	
	batchResult.setPercentCorrect(percentCorrect);
	
	batchResult.setLoss(loss);
	}
	
	public void adjust(BatchResult batchResult, Matrix expected) {
		
		var weightInputs = batchResult.getWeightInputs();
		var weightErrors = batchResult.getWeightErrors();
		
		assert weightInputs.size()==weightErrors.size();
		assert weightInputs.size()==weights.size();
	}
   
	
public void adjust(BatchResult batchResult, double learnRate) {
		
		var weightInputs = batchResult.getWeightInputs();
		var weightErrors = batchResult.getWeightErrors();
		
		assert weightInputs.size()==weightErrors.size();
		assert weightInputs.size()==weights.size();
		
		for (int i = 0; i < weights.size(); i++) {

			var weight = weights.get(i);
			var bias = biases.get(i);
			var error= weightErrors.get(i);
			var input= weightInputs.get(i);
					
			assert weight.getCols()==input.getRows();
			
			var weightAdjust= error.multiply(input.transpose());
			var biasAdjust= error.averageColumn();
			
			double rate = learnRate/input.getCols();
			weight.modify((index,value)->rate *weightAdjust.get(index));
			
			bias.modify((row,col,value)->rate *biasAdjust.get(row));
		}	

	}
	
	public Matrix runBackwards(BatchResult batchResult, Matrix expected) {
		
	var  transformIt	 = transforms.descendingIterator();
	
	
	if (lossFunction!=LossFunction.CROSSENTROPY|| transforms.getLast()!=Transform.SOFTMAX) {
		
		throw new UnsupportedOperationException("Last fuction must be cross entropy and transform must be softmax");
	}
	
	var ioIt = batchResult.getIo().descendingIterator();
	var weightIt =weights.descendingIterator();
	Matrix softmaxOutput =ioIt.next();
	Matrix error = softmaxOutput.apply((index, value)->value - expected.get(index));
	
	
	while (transformIt.hasNext()) {
	    Matrix input =ioIt.next();
		Transform transform = transformIt.next();
		
		switch(transform) {
		case DENSE:
			Matrix weight = weightIt.next();
			
			batchResult.addWeightError(error);
		 
			if(weightIt.hasNext() || storeInputError) {
			error=weight.transpose().multiply(error);
		  }
			break;
		case RELU:
			error=  error.apply((index,value)->input.get(index) > 0 ? value :0);
			break;
		case SOFTMAX:
			break;
		default:
			throw new UnsupportedOperationException("Not Impelemented");
		}
		
		//System.out.println(transform);
	}
	
	if(storeInputError) {
		
	batchResult.setInputError(error);
	
    }
	
	return null;	
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
			batchResult.addWeightInput(output);
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