package algorithmLayer;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Data {
    public static ArrayList<String> initializeClassLabels(){
        return new ArrayList<String>(Arrays.asList("UK","China","poultry","coffee","elections","sports"));
    }

    public static TweetStorage initializeTrainingSet(ArrayList<String> classLabels) {
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
    public static TweetStorage initializeTestSet() {
        TweetStorage trainingSet = new TweetStorage(
                new ArrayList<Tweet>(Arrays.asList(
                        new Tweet(2, 2, 3, 4, "first private Chinese airline", new Date(), -73, 41)
                ))
        );
        return trainingSet;
    }
}
