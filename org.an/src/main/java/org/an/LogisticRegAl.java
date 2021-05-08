package org.an;

import java.util.Arrays;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class LogisticRegAl 
{
	public static Instances getInstances (String filename)
	{
		
		DataSource source;
		Instances dataset = null;
		try {
			source = new DataSource(filename);
			dataset = source.getDataSet();
			dataset.setClassIndex(dataset.numAttributes()-1);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return dataset;
	}
	
	public static void main(String[] args) throws Exception
{

		Instances train_data = getInstances("C:\\Users\\home\\eclipse-workspace\\org.an\\src\\main\\java\\org\\an\\bank_trainset.arff");
		Instances test_data = getInstances("C:\\Users\\home\\eclipse-workspace\\org.an\\src\\main\\java\\org\\an\\bank_testset.arff");
		
		System.out.println(train_data.size());
		
		/** Classifier here is Linear Regression */
		Classifier classifier = new weka.classifiers.functions.Logistic();
		/** */
		classifier.buildClassifier(train_data);
		
		
		/**
		 * train the alogorithm with the training data and evaluate the
		 * algorithm with testing data
		 */
		Evaluation eval = new Evaluation(train_data);
		eval.evaluateModel(classifier, test_data);
		
		/** Print the algorithm summary */
		System.out.println("** Logistic Regression Evaluation with Datasets **");
		
		System.out.println(eval.toSummaryString());
		
		System.out.print(" the expression for the input data as per alogorithm is ");
		
		System.out.println(classifier);
		
		double confusion[][] = eval.confusionMatrix();
		System.out.println("Confusion matrix:");
		for (double[] row : confusion)
			System.out.println(	 Arrays.toString(row));
		System.out.println("-------------------");

		System.out.println("Area under the curve");
			
			
		System.out.println( eval.areaUnderROC(0));
		System.out.println("-------------------");
		
		System.out.println(eval.getAllEvaluationMetricNames());
		
		System.out.print("Recall :");
		System.out.println(Math.round(eval.recall(1)*100.0)/100.0);
		
		System.out.print("Precision:");
		System.out.println(Math.round(eval.precision(1)*100.0)/100.0);
		System.out.print("F1 score:");
		System.out.println(Math.round(eval.fMeasure(1)*100.0)/100.0);
		
		System.out.print("Accuracy:");
		double acc = eval.correct()/(eval.correct()+ eval.incorrect());
		System.out.println(Math.round(acc*100.0)/100.0);
		
		
		System.out.println("-------------------");
		Instance predicationDataSet = test_data.get(2);
		double value = classifier.classifyInstance(predicationDataSet);
		
		/** Prediction Output */
		System.out.println("Predicted label:");
		System.out.print(value);
		
		
	}

}
