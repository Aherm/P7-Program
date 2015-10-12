package businessLogicLayer;

import java.util.Date;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

// Created by Mads at 11-10-2015
public class DataAnalysis {

	private TweetStorage storage; 
	
	public DataAnalysis(TweetStorage storage){
		this.storage = storage;
	}
	
	public long nrGeotagged(){
		long amount = 0; 
		
		for(Tweet t : storage){
			if(t.isGeotagged())
				amount++; 
		}
		
		return amount; 
		
	}
	
	public void printStatistics(){
		System.out.println("Total tweets: " + storage.size() + "\n" +
						   "Geotagged Tweets: " + nrGeotagged() + "\n" + 
						   "Procent Geotagged Tweets: " + (
								   ((double)nrGeotagged() / (double)storage.size()) * 100) + " %");
	}
	
	
	
	
	
}
