package modelLayer;

import java.util.List;

import businessLogicLayer.Filter;

public class Restaurant implements OurLocation {
	private String name;
	private double lat, lon;
	private TweetStorage matchedTweets = new TweetStorage();
	private int score = -1;
	
	public Restaurant (String name, double lat, double lon) {
		this.name = name;
		this.lat = lat;
		this.lon = lon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public int calculateScore(Grid g, InvertedIndex ii, double dist) {
		this.matchedTweets = g.rangeQuery(this, dist);
		TweetStorage temp = Filter.restaurantNameQuery(this, ii);
		// Removes first to make sure the same tweet isn't there twice.
		this.matchedTweets.removeAll(temp);
		this.matchedTweets.addAll(temp);
		
		this.score = matchedTweets.size();
		return this.score;
	}
	
	public static void scoreAll(List<Restaurant> list, Grid g, InvertedIndex ii, double dist) {
		for (Restaurant r : list) {
			r.calculateScore(g, ii, dist);
		}
	}
}
