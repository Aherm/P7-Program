package businessLogicLayer;

import java.util.Date;
import java.util.List;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import org.joda.time.*;

import modelLayer.TweetStorage;
import streaming.Oauth;

//Created by Mads on 07-10-2015


public class TwitterRest {

	private int totalcalls = 0; // holds nr of times we use the twitter api
	private Twitter twitter; 
	
	public TwitterRest(){
		
		ConfigurationBuilder cb = new Oauth().createConfigBuilder();
		TwitterFactory factory = new TwitterFactory(cb.build());
		twitter = factory.getInstance();
	}
	
	
	public TweetStorage getUserTimeline3days(long userId, Date startDate){
		
		Paging page = new Paging(1,100);  
		
		try {
			List<Status> userTimeline = twitter.getUserTimeline(userId, page);
			
			Date lastDate = userTimeline.get(userTimeline.size() -1 ).getCreatedAt();
			
			}
		
		 catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return new TweetStorage(); 
		
	}
	
	
	
}
