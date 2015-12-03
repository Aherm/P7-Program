package evaluationLayer;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import naiveBayes.Multinomial;
import naiveBayes.NaiveBayes;
import naiveBayes.ProbabilityModel;
import modelLayer.EvaluationModel;
import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class EvalNB {

	public static Map<String, EvaluationModel> tenFoldCrossValidation(TweetStorage dataSet){
		TweetStorage[] folds = new TweetStorage[10];
		int from = 0;
		int sizeOfFold = (dataSet.size() / 10) - 1;
		int to = sizeOfFold; 	
		int remainder = dataSet.size() % 10;
		NaiveBayes NB = new Multinomial();
		ArrayList<String> classLabels = new ArrayList<String>(Arrays.asList("1", "0"));
		Map<String, EvaluationModel> fullEvaluation = new HashMap<String, EvaluationModel>();
		System.out.println("Starting To Create Data Set...");
		for(int i = 0; i < 10; i++){
			System.out.println("Creating Data Set For Fold " + i + "...");
			TweetStorage fold = new TweetStorage();
			if(i == 9) {
				fold = dataSet.getFromInterval(from, to + remainder);
			} else {
				fold = dataSet.getFromInterval(from, to);
			}
			from = to + 1;
			to = from + sizeOfFold;
			folds[i] = fold;
		}
		System.out.println("Dataset Has Been Created...");
		
		System.out.println("Starting To Apply Model...");
		for(int i = 0; i < 10; i++) {
			int TP = 0;
			int TN = 0;
			int FP = 0;
			int FN = 0;
			System.out.println("Applying Model for Iteration " + i + "...");
			TweetStorage testSet = folds[i];
			TweetStorage trainingSet = new TweetStorage();
			System.out.println("Starting To Create Training Set For Iteration " + i + "...");
			for(int n = 0; n < 10; n++) {
				System.out.println("Training Set In Progress Of Being Created...");
				if(n != i) {
					trainingSet.addAll(folds[n]);
				}
			}
			System.out.println("Training Set For Iteration " + i + " Created...");
			System.out.println("Starting To Train Model For Iteration " + i + "...");
			ProbabilityModel probModel = NB.train(classLabels, trainingSet);
			System.out.println("Model Has Been Trained...");
			System.out.println("Starting To Test Model For Iteration " + i + "...");
			for(Tweet tweet : testSet) {
				System.out.println("Testing For Tweet " + tweet.getTweetText());
				tweet.setAssignedClassLabel(NB.apply(classLabels, probModel, tweet));
				if(tweet.getAssignedClassLabel().equals("1") && tweet.getExpectedClassLabel().equals("1")) {
					TP++;
				} else if (tweet.getAssignedClassLabel().equals("1") && tweet.getExpectedClassLabel().equals("0")) {
					FP++;
				} else if (tweet.getAssignedClassLabel().equals("0") && tweet.getExpectedClassLabel().equals("0")) {
					TN++;
				} else if (tweet.getAssignedClassLabel().equals("0") && tweet.getExpectedClassLabel().equals("1")) {
					FN++;
				}
			}
			System.out.println("Model Has Been Tested...");
			double prec = getPrecision(TP, FP);
			double rec = getRecall(TP, FN);
			double tpRate = getTPRate(TP, FN);
			double fpRate = getFPRate(FP, TN);
			EvaluationModel evalModel = new EvaluationModel(i, prec, rec, tpRate, fpRate);
			fullEvaluation.put("Fold" + i, evalModel);
		}
		System.out.println("Model Has Been Applied...");
		
		return fullEvaluation;
	}
	
	public static Map<String, EvaluationModel> seventyThirtySplitValidation(TweetStorage dataSet) {
		int sizeOfTrainingSet = (dataSet.size() * 7/10);
		int testSetFrom = sizeOfTrainingSet;
		TweetStorage trainingSet = new TweetStorage();
		TweetStorage testSet = new TweetStorage();
		NaiveBayes NB = new Multinomial();
		ArrayList<String> classLabels = new ArrayList<String>(Arrays.asList("1", "0"));
		Map<String, EvaluationModel> fullEvaluation = new HashMap<String, EvaluationModel>();
		for(int i = 0; i < sizeOfTrainingSet; i++) {
			trainingSet.add(dataSet.get(i));		
		}
		for(int i = testSetFrom; i < dataSet.size(); i++) {
			testSet.add(dataSet.get(i));
		}
		int TP = 0;
		int FP = 0;
		int TN = 0;
		int FN = 0;
		ProbabilityModel probModel = NB.train(classLabels, trainingSet);
		for(Tweet tweet : testSet) {
			tweet.setAssignedClassLabel(NB.apply(classLabels, probModel, tweet));
			if(tweet.getAssignedClassLabel().equals("1") && tweet.getExpectedClassLabel().equals("1")) {
				TP++;
			} else if (tweet.getAssignedClassLabel().equals("1") && tweet.getExpectedClassLabel().equals("0")) {
				FP++;
			} else if (tweet.getAssignedClassLabel().equals("0") && tweet.getExpectedClassLabel().equals("0")) {
				TN++;
			} else if (tweet.getAssignedClassLabel().equals("0") && tweet.getExpectedClassLabel().equals("1")) {
				FN++;
			}
		}
		double prec = getPrecision(TP, FP);
		double rec = getRecall(TP, FN);
		double tpRate = getTPRate(TP, FN);
		double fpRate = getFPRate(FP, TN);
		
		EvaluationModel evalNB = new EvaluationModel(1, prec, rec, tpRate, fpRate);
		fullEvaluation.put("Fold1", evalNB);
		return fullEvaluation;
	}
	
	public void rocCurve(TweetStorage tweets) {
		List<Point2D.Double> points = new ArrayList<Point2D.Double>();
		
		BigDecimal minProb = minProb(tweets);
		BigDecimal maxProb = maxProb(tweets);
		BigDecimal step = maxProb.subtract(minProb).divide(new BigDecimal(100));
		
		for (BigDecimal threshold = maxProb; threshold.compareTo(minProb) >= 0; threshold = threshold.subtract(step)) {
			int TP = 0, FP = 0, TN = 0, FN = 0;
			for (Tweet t : tweets) {
				if (t.getProbabilityTrue().compareTo(threshold) >= 0 && t.getExpectedClassLabel() == "1") {
					TP++;
				}
				if (t.getProbabilityTrue().compareTo(threshold) >= 0 && t.getExpectedClassLabel() == "0") {
					FP++;
				}
				if (t.getProbabilityTrue().compareTo(threshold) < 0 && t.getExpectedClassLabel() == "0") {
					TN++;
				}
				if (t.getProbabilityTrue().compareTo(threshold) < 0 && t.getExpectedClassLabel() == "1") {
					FN++;
				}
			}
			double tpRate = getTPRate(TP, FN);
			double fpRate = getFPRate(FP, TN);
			
			points.add(new Point2D.Double(fpRate, tpRate));
		}
	}
	
	private static double getPrecision(double TP, double FP) {
		return TP / (TP + FP);
	}
	
	private static double getRecall(double TP, double FN) {
		return TP / (TP + FN);
	}
	
	private static double getTPRate(double TP, double FN) {
		return getRecall(TP, FN);
	}
	
	private static double getFPRate(double FP, double TN) {
		return FP / (FP + TN);
	}
	
	private BigDecimal maxProb(TweetStorage tweets) {
		BigDecimal max = new BigDecimal(-1);
		for (Tweet t : tweets) {
			if (t.getProbabilityTrue().compareTo(max) > 0) {
				max = t.getProbabilityTrue();
			}
		}
		return max;		
	}
	
	private BigDecimal minProb(TweetStorage tweets) {
		BigDecimal min = new BigDecimal(1000000000);
		for (Tweet t : tweets) {
			if (t.getProbabilityTrue().compareTo(min) < 0) {
				min = t.getProbabilityTrue();
			}
		}
		return min;
	}
}
