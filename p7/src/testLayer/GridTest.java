package testLayer;

import java.util.List;
import dataAccessLayer.DBConnect;
import dataAccessLayer.DBGetRestaurants;
import dataAccessLayer.DBGetTweets;
import fileCreation.GenericPrint;
import modelLayer.Grid;
import modelLayer.Restaurant;
import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class GridTest {
	private static long[][] results = new long[51][10];
	
	public static long testCase(List<Restaurant> restaurants, TweetStorage tweets, int n) {
		long startTime = System.nanoTime();
		Grid grid = new Grid(-74, -73, 40, 41, n, n);
		grid.addTweets(tweets);
		for (Restaurant r : restaurants) {
			grid.rangeQuery(r, 25);
		}
		
		for (Tweet t : tweets) {
			grid.removeTweet(t);
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
		
		for (int i = 0; i < 10; i++) {
			System.out.println("Run " + (i + 1));
			sb.append("Run nr: " + (i + 1) + "\n");
			sb.append(singleRun(tweets, restaurants, i));
			sb.append("\n\n");
		}
		
		sb.append("Averages over the runs \n");
		for (int i = 1; i <= 50; i++) {
			long averagetime = averageArray(results[i]);
			sb.append("Time for size " + i*100 + " is: " + averagetime/1000000 + "ms \n");
		}
		
		GenericPrint.PRINTER("./statistics/gridTest.txt", sb.toString());
		
		connection.closeConnection();
	}

	private static String singleRun(TweetStorage tweets, List<Restaurant> restaurants, int run) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 1; i <= 50; i++) {
			//System.out.println("Doing step: " + i);
			long time = testCase(restaurants, tweets, i*100);
			results[i][run] = time;
			sb.append("Time for size " + i*100 + " is: " + time/1000000 + "ms \n");
		}
		return sb.toString();
	}
	
	private static long averageArray(long[] array) {
		long sum = 0;
		for (long i : array) {
			sum += i;
		}
		return sum / array.length;
	}
}