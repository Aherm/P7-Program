package algorithmLayer;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class TestMultinomialNB {

    public static void main(String[] args) {
        ArrayList<String> classSet = new ArrayList<String>();
        //Consider making a separate Document class as a wrapper of a tweet that provides access to the most
        //relevant fields of the tweet in relation to naiveBayes

        //Need to fetch actual training data from database and use below as test data
        TweetStorage trainingSet = new TweetStorage(
                new ArrayList<Tweet>(Arrays.asList(
                        new Tweet(2, 2, 3, 4, "sick of this shit", new Date(), -73, 41),
                        new Tweet(2, 2, 3, 4, "Got sick, mcdonalds fucking sucks", new Date(), -73, 41),
                        new Tweet(2, 2, 3, 4, "Im sick of my hater", new Date(), -73, 41),
                        new Tweet(2, 2, 3, 4, "I love people, but hate when do weird shit", new Date(), -73, 41),
                        new Tweet(2, 2, 3, 4, "lol lol lol why you mad", new Date(), -73, 41))
                )
        );

        MultinomialNBUpdate naiveBayes = new MultinomialNBUpdate();
        ProbabilityUpdate probabilityModel = naiveBayes.trainMultinomialNB(classSet, trainingSet);


        TweetStorage testSet = new TweetStorage(
                new ArrayList<Tweet>(Arrays.asList(
                        new Tweet(2, 2, 3, 4, "sick of this shit", new Date(), -73, 41),
                        new Tweet(2, 2, 3, 4, "Got sick, mcdonalds fucking sucks", new Date(), -73, 41),
                        new Tweet(2, 2, 3, 4, "Im sick of my hater", new Date(), -73, 41),
                        new Tweet(2, 2, 3, 4, "I love people, but hate when do weird shit", new Date(), -73, 41),
                        new Tweet(2, 2, 3, 4, "lol lol lol why you mad", new Date(), -73, 41))
                )
        );

        //naiveBayes.applyMultinomialNB();
    }

}
