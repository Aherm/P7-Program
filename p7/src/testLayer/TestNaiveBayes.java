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
        multinomialNBBD.trainBigDecimal(classLabels, trainingSet);

        String filePath = "./classifiers/naiveBayes.model";
        //MultinomialBigDecimal.saveClassifier(probabilityModelBigDecimal, filePath);
        //MultinomialBigDecimal.loadClassifier(filePath);

        //test
        TweetStorage resultTweets = new TweetStorage();
        for (Tweet tweet : testSet) {
            try {
                String predictedClass = multinomialNBBD.applyBigDecimal(tweet);
                System.out.println("predicted class: " + predictedClass);
                Tweet classifiedTweet = multinomialNBBD.applyGetProbability(tweet);
                resultTweets.add(classifiedTweet);

            } catch (Exception ex){
                System.out.println(ex);
            }

            //printResults(resultClass, tweet);
        }
        return resultTweets;
    }

    private static void testMultinomialProbability(ArrayList<String> classLabels, TweetStorage trainingSet, TweetStorage testSet){
        Multinomial multinomialNB = new Multinomial();
        multinomialNB.train(classLabels, trainingSet);

        //test
        List<Map<String, Double>> results = new ArrayList<Map<String, Double>>();
        int counter = 0;
        for (Tweet tweet : testSet) {
            try {
                String predictedClass = multinomialNB.applyProbability(tweet);
                Map<String, Double> score = multinomialNB.applyProbabilityGetScore(tweet);
                results.add(score);
            } catch (Exception ex){
                System.out.println();
            }
            //printResults(resultClass, tweet);
            counter++;
        }
    }

    public static void testSickClassifier(){
        /**
         * Initialization of data
         */
        ArrayList<String> classLabels = new ArrayList<String>(Arrays.asList("0", "1"));
        TweetStorage trainingSet = Utils.getDataFromFile("trainingData\\sickSet.csv");
        Preprocessor.processTweets(trainingSet);
        TweetStorage correctSet = new TweetStorage();

        System.out.println(correctSet.size());
        /**
         * Training the classifier
         */
        Multinomial multinomialNB = new Multinomial();
        multinomialNB.train(classLabels, trainingSet);

        /**
         * Saving the classifier
         */
        String filePath = "./classifiers/sickNaiveBayes.model";
        try{
            multinomialNB.saveClassifier(filePath);
        } catch (Exception ex){
            System.out.println(ex);
        }
        //MultinomialBigDecimal.loadClassifier(filePath);
    }

    private static void testVisitClassifier(){
        /**
         * Initialization of data
         */
        ArrayList<String> classLabels = new ArrayList<String>(Arrays.asList("0", "1"));
        TweetStorage trainingSet = Utils.getDataFromFileWithGeo("trainingData\\RealSecondSet.csv");
        Preprocessor.processTweets(trainingSet);

        /**
         * Training the classifier
         */
        Multinomial multinomialNB = new Multinomial();
        multinomialNB.train(classLabels, trainingSet);

        /**
         * Saving the classifier
         */
        String filePath = "./classifiers/visitNaiveBayes.model";
        try{
            multinomialNB.saveClassifier(filePath);
        } catch (Exception ex){
            System.out.println(ex);
        }
    }

    public static void main(String[] args){
        //testSickClassifier();
        testVisitClassifier();
    }




    private static void printResults(String resultClass, Tweet tweet){
        System.out.println("-----------------------------------------------------");
        System.out.println("Tweet ID: " + tweet.getTweetID());
        System.out.println("Text: " + tweet.getTweetText());
        System.out.println("Class assigned: " + resultClass);
        System.out.println("-----------------------------------------------------");
    }
}
