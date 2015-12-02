package businessLogicLayer;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class Evaluation {

	public void tenFoldCrossValidation(TweetStorage trainingSet){
		TweetStorage[] folds = new TweetStorage[10];
		
		for(int i = 0; i < 10; i++){
			
		}
	}
	
	public void rocCurve(TweetStorage tweets) {
		List<Point> points = new ArrayList<Point>();
		for (int threshold = 100; threshold >= 0; threshold--) {
			int TP = 0, FP = 0, TN = 0, FN = 0;
			for (Tweet t : tweets) {
				// if classified = true = expected TP++
				// if classified = true != expected FP++
				// if classified = false = expected TN++
				// if classified = false != expected FN++
			}
			// TP rate
			// FP rate
		}
	}
	
	public double precision(int TP, int FP) {
		return TP / (TP + FP);
	}
	
	public double recall(int TP, int FN) {
		return TP / (TP + FN);
	}
	
	public double tpRate(int TP, int FN) {
		return recall(TP, FN);
	}
	
	public double tnRate(int FP, int TN) {
		return TN / (FP + TN);
	}
}
