package algorithmLayer;

import businessLogicLayer.Preprocessor;
import modelLayer.Tweet;
import modelLayer.TweetStorage;

import java.math.BigDecimal;
import java.util.*;

public class TestNaiveBayes {

    private static void testMultinomialBigDecimal(ArrayList<String> classLabels, TweetStorage trainingSet){
        MultinomialBigDecimal multinomialNBBD = new MultinomialBigDecimal();

        //ProbabilityModel probabilityModel = multinomialNB.train(bookClassLabels, bookTrainingSet);
        ProbabilityModelBigDecimal probabilityModelBigDecimal = multinomialNBBD.trainBigDecimal(classLabels, trainingSet);
        //Possibly find way to store .jar stopped at this point and executes on with provided argument

        /**
         * Test phase
         */
        List<Map<String, Map<String, BigDecimal>>> resultsBigDecimal = new ArrayList<Map<String, Map<String, BigDecimal>>>();

        TweetStorage bookTestSet = Data.initializeTestSet();
        for (Tweet tweet : bookTestSet) {
            String predictedClass = multinomialNBBD.applyBigDecimal(classLabels, probabilityModelBigDecimal, tweet);
            System.out.println("predicted class: " + predictedClass);
            Map<String, BigDecimal> probability = multinomialNBBD.applyGetProbability(classLabels, probabilityModelBigDecimal, tweet);
            Map<String, Map<String, BigDecimal>> store = new HashMap<String, Map<String, BigDecimal>>();
            store.put(predictedClass, probability);
            resultsBigDecimal.add(store);
            System.out.println("done");

            //printResults(resultClass, tweet);
        }
    }

    private static void testMultinomialProbability(ArrayList<String> classLabels, TweetStorage trainingSet, TweetStorage testSet){
        Multinomial multinomialNB = new Multinomial();

        /**
         * Training phase
         */
        ProbabilityModel probabilityModel = multinomialNB.train(classLabels, trainingSet);
        //Possibly find way to store .jar stopped at this point and executes on with provided argument

        /**
         * Test phase
         */
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
        String filePath = "resturant_mentions.csv";

        //TweetStorage bookTrainingSet = Data.initializeTrainingSet(bookClassLabels);

        TweetStorage trainingSet = new TweetStorage();
        for (Document d : Data.initializeDataFromFile(filePath)){
            Tweet tweet = new Tweet();
            tweet.setTweetID(d.getID());
            tweet.setTweetText(d.getText());
            tweet.setClassLabel(d.getClassLabel());
            trainingSet.add(tweet);
        }

        // Preprocess the training data
        for (Tweet t : trainingSet)
            Preprocessor.processTweet(t);


        //Consider making a separate Document class as a wrapper of a tweet that provides access to the most
        //relevant fields of the tweet in relation to naiveBayes

        //Need to fetch actual training data from database and use below as test data
        //Possible issue: Make sure that all tweets in the tweetstorage are associated to a class

        TweetStorage bookTestSet = Data.initializeTestSet();
        for (Tweet t : bookTestSet)
            Preprocessor.processTweet(t);

        testMultinomialProbability(classLabels, trainingSet, trainingSet);
        //testMultinomialBigDecimal(bookClassLabels, bookTrainingSet);
    }

    private static void printResults(String resultClass, Tweet tweet){
        System.out.println("-----------------------------------------------------");
        System.out.println("Tweet ID: " + tweet.getTweetID());
        System.out.println("Text: " + tweet.getTweetText());
        System.out.println("Class assigned: " + resultClass);
        System.out.println("-----------------------------------------------------");
    }
}
