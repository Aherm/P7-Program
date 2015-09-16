package main;

import streaming.OurStatusListener;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		OurStatusListener listener = new OurStatusListener();
		TwitterStream stream = new TwitterStreamFactory().getInstance(); 
		stream.addListener(listener);
		
		//stream.sample();

        double[][] locations = new double[][]{
                {-74,40},
                {-73,41}
        };


        FilterQuery query = new FilterQuery();
        //query.language("da");
        query.locations(locations);
        //-74,40,-73,41
        stream.filter(query);

	}

}
