package org.an;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import javax.swing.JFrame;

import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.traces.BoxTrace;
import tech.tablesaw.plotly.traces.HistogramTrace;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

public class ClassificationDemo {

	private Instances data;
	private J48 tree;

	public ClassificationDemo(String arff) throws Exception
	{
		DataSource source = new DataSource(arff);
		data = source.getDataSet();
		System.out.println(data.numInstances() + " instances loaded!");
		System.out.println(data.toString());
	}
	
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

	public void removeFirstAttribute() throws Exception {
		Remove remove = new Remove();
		String[] opts = new String[] { "-R", "1" };
		remove.setOptions(opts);
		remove.setInputFormat(data);
		data = Filter.useFilter(data, remove);
	}

	public void selectFeatures() throws Exception {
		InfoGainAttributeEval evaluator = new InfoGainAttributeEval();
		Ranker ranker = new Ranker();
		AttributeSelection attSelect = new AttributeSelection();
		attSelect.setEvaluator(evaluator);
		attSelect.setSearch(ranker);
		attSelect.SelectAttributes(data);
		int[] selectedAttributes = attSelect.selectedAttributes();
		System.out.println(Utils.arrayToString(selectedAttributes));
	}

	public void buildDecisionTree() throws Exception {
		tree = new J48();
		String[] options = new String[1];
		options[0] = "-U"; // un-pruned tree option
		tree.setOptions(options);
		tree.buildClassifier(data);
		System.out.println(tree.toString());
	}

	public void visualizeTree() throws Exception {
		TreeVisualizer tv = new TreeVisualizer(null, tree.graph(), new PlaceNode2());
		JFrame frame = new JFrame("Tree Visualizer");
		frame.setSize(800, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(tv);
		frame.setVisible(true);
		tv.fitToScreen();
	}

	public void classifyData() throws Exception {
		double[] vals = new double[data.numAttributes()];
		vals[0] = 1.0;	//
		vals[1] = 0.0;	//
		vals[2] = 0.0;	//
		vals[3] = 1.0;	//
		vals[4] = 0.0;	//
		vals[5] = 0.0;	//
		vals[6] = 0.0;	//
		vals[7] = 1.0;	//
		vals[8] = 1.0;	//
		vals[9] = 1.0;	//
		vals[10] = 1.0;	//
		vals[11] = 0.0;	//
		vals[12] = 4.0;	//
		vals[13] = 1.0;	//
		vals[14] = 1.0;	//
		vals[15] = 0.0;	//
	
		Instance myUnicorn = new DenseInstance(1.0, vals);
		myUnicorn.setDataset(data);
		double result = tree.classifyInstance(myUnicorn);
		System.out.println("And the class deposit is... " + data.classAttribute().value((int) result));
	}

	public void showErrorMetrics() throws Exception {
		Classifier c1 = new J48();
		Evaluation evalRoc = new Evaluation(data);
		evalRoc.crossValidateModel(c1, data, 10, new Random(1), new Object[] {});
		System.out.println(evalRoc.toSummaryString());
		System.out.println(evalRoc.toMatrixString());
	}

	public static void main(String[] args)
	{
		System.out.print("having the data to be analysing");
		
		try {
			Table bank_data = Table.read().csv("C:\\Users\\home\\eclipse-workspace\\org.an\\src\\main\\java\\org\\an\\bank.csv");
			
			System.out.println(bank_data.shape());
			System.out.println(bank_data.first(6));
			System.out.println(bank_data.last(3));
			
			System.out.println(bank_data.structure());
			System.out.println(bank_data.missingValueCounts());
			
			System.out.println(bank_data.summary());
			
			System.out.println(bank_data.hashCode());
			
			
///		Histogram
			Layout layout1 = Layout.builder().title("Distribution of  job").build();
			HistogramTrace trace1 = HistogramTrace.builder(bank_data.nCol("job")).build();
		    Plot.show(new Figure(layout1, trace1));
		    
		 // try all these numerical & nominal features for the distribution get visualizers 
		//	age,balance,day,duration,campaign,pdays,previous -- job,marital,education,default,housing,loan,contact,month,poutcome,deposit    
		    
		    Layout layout3 = Layout.builder().title("age by deposit ").build();
		    BoxTrace trace3 =BoxTrace.builder(bank_data.categoricalColumn("deposit"), bank_data.nCol("age")).build();
		    Plot.show(new Figure(layout3, trace3));
			Instances train_data = getInstances("C:\\Users\\home\\eclipse-workspace\\org.an\\src\\main\\java\\org\\an\\bank_trainset.arff");
			Instances test_data = getInstances("C:\\Users\\home\\eclipse-workspace\\org.an\\src\\main\\java\\org\\an\\bank_testset.arff");
			
			ClassificationDemo weka = new ClassificationDemo("C:\\Users\\home\\eclipse-workspace\\org.an\\src\\main\\java\\org\\an\\bankset.arff");
			weka.removeFirstAttribute();
			weka.selectFeatures();
			weka.buildDecisionTree();
			weka.visualizeTree();
			weka.classifyData();
			weka.showErrorMetrics();
			
			
			
			Classifier classifier = new weka.classifiers.functions.Logistic();
			
			classifier.buildClassifier(train_data);
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
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

