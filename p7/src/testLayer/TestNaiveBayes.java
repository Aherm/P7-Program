package testLayer;

import businessLogicLayer.Preprocessor;
import modelLayer.Tweet;
import modelLayer.TweetStorage;
import naiveBayes.*;

import java.io.*;
import java.util.*;

public class TestNaiveBayes {

    private static TweetStorage testMultinomialBigDecimal(ArrayList<String> classLabels, TweetStorage trainingSet, TweetStorage testSet){
        MultinomialBigDecimal multinomialNBBD = new MultinomialBigDecimal();
        ProbabilityModelBigDecimal probabilityModelBigDecimal = multinomialNBBD.trainBigDecimal(classLabels, trainingSet);

        String filePath = "./classifiers/naiveBayes.model";
        //MultinomialBigDecimal.saveClassifier(probabilityModelBigDecimal, filePath);
        ProbabilityModelBigDecimal classifier = MultinomialBigDecimal.loadClassifier(filePath);

        //test
        TweetStorage resultTweets = new TweetStorage();
        for (Tweet tweet : testSet) {
            String predictedClass = multinomialNBBD.applyBigDecimal(classLabels, classifier, tweet);
            System.out.println("predicted class: " + predictedClass);
            Tweet classifiedTweet = multinomialNBBD.applyGetProbability(classLabels, classifier, tweet);
            resultTweets.add(classifiedTweet);

            //printResults(resultClass, tweet);
        }
        return resultTweets;
    }

    private static void testMultinomialProbability(ArrayList<String> classLabels, TweetStorage trainingSet, TweetStorage testSet){
        Multinomial multinomialNB = new Multinomial();
        ProbabilityModel probabilityModel = multinomialNB.train(classLabels, trainingSet);

        //test
        List<Map<String, Double>> results = new ArrayList<Map<String, Double>>();
        int counter = 0;
        for (Tweet tweet : testSet) {
            String predictedClass = multinomialNB.applyProbability(classLabels, probabilityModel, tweet);
            Map<String, Double> score = multinomialNB.applyProbabilityGetScore(classLabels, probabilityModel, tweet);
            results.add(score);
            System.out.println("index: " + counter + ", predicted class: " + predictedClass);
            //printResults(resultClass, tweet);
            counter++;
        }
    }

    public static void main(String[] args){
        /**
         * Initialization of data
         */
        //btc, consider using a HashSet to avoid duplicate classes
        ArrayList<String> bookClassLabels = Data.initializeClassLabels();
        ArrayList<String> classLabels = new ArrayList<String>(Arrays.asList("not visited", "visited"));
        String trainingFilePath = "Resturants_mentions2.csv";

        TweetStorage bookTrainingSet = Data.initializeTrainingSet(bookClassLabels);

        for (Tweet t : bookTrainingSet)
            Preprocessor.processTweet(t);

        /*
        TweetStorage trainingSet = new TweetStorage();
        for (Document d : Data.initializeDataFromFile(trainingFilePath)){
            Tweet tweet = new Tweet();
            tweet.setTweetID(d.getID());
            tweet.setTweetText(d.getText());
            tweet.setExpectedClassLabel(d.getClassLabel());
            trainingSet.add(tweet);
        }

        // Preprocess the training data
        for (Tweet t : trainingSet)
            Preprocessor.processTweet(t);
        */


        TweetStorage bookTestSet = Data.initializeTestSet();
        for (Tweet t : bookTestSet)
            Preprocessor.processTweet(t);


        TweetStorage testSet = new TweetStorage();
        testSet.add(new Tweet(1, 1, 1, 1, "I'm at HaterKing in New York, NY https://t.co/frLjcnL0zk", new Date()));
        testSet.add(new Tweet(1, 1, 1, 1, "I'm at NoMad in New	 York, NY @LINK", new Date()));
        testSet.add(new Tweet(1, 1, 1, 1, "I'm at Carstensen's Café in New York, NY", new Date()));
        testSet.add(new Tweet(1, 1, 1, 1, "Happy hour lel mate at 2A", new Date()));
        testSet.add(new Tweet(1, 1, 1, 1, "I'm at", new Date()));
        testSet.add(new Tweet(1, 1, 1, 1, "HOOOOOOOOOOOOOOOODOOOOOOOOOOOOOOOOOR", new Date()));
        testSet.add(new Tweet(1, 1, 1, 1, "fuck u bjørk", new Date()));
        Preprocessor.processTweets(testSet);

        //testMultinomialProbability(classLabels, trainingSet, testSet);
        testMultinomialBigDecimal(bookClassLabels, bookTrainingSet, bookTestSet);
    }

    private static void printResults(String resultClass, Tweet tweet){
        System.out.println("-----------------------------------------------------");
        System.out.println("Tweet ID: " + tweet.getTweetID());
        System.out.println("Text: " + tweet.getTweetText());
        System.out.println("Class assigned: " + resultClass);
        System.out.println("-----------------------------------------------------");
    }
}
