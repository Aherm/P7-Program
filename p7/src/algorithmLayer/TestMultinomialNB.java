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
        ArrayList<String> classLabels = new ArrayList<String>(Arrays.asList("Not visited", "Visited"));
        ArrayList<String> bookClassLabels = initializeClassLabels();

        TweetStorage bookTrainingSet = initializeTrainingSet(bookClassLabels);

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
        ProbabilityModel probabilityModel = naiveBayes.trainMultinomialNB(bookClassLabels, bookTrainingSet);
        //Possibly find way to store .jar stopped at this point and executes on with provided argument

        /**
         * Test phase
         */
        TweetStorage bookTestSet = initializeTestSet();
        TweetStorage testSet = new TweetStorage(
                new ArrayList<Tweet>(Arrays.asList(
                        new Tweet(2, 2, 3, 4, "sick of this shit", new Date(), -73, 41, classLabels.get(0)),
                        new Tweet(2, 2, 3, 4, "Got sick, mcdonalds fucking sucks", new Date(), -73, 41, classLabels.get(1)),
                        new Tweet(2, 2, 3, 4, "Im sick of my hater", new Date(), -73, 41, classLabels.get(0)),
                        new Tweet(2, 2, 3, 4, "I love people, but hate when do weird shit", new Date(), -73, 41, classLabels.get(0)),
                        new Tweet(2, 2, 3, 4, "lol lol lol why you mad", new Date(), -73, 41, classLabels.get(1)))
                )
        );
        for (Tweet tweet : bookTestSet) {
            String resultClass = naiveBayes.applyMultinomialNB(bookClassLabels, probabilityModel, tweet);

            printResults(resultClass, tweet);

        }
    }

    private static ArrayList<String> initializeClassLabels(){
        return new ArrayList<String>(Arrays.asList("UK","China","poultry","coffee","elections","sports"));
    }

    private static TweetStorage initializeTrainingSet(ArrayList<String> classLabels) {
        return new TweetStorage(
                new ArrayList<Tweet>(Arrays.asList(
                        new Tweet(2, 2, 3, 4, "congestion London", new Date(), -73, 41, classLabels.get(0)),
                        new Tweet(2, 2, 3, 4, "Parliament Big Ben", new Date(), -73, 41, classLabels.get(0)),
                        new Tweet(2, 2, 3, 4, "Windsor the Queen", new Date(), -73, 41, classLabels.get(0)),
                        new Tweet(2, 2, 3, 4, "Olympics Beijing", new Date(), -73, 41, classLabels.get(1)),
                        new Tweet(2, 2, 3, 4, "tourism Great Wall", new Date(), -73, 41, classLabels.get(1)),
                        new Tweet(2, 2, 3, 4, "Mao communist", new Date(), -73, 41, classLabels.get(1)),
                        new Tweet(2, 2, 3, 4, "feed chicken", new Date(), -73, 41, classLabels.get(2)),
                        new Tweet(2, 2, 3, 4, "pate ducks", new Date(), -73, 41, classLabels.get(2)),
                        new Tweet(2, 2, 3, 4, "bird flu turkey", new Date(), -73, 41, classLabels.get(2)),
                        new Tweet(2, 2, 3, 4, "roasting beans", new Date(), -73, 41, classLabels.get(3)),
                        new Tweet(2, 2, 3, 4, "arabica robusta", new Date(), -73, 41, classLabels.get(3)),
                        new Tweet(2, 2, 3, 4, "Kenya harvest", new Date(), -73, 41, classLabels.get(3)),
                        new Tweet(2, 2, 3, 4, "recount votes", new Date(), -73, 41, classLabels.get(4)),
                        new Tweet(2, 2, 3, 4, "seat run-off", new Date(), -73, 41, classLabels.get(4)),
                        new Tweet(2, 2, 3, 4, "TV ads campaign", new Date(), -73, 41, classLabels.get(4)),
                        new Tweet(2, 2, 3, 4, "diamond baseball", new Date(), -73, 41, classLabels.get(5)),
                        new Tweet(2, 2, 3, 4, "forward soccer", new Date(), -73, 41, classLabels.get(5)),
                        new Tweet(2, 2, 3, 4, "team captain", new Date(), -73, 41, classLabels.get(5))
                ))
        );
    }

    //Note that these collections will not give the expected result of "China" as none of the words in this
    //tweet have ever been seen before
    private static TweetStorage initializeTestSet() {
        TweetStorage trainingSet = new TweetStorage(
                new ArrayList<Tweet>(Arrays.asList(
                        new Tweet(2, 2, 3, 4, "first private Chinese airline", new Date(), -73, 41)
                ))
        );
        return trainingSet;
    }

    private static void printResults(String resultClass, Tweet tweet){
        System.out.println("-----------------------------------------------------");
        System.out.println("Tweet ID: " + tweet.getTweetID());
        System.out.println("Text: " + tweet.getTweetText());
        System.out.println("Class assigned: " + resultClass);
        System.out.println("-----------------------------------------------------");
    }
}
