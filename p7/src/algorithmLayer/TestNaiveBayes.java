package algorithmLayer;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.StreamHandler;

public class TestNaiveBayes {

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
        NaiveBayes multinomialNB = new Multinomial();
        NaiveBayes bernoulliNB = new Bernoulli();

        ProbabilityModel probabilityModel = multinomialNB.train(bookClassLabels, bookTrainingSet);
        //Possibly find way to store .jar stopped at this point and executes on with provided argument

        /**
         * Test phase
         */
        List<Map<String, Double>> results = new ArrayList<Map<String, Double>>();
        TweetStorage bookTestSet = Data.initializeTestSet();
        for (Tweet tweet : bookTestSet) {
            System.out.println(multinomialNB.apply(bookClassLabels, probabilityModel, tweet));
        	//results.add(multinomialNB.applyGetProbability(bookClassLabels, probabilityModel, tweet));
            //printResults(resultClass, tweet);
        }
    }

    private static void printResults(String resultClass, Tweet tweet){
        System.out.println("-----------------------------------------------------");
        System.out.println("Tweet ID: " + tweet.getTweetID());
        System.out.println("Text: " + tweet.getTweetText());
        System.out.println("Class assigned: " + resultClass);
        System.out.println("-----------------------------------------------------");
    }
}
