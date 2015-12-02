package businessLogicLayer;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import algorithmLayer.Multinomial;
import algorithmLayer.NaiveBayes;
import algorithmLayer.ProbabilityModel;
import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class Evaluation {

	public TweetStorage[] tenFoldCrossValidation(TweetStorage dataSet){
		TweetStorage[] folds = new TweetStorage[10];
		int from = 0;
		int sizeOfFold = (dataSet.size() / 10) - 1;
		int to = sizeOfFold; 	
		int remainder = dataSet.size() % 10;
		NaiveBayes NB = new Multinomial();
		ArrayList<String> classLabels = new ArrayList<String>(Arrays.asList("1", "0"));
		
		for(int i = 0; i < 10; i++){
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
		
		int TP = 0;
		int TN = 0;
		int FP = 0;
		int FN = 0;
		
		for(int i = 0; i < 10; i++) {
			TweetStorage testSet = folds[i];
			TweetStorage trainingSet = new TweetStorage();
			for(int n = 0; n < 10; n++) {
				if(n != i) {
					trainingSet.addAll(folds[n]);
				}
			}
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
			double tnRate = getTNRate(FP, TN);
		}
		
		return folds;
	}
	
	public void rocCurve(TweetStorage tweets) {
		List<Point> points = new ArrayList<Point>();
		for (int threshold = 100; threshold >= 0; threshold--) {
			int TP = 0, FP = 0, TN = 0, FN = 0;
			for (Tweet t : tweets) {
				
			}
			// TP rate
			// FP rate
		}
	}
	
	public double getPrecision(int TP, int FP) {
		return TP / (TP + FP);
	}
	
	public double getRecall(int TP, int FN) {
		return TP / (TP + FN);
	}
	
	public double getTPRate(int TP, int FN) {
		return getRecall(TP, FN);
	}
	
	public double getTNRate(int FP, int TN) {
		return TN / (FP + TN);
	}
}
