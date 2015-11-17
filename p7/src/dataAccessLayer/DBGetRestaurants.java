package dataAccessLayer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class DBGetRestaurants {
	private static List<String> listQuery(String query) { // Give this a proper name

		List<String> restaurants = new ArrayList<String>();
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
	
	private static List<String> initializeRestaurants(ResultSet res) {
		List<String> restaurants = new ArrayList<String>();
		try {
			while (res.next()) {
				String newRestaurant = "";
				newRestaurant = res.getString("name");
				if (newRestaurant != null)
					restaurants.add(newRestaurant.toLowerCase());
			}
		}
		catch (Exception exh) {
			System.out.println(exh);
			exh.printStackTrace();
		}
		return restaurants;
	}
	
	public static List<String> getRestaurants() {
		return listQuery("SELECT DISTINCT name, street FROM dohmh");
	}
}
