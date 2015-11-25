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
				//long lat = res.getLong("lat");
				//long lon = res.getLong("long"); 
				Restaurant newRestaurant = new Restaurant(name, 0, 0); 
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
		return listQuery("SELECT DISTINCT lower(name) as name FROM dohmh WHERE name IS NOT NULL");
	}
}
