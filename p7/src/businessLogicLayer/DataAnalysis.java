package businessLogicLayer;

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
	
	public TweetStorage geotagged(){
		TweetStorage ts = new TweetStorage(); 
		for(Tweet t : storage){
			if(t.isGeotagged()){
				ts.add(t);
			}
		}
		
		return ts; 
	}

	
	public void printStatistics(){
		System.out.println("Total tweets: " + storage.size() + "\n" +
						   "Geotagged Tweets: " + nrGeotagged() + "\n" + 
						   "Procent Geotagged Tweets: " + (
								   ((double)nrGeotagged() / (double)storage.size()) * 100) + " %");
	}
	
	public void clusterAnalysis(List<Cluster> clusters, int numbers){
		int[] sizes = new int[numbers];
		
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
		
		System.out.println("Total amount of clusters: " + clusters.size());
		for(int i = 0; i < sizes.length; i++){
			if(sizes[i] > 0 )
				System.out.println("nr " + i + " Size: " + sizes[i]);
		}
		
	}
	
	
	
	
	
}
