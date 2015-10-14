package businessLogicLayer;

import java.util.Date;
import java.util.List;

import modelLayer.Cluster;
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
	
	public void clusterAnalysis(List<Cluster> clusters){
		int[] sizes = new int[52];
		
		for(int i = 0; i < sizes.length; i++){
			sizes[i] = 0;
		}
		
		for(Cluster c : clusters){
			if(c.getTweets().size() > sizes.length -1){
				sizes[sizes.length-1]++;
			}
			else
				sizes[c.getTweets().size()]++;
		}
		
		for(int i = 0; i < sizes.length; i++){
			System.out.println("nr " + i + " Size: " + sizes[i]);
		}
		
	}
	
	
	
	
	
}
