package streaming;

import businessLogicLayer.Clustering;
import businessLogicLayer.DataAnalysis;
import businessLogicLayer.Filter;
import businessLogicLayer.Scoring;
import evaluationLayer.Rank;
import fileCreation.GenericPrint;
import fileCreation.GpxCreator;
import fileCreation.StatisticsWriter;
import modelLayer.ClusterStorage;
import modelLayer.Grid;
import modelLayer.InvertedIndex;
import modelLayer.Restaurant;
import modelLayer.TweetStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class TweetQueryThread extends Thread {
	private TweetStorage tweets;
	private List<Restaurant> restaurants; 
	private InvertedIndex invertedIndex;
	private Grid grid; 
	private Scoring scoring = new Scoring();

	public TweetQueryThread(TweetStorage tweets, List<Restaurant> restaurants, InvertedIndex invertedIndex,Grid grid) {
		this.tweets = tweets;
		this.restaurants = restaurants;
		this.invertedIndex = invertedIndex;
		this.grid = grid; 
	}

	public void run() {
		System.out.println("Press 1 to get amount of tweets in main memory, 2 Score tweets in main memory");
		try	{
			Scanner sc = new Scanner(System.in);
			boolean running = true;
			while (running) {
				int i = sc.nextInt();
				if (i == 1 || i == 2) {
					switch (i) {
						case 1:
							System.out.println("Tweet size is " + tweets.size());
							break;
						case 2:
							scoring.ScoreSystem(grid, invertedIndex, tweets, restaurants);
							handleScores();
							break;
						default:
							running = false;
					}
				}
				else
					System.out.println("Only 1 or 2 are accepted as input");
			}
			sc.close();
		} catch (Exception e){
			System.out.println(e);
			run();
		}
	}
	
	private void handleScores(){
		StringBuilder geoScores = new StringBuilder();
		StringBuilder nameScores = new StringBuilder();
		StringBuilder combinedScores = new StringBuilder();
		
		int geoCounter = 0; 
		for(Restaurant r : scoring.geoScore.keySet()){
			if(scoring.geoScore.get(r).doubleValue() > 0){
				geoScores.append(r.getName() + " has score " + scoring.geoScore.get(r).doubleValue() + "\n"); 
				geoCounter++; 
			}
			
		}
		
		int nameCounter = 0;
		for(Restaurant r : scoring.nameScore.keySet()){
			if(scoring.nameScore.get(r).doubleValue() > 0){
				nameScores.append(r.getName() + " has score " + scoring.nameScore.get(r).doubleValue() + "\n");
				nameCounter++; 
			}
			
		}
		
		int combinedCounter = 0; 
		for(Restaurant r: scoring.combinedScore.keySet()){
			if(scoring.combinedScore.get(r).doubleValue() > 0){
				combinedScores.append(r.getName() + " has score " + scoring.combinedScore.get(r).doubleValue() + "\n");
				combinedCounter++;	
			}
		}
		
		if(checkForZero(geoCounter, "Loactions")){
			System.out.println("GEOSCORES------------");
			System.out.println(geoScores.toString());
		}
		
		if(checkForZero(nameCounter, "Mentions")){
			System.out.println("MENTIONSCORES------------");
			System.out.println(nameScores.toString());
		}
		
		if(checkForZero(combinedCounter, "Combined")){
			System.out.println("COMBINEDSCORES------------");
			System.out.println(combinedScores.toString());
		}
		
	}
	
	private boolean checkForZero(int counter,String method){
		
		if(counter == 0){
			System.out.println("No restaurants scored for: " + method);
			return false; 
		}
		
		return true;
	}
	
}
