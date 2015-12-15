package evaluationLayer;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import businessLogicLayer.Filter;
import businessLogicLayer.Preprocessor;
import naiveBayes.Multinomial;
import naiveBayes.MultinomialBigDecimal;
import naiveBayes.NaiveBayes;
import naiveBayes.ProbabilityModel;
import naiveBayes.ProbabilityModelBigDecimal;
import modelLayer.EvaluationModel;
import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class EvalNB {

	public static Map<String, EvaluationModel> stratifiedTenFoldCrossValidation(TweetStorage dataSet) {
		Collections.shuffle(dataSet);
		Preprocessor.processTweets(dataSet);
		TweetStorage[] folds = new TweetStorage[10];
		TweetStorage positives = new TweetStorage();
		TweetStorage negatives = new TweetStorage();
		NaiveBayes NB = new Multinomial();
		ArrayList<String> classLabels = new ArrayList<String>(Arrays.asList("1", "0"));
		Map<String, EvaluationModel> fullEvaluation = new HashMap<String, EvaluationModel>();
		
		System.out.println("Starting to create positive and negative sets...");
		for(Tweet tweet : dataSet) {
			if(tweet.getExpectedClassLabel().equals("1")) {
				positives.add(tweet);
			}
			if(tweet.getExpectedClassLabel().equals("0")) {
				negatives.add(tweet);
			}
		}
		System.out.println("Positive and negative sets has been created...");
		int fromPositives = 0;
		int fromNegatives = 0;
		int sizeOfPositives = (positives.size() / 10) - 1;
		int sizeOfNegatives = (negatives.size() / 10) - 1;
		int toPositives = sizeOfPositives;
		int toNegatives = sizeOfNegatives;
		int remainderPositives = positives.size() % 10;
		int remainderNegatives = negatives.size() % 10;
		System.out.println("Starting to create folds...");
		for(int i = 0; i < 10; i++) {
			TweetStorage fold = new TweetStorage();
			if(i == 9) {
				fold = dataSet.getFromInterval(fromPositives, toPositives + remainderPositives);
				fold.addAll(dataSet.getFromInterval(fromNegatives, toNegatives + remainderNegatives));
			} else {
				fold = dataSet.getFromInterval(fromPositives, toPositives);
				fold.addAll(dataSet.getFromInterval(fromNegatives, toNegatives));
			}
			fromPositives = toPositives + 1;
			toPositives = fromPositives + sizeOfPositives;
			fromNegatives = toNegatives + 1;
			toNegatives = fromNegatives + sizeOfNegatives;
			folds[i] = fold;	
		}
		System.out.println("Folds has been created...");
		
		System.out.println("Starting to create training set...");
		for(int i = 0; i < 10; i++) {
			TweetStorage trainingSet = new TweetStorage();
			TweetStorage testSet = folds[i];
			int TP = 0;
			int TN = 0;
			int FP = 0;
			int FN = 0;
			for(int n = 0; n < 10; n++) {
				if (n != i) {
					trainingSet.addAll(folds[n]);
				}
			}
			System.out.println("Training sets has been created...");
			System.out.println("Starting to train model for iteration: " + (i + 1));
			NB.train(classLabels, trainingSet);
			System.out.println("Model has been trained for iteration: " + (i + 1));
			System.out.println("Starting to test model...");
			for(Tweet tweet : testSet) {
				try{
					System.out.println("Testing for tweet: " + tweet.getTweetText());
					tweet.setAssignedClassLabel(NB.apply(tweet));
					if(tweet.getAssignedClassLabel().equals("1") && tweet.getExpectedClassLabel().equals("1")) {
						TP++;
					} else if (tweet.getAssignedClassLabel().equals("1") && tweet.getExpectedClassLabel().equals("0")) {
						FP++;
					} else if (tweet.getAssignedClassLabel().equals("0") && tweet.getExpectedClassLabel().equals("0")) {
						TN++;
					} else if (tweet.getAssignedClassLabel().equals("0") && tweet.getExpectedClassLabel().equals("1")) {
						FN++;
					}
				} catch (Exception ex){
					System.out.println(ex);
				}
			}
			System.out.println("Model has been tested...");
			EvaluationModel evalModel = new EvaluationModel("10-fold stratified cross-validation", (i + 1), TP, TN, FP, FN);
			fullEvaluation.put("Fold" + (i + 1) , evalModel);
		}
		return fullEvaluation;
	}
	
	public static Map<String, EvaluationModel> tenFoldCrossValidation(TweetStorage dataSet){
		Collections.shuffle(dataSet);
		Preprocessor.processTweets(dataSet);
		TweetStorage[] folds = new TweetStorage[10];
		int from = 0;
		int sizeOfFold = (dataSet.size() / 10) - 1;
		int to = sizeOfFold; 	
		int remainder = dataSet.size() % 10;
		NaiveBayes NB = new Multinomial();
		//MultinomialBigDecimal NB = new MultinomialBigDecimal();
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
			NB.train(classLabels, trainingSet);
			//ProbabilityModelBigDecimal probModel = NB.trainBigDecimal(classLabels, trainingSet);
			System.out.println("Model Has Been Trained...");
			System.out.println("Starting To Test Model For Iteration " + i + "...");
			for(Tweet tweet : testSet) {
				try {
					System.out.println("Testing For Tweet " + tweet.getTweetText());
					tweet.setAssignedClassLabel(NB.apply(tweet));
					//tweet.setAssignedClassLabel(NB.applyBigDecimal(classLabels, probModel, tweet));
					if(tweet.getAssignedClassLabel().equals("1") && tweet.getExpectedClassLabel().equals("1")) {
						TP++;
					} else if (tweet.getAssignedClassLabel().equals("1") && tweet.getExpectedClassLabel().equals("0")) {
						FP++;
					} else if (tweet.getAssignedClassLabel().equals("0") && tweet.getExpectedClassLabel().equals("0")) {
						TN++;
					} else if (tweet.getAssignedClassLabel().equals("0") && tweet.getExpectedClassLabel().equals("1")) {
						FN++;
					}
				} catch (Exception ex){
					System.out.println(ex);
				}
			}
			System.out.println("Model Has Been Tested...");
			EvaluationModel evalModel = new EvaluationModel("10-fold Crossvalidation", i, TP, TN, FP, FN);
			fullEvaluation.put("Fold" + i, evalModel);
		}
		
		System.out.println("Model Has Been Applied...");
		
		return fullEvaluation;
	}
	
	public static Map<String, EvaluationModel> seventyThirtySplitValidation(TweetStorage dataSet) {
		Collections.shuffle(dataSet);
		Preprocessor.processTweets(dataSet);
		int sizeOfTrainingSet = (dataSet.size() * 7/10);
		int testSetFrom = sizeOfTrainingSet;
		TweetStorage trainingSet = new TweetStorage();
		TweetStorage testSet = new TweetStorage();
		//NaiveBayes NB = new Multinomial();
		MultinomialBigDecimal NB = new MultinomialBigDecimal();
		ArrayList<String> classLabels = new ArrayList<String>(Arrays.asList("1", "0"));
		Map<String, EvaluationModel> fullEvaluation = new HashMap<String, EvaluationModel>();
		System.out.println("Starting To Create Training Data...");
		for(int i = 0; i < sizeOfTrainingSet; i++) {
			System.out.println("Training Data Creation Iteration " + i + "...");
			trainingSet.add(dataSet.get(i));		
		}
		System.out.println("Training Data Has Been Created...");
		System.out.println("Starting To Create Test Data...");
		for(int i = testSetFrom; i < dataSet.size(); i++) {
			System.out.println("Test Data Creation Iteration " + i + "...");
			testSet.add(dataSet.get(i));
		}
		System.out.println("Test Data Has Been Created...");
		int TP = 0;
		int FP = 0;
		int TN = 0;
		int FN = 0;
		System.out.println("Starting To Train Model...");
		//ProbabilityModel probModel = NB.train(classLabels, trainingSet);
		NB.trainBigDecimal(classLabels, trainingSet);
		System.out.println("The Model Has Been Trained...");
		System.out.println("Starting To Test Model...");
		for(Tweet tweet : testSet) {
			try {
				System.out.println("Testing For Tweet: " + tweet.getTweetText());
				//tweet.setAssignedClassLabel(NB.apply(classLabels, probModel, tweet));
				tweet.setAssignedClassLabel(NB.applyBigDecimal(tweet));
				if(tweet.getAssignedClassLabel().equals("1") && tweet.getExpectedClassLabel().equals("1")) {
					TP++;
				} else if (tweet.getAssignedClassLabel().equals("1") && tweet.getExpectedClassLabel().equals("0")) {
					FP++;
				} else if (tweet.getAssignedClassLabel().equals("0") && tweet.getExpectedClassLabel().equals("0")) {
					TN++;
				} else if (tweet.getAssignedClassLabel().equals("0") && tweet.getExpectedClassLabel().equals("1")) {
					FN++;
				}
			} catch (Exception ex){
				System.out.println(ex);
			}
		}
		System.out.println("The Model Has Been Tested...");
		
		EvaluationModel evalNB = new EvaluationModel("Seventy-Thirty Split", 1, TP, TN, FP, FN);
		fullEvaluation.put("Fold1", evalNB);
		return fullEvaluation;
	}

	public static Map<String, EvaluationModel> testSet(TweetStorage testSet, Multinomial NB) {
		Collections.shuffle(testSet);
		Preprocessor.processTweets(testSet);
		ArrayList<String> classLabels = new ArrayList<String>(Arrays.asList("1", "0"));
		Map<String, EvaluationModel> fullEvaluation = new HashMap<String, EvaluationModel>();
		System.out.println("Starting To Create Training Data...");
		System.out.println("Test Data Has Been Created...");
		int TP = 0;
		int FP = 0;
		int TN = 0;
		int FN = 0;
		System.out.println("Starting To Train Model...");
		System.out.println("The Model Has Been Trained...");
		System.out.println("Starting To Test Model...");
		for(Tweet tweet : testSet) {
			try {
				//System.out.println("Testing For Tweet: " + tweet.getTweetText());
				//tweet.setAssignedClassLabel(NB.apply(classLabels, probModel, tweet));
				tweet.setAssignedClassLabel(NB.apply(tweet));
				if(tweet.getAssignedClassLabel().equals("1") && tweet.getExpectedClassLabel().equals("1")) {
					TP++;

				} else if (tweet.getAssignedClassLabel().equals("1") && tweet.getExpectedClassLabel().equals("0")) {
					FP++;
				} else if (tweet.getAssignedClassLabel().equals("0") && tweet.getExpectedClassLabel().equals("0")) {
					TN++;
				} else if (tweet.getAssignedClassLabel().equals("0") && tweet.getExpectedClassLabel().equals("1")) {
					FN++;
				}
			} catch (Exception ex){
				System.out.println(ex);
			}
		}
		System.out.println("The Model Has Been Tested...");

		EvaluationModel evalNB = new EvaluationModel("Seventy-Thirty Split", 1, TP, TN, FP, FN);
		fullEvaluation.put("Fold1", evalNB);
		evalNB.printEvaluation();
		return fullEvaluation;
	}
	
	/*public void rocCurve(TweetStorage tweets) {
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
	}*/
	
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
