package algorithmLayer;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class TestMultinomialNB {

    public static void main(String[] args) {

        /**
         * Initialization of data
         */
        //btc, consider using a HashSet to avoid duplicate classes
        ArrayList<String> classLabels = new ArrayList<String>();
        classLabels.add("Not visited");
        classLabels.add("Visited");
        //Consider making a separate Document class as a wrapper of a tweet that provides access to the most
        //relevant fields of the tweet in relation to naiveBayes

        //Need to fetch actual training data from database and use below as test data
        //Possible issue: Make sure that all tweets in the tweetstorage are associated to a class
        TweetStorage trainingSet = new TweetStorage(
                new ArrayList<Tweet>(Arrays.asList(
                        new Tweet(2, 2, 3, 4, "sick of this shit", new Date(), -73, 41, classLabels.get(0)),
                        new Tweet(2, 2, 3, 4, "Got sick, mcdonalds fucking sucks", new Date(), -73, 41, classLabels.get(1)),
                        new Tweet(2, 2, 3, 4, "Im sick of my hater", new Date(), -73, 41, classLabels.get(0)),
                        new Tweet(2, 2, 3, 4, "I love people, but hate when do weird shit", new Date(), -73, 41, classLabels.get(0)),
                        new Tweet(2, 2, 3, 4, "lol lol lol why you mad", new Date(), -73, 41, classLabels.get(0)))
                )
        );

        /**
         * Training phase
         */
        MultinomialNBUpdate naiveBayes = new MultinomialNBUpdate();
        ProbabilityModel probabilityModel = naiveBayes.trainMultinomialNB(classLabels, trainingSet);
        //Possibly find way to store .jar stopped at this point and executes on with provided argument

        /**
         * Test phase
         */
        TweetStorage testSet = new TweetStorage(
                new ArrayList<Tweet>(Arrays.asList(
                        new Tweet(2, 2, 3, 4, "sick of this shit", new Date(), -73, 41, classLabels.get(0)),
                        new Tweet(2, 2, 3, 4, "Got sick, mcdonalds fucking sucks", new Date(), -73, 41, classLabels.get(1)),
                        new Tweet(2, 2, 3, 4, "Im sick of my hater", new Date(), -73, 41, classLabels.get(0)),
                        new Tweet(2, 2, 3, 4, "I love people, but hate when do weird shit", new Date(), -73, 41, classLabels.get(0)),
                        new Tweet(2, 2, 3, 4, "lol lol lol why you mad", new Date(), -73, 41, classLabels.get(1)))
                )
        );
        for (Tweet tweet : testSet){
            String resultClass = naiveBayes.applyMultinomialNB(classLabels, probabilityModel, tweet);
            System.out.println("-----------------------------------------------------");
            System.out.println("Tweet ID: " + tweet.getTweetID());
            System.out.println("Text: " + tweet.getTweetText());
            System.out.println("Class assigned: " + resultClass);
            System.out.println("-----------------------------------------------------");
        }
    }

}
