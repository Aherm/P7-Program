package testLayer;

import businessLogicLayer.Preprocessor;
import modelLayer.Tweet;
import modelLayer.TweetStorage;
import naiveBayes.*;
import utility.Utils;

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
        ArrayList<String> classLabels = new ArrayList<String>(Arrays.asList("not visited", "visited"));
        //String trainingFilePath = "Resturants_mentions2.csv";
        TweetStorage trainingSet = Utils.getDataFromFile("t");

        for (Tweet t : trainingSet)
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

        TweetStorage testSet = new TweetStorage();
        for (Tweet t : testSet)
            Preprocessor.processTweet(t);




        Preprocessor.processTweets(testSet);



    }

    private static void classifierToFile(){

    }


    private static void printResults(String resultClass, Tweet tweet){
        System.out.println("-----------------------------------------------------");
        System.out.println("Tweet ID: " + tweet.getTweetID());
        System.out.println("Text: " + tweet.getTweetText());
        System.out.println("Class assigned: " + resultClass);
        System.out.println("-----------------------------------------------------");
    }
}
