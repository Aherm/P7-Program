package dataAccessLayer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import modelLayer.Restaurant;
import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class DBGetRestaurants {
	private static List<Restaurant> listQuery(String query) { // Give this a proper name

		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		try {
			Connection con = DBConnect.getInstance().getCon();

			Statement stmt = con.createStatement();
			//stmt.execute("SET datestyle = \"ISO,DMY\"");
			ResultSet res = stmt.executeQuery(query);
			
			restaurants = initializeRestaurants(res);
		}
		catch (Exception exh) {
			System.out.println(exh);
		}

		return restaurants;
	}
	
	private static List<Restaurant> initializeRestaurants(ResultSet res) {
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		try {
			while (res.next()) {
				String name = res.getString("name");
				double lat = res.getDouble("lat");
				double lon = res.getDouble("long"); 
				Restaurant newRestaurant = new Restaurant(name, lat, lon); 
				restaurants.add(newRestaurant); 
				
			}
		}
		catch (Exception exh) {
			System.out.println(exh);
			exh.printStackTrace();
		}
		return restaurants;
	}
	
	public static List<Restaurant> getRestaurants() {
		return listQuery("SELECT DISTINCT name, lat,long FROM reslocations");
	}
}
