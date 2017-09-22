package main;

import java.util.List;

import Processing.Stopwords;
import modelLayer.Grid;
import modelLayer.InvertedIndex;
import modelLayer.Restaurant;
import modelLayer.TweetStorage;
import naiveBayes.Multinomial;
import streaming.Oauth;
import streaming.OurStatusListener;
import streaming.TweetQueryThread;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import utility.Utils;

public class Program {

    public static void main(String[] args) {
        
    	//Twitter stream set up  
    	TwitterStreamFactory tsf = new TwitterStreamFactory(Oauth.createConfigBuilder().build());
        TwitterStream stream = tsf.getInstance();
        
        //Inverted index set up 
        InvertedIndex invertedIndex = new InvertedIndex();
        List<Restaurant> restaurants = Utils.getRestaurantsFromFile("restaurantData/resData.csv");
        for(Restaurant r: restaurants){
            r.setName(r.getName().toLowerCase());
            if (!r.getName().toLowerCase().contains("{iv}") && !r.getName().toLowerCase().contains("floor)"))
                invertedIndex.addEntry(r);

        }
        invertedIndex.precomputeResturantPatterns();
        
      //Load the multinominal naive bayes
        Multinomial multinomial; 
    	multinomial = Multinomial.loadClassifier("./classifiers/visitNaiveBayes.model");
    	multinomial.setStopwords(new Stopwords());
    	
        //Status listener set up 
        OurStatusListener listener = new OurStatusListener(invertedIndex,multinomial);
        stream.addListener(listener);;


        //bounding box for new york
        double[][] locations = new double[][]{
                {-74, 40}, //lon, lat
                {-73, 41}
        };
        
        //Filter out all tweets not from new york 
        FilterQuery query = new FilterQuery();
        query.locations(locations);
        stream.filter(query);
        
        //Set up and start of query thread
        TweetStorage allTweets = listener.getTweets();
        Grid grid = listener.getGrid();
        TweetQueryThread t = new TweetQueryThread(allTweets, restaurants,invertedIndex,grid);
        t.start();
    }
}
