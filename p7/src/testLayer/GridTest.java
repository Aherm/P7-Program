package testLayer;

import java.util.List;

import dataAccessLayer.DBConnect;
import dataAccessLayer.DBGetRestaurants;
import dataAccessLayer.DBGetTweets;
import fileCreation.GenericPrint;
import modelLayer.Grid;
import modelLayer.Restaurant;
import modelLayer.TweetStorage;

public class GridTest {
	public static long testCase(List<Restaurant> restaurants, TweetStorage tweets, int n) {
		long startTime = System.nanoTime();
		Grid grid = new Grid(-74, -73, 40, 41, n, n);
		grid.addTweets(tweets);
		for (Restaurant r : restaurants) {
			grid.rangeQuery(r, 25);
		}
		long endTime = System.nanoTime();
		
		return endTime - startTime;
	}
	
	public static void runTest() {
		DBConnect connection = DBConnect.getInstance();
		connection.connectToServer("jdbc:postgresql://172.25.26.208/", "world", "postgres", "21");
		
		TweetStorage tweets = DBGetTweets.getAllTweetsExperiment();
		List<Restaurant> restaurants = DBGetRestaurants.getRestaurants().x;
		
		StringBuilder sb = new StringBuilder();
		
		for (int i = 1; i <= 10; i++) {
			long time = testCase(restaurants, tweets, i*500);
			sb.append("Time for size " + i*500 + " is: " + time/1000000 + "ms \n");
		}
		
		GenericPrint.PRINTER("./statistics/gridTest.txt", sb.toString());
		
		connection.closeConnection();
	}
}