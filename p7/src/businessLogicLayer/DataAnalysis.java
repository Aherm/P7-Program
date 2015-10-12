package businessLogicLayer;

import java.util.Date;

import modelLayer.TweetStorage;

// Created at 11-10-2015
public class DataAnalysis {

	private TweetStorage storage; 
	
	public DataAnalysis(TweetStorage storage){
		this.storage = storage; 
	}
	
	public int nrTweetsAfterFilter(){
		return Filter.filterTweet(storage, new Date()).size(); 
	}
	
	
	
}
