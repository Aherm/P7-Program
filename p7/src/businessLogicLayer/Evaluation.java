package businessLogicLayer;

import java.util.ArrayList;
import java.util.Arrays;

import algorithmLayer.Multinomial;
import algorithmLayer.NaiveBayes;
import algorithmLayer.ProbabilityModel;
import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class Evaluation {

	public static TweetStorage[] tenFoldCrossValidation(TweetStorage dataSet){
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
				NB.apply(classLabels, probModel, tweet);
			}
			// something needs to happen here 
		}
		
		return folds;
	}
}
