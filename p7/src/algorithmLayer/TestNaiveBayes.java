package algorithmLayer;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

import java.util.*;

public class TestNaiveBayes {

    private void testMultinomialBigDecimal(ArrayList<String> classLabels, TweetStorage trainingSet){
        MultinomialBigDecimal multinomialNBBD = new MultinomialBigDecimal();

        //ProbabilityModel probabilityModel = multinomialNB.train(bookClassLabels, bookTrainingSet);
        ProbabilityModelBigDecimal probabilityModelBigDecimal = multinomialNBBD.trainBigDecimal(classLabels, trainingSet);
        //Possibly find way to store .jar stopped at this point and executes on with provided argument

        /**
         * Test phase
         */
        List<Map<String, Double>> results = new ArrayList<Map<String, Double>>();
        //List<Map<String, Map<String, BigDecimal>>> resultsBigDecimal = new ArrayList<Map<String, Map<String, BigDecimal>>>();

        TweetStorage bookTestSet = Data.initializeTestSet();
        for (Tweet tweet : bookTestSet) {
            String predictedClass = multinomialNBBD.applyBigDecimal(classLabels, probabilityModelBigDecimal, tweet);
            System.out.println("predicted class: " + predictedClass);
            //Map<String, BigDecimal> probability = multinomialNBBD.applyGetProbability(bookClassLabels, probabilityModelBigDecimal, tweet);
            //Map<String, Map<String, BigDecimal>> store = new HashMap<String, Map<String, BigDecimal>>();
            //store.put(predictedClass, probability);
            //resultsBigDecimal.add(store);
            //results.add(multinomialNB.applyGetScore(bookClassLabels, probabilityModel, tweet));
            //printResults(resultClass, tweet);
        }
    }

    private static void testMultinomialProbability(ArrayList<String> classLabels, TweetStorage trainingSet){
        Multinomial multinomialNB = new Multinomial();

        ProbabilityModel probabilityModel = multinomialNB.train(classLabels, trainingSet);
        //Possibly find way to store .jar stopped at this point and executes on with provided argument

        /**
         * Test phase
         */
        List<Map<String, Double>> results = new ArrayList<Map<String, Double>>();
        TweetStorage bookTestSet = Data.initializeTestSet();
        for (Tweet tweet : bookTestSet) {
            String predictedClass = multinomialNB.applyProbability(classLabels, probabilityModel, tweet);
            Map<String, Double> score = multinomialNB.applyProbabilityGetScore(classLabels, probabilityModel, tweet);
            results.add(score);
            System.out.println("predicted class: " + predictedClass);
            //printResults(resultClass, tweet);
        }
    }

    public static void main(String[] args){
        /**
         * Initialization of data
         */
        //btc, consider using a HashSet to avoid duplicate classes
        ArrayList<String> classLabels = new ArrayList<String>(Arrays.asList("Not visited", "Visited"));
        ArrayList<String> bookClassLabels = Data.initializeClassLabels();

        TweetStorage bookTrainingSet = Data.initializeTrainingSet(bookClassLabels);

        //Consider making a separate Document class as a wrapper of a tweet that provides access to the most
        //relevant fields of the tweet in relation to naiveBayes

        //Need to fetch actual training data from database and use below as test data
        //Possible issue: Make sure that all tweets in the tweetstorage are associated to a class

        /**
         * Training phase
         */

        testMultinomialProbability(bookClassLabels, bookTrainingSet);
    }

    private static void printResults(String resultClass, Tweet tweet){
        System.out.println("-----------------------------------------------------");
        System.out.println("Tweet ID: " + tweet.getTweetID());
        System.out.println("Text: " + tweet.getTweetText());
        System.out.println("Class assigned: " + resultClass);
        System.out.println("-----------------------------------------------------");
    }
}
