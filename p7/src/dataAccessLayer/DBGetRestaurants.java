package dataAccessLayer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class DBGetRestaurants {
	private static List<String> listQuery(String query) {

		List<String> restaurants = new ArrayList<String>();

		try {
			Connection con = DBConnect.getInstance().getCon();

			Statement stmt = con.createStatement();
			//stmt.execute("SET datestyle = \"ISO,DMY\"");
			ResultSet res = stmt.executeQuery(query);

			restaurants = initializeTweets(res);
		}
		catch (Exception exh) {
			System.out.println(exh);
		}

		return restaurants;
	}
	
	private static List<String> initializeTweets(ResultSet res) {
		List<String> restaurants = new ArrayList<String>();
		try {
			while (res.next()) {
				String newRestaurant = "";
				newRestaurant = res.getString("name");
				restaurants.add(newRestaurant);
			}
		}
		catch (Exception exh) {
			System.out.println(exh);
			exh.printStackTrace();
		}
		return restaurants;
	}
	
	public static List<String> getRestaurants() {
		return listQuery("SELECT name FROM restaurants");
	}
}
