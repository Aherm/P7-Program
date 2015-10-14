package businessLogicLayer;

import dataAccessLayer.DBGetTweets;
import modelLayer.TweetStorage;
import java.util.*;

public class Batch {

	public static TweetStorage filterTweets(){
		TweetStorage ts = new TweetStorage(); 
		TweetStorage interval = new TweetStorage(); 
		int size = 10000; 
		int start = 1; 
		int itnr = 1; 
		DBGetTweets getT = new DBGetTweets();
		do{
			System.out.println("iteration: " + itnr);
			interval = getT.getInterval(start, size);
			ts.addAll(Filter.filterTweets(interval, new Date()));
		    System.out.println(interval.size());
		    if(!ts.isEmpty()){
		    	System.out.println(ts.getLast().getTweetText());
		    }
		    System.out.println("size so far:" + ts.size() );
		    System.out.println("start value " + start);
			start = start + size; 
			itnr++;
		}while(interval.size() == size); 
		
		return ts;
	}
}
