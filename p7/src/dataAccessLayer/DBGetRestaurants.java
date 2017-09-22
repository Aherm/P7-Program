package dataAccessLayer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import evaluationLayer.Rank;
import modelLayer.Restaurant;
import utility.Tuple;

public class DBGetRestaurants {
	private static Tuple<List<Restaurant>, List<Rank>> listQuery(String query) { // Give this a proper name

		Tuple<List<Restaurant>, List<Rank>> restaurantAndRanks = null; 
		try {
			Connection con = DBConnect.getInstance().getCon();

			Statement stmt = con.createStatement();
			//stmt.execute("SET datestyle = \"ISO,DMY\"");
			ResultSet res = stmt.executeQuery(query);
			
			restaurantAndRanks = initializeRestaurants(res);
		}
		catch (Exception exh) {
			System.out.println(exh);
		}

		return restaurantAndRanks;
	}
	
	private static Tuple<List<Restaurant>, List<Rank>> initializeRestaurants(ResultSet res) {
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		List<Rank> ranks = new ArrayList<Rank>(); 
		try {
			while (res.next()) {
				String name = res.getString("name");
				double lat = res.getDouble("lat");
				double lon = res.getDouble("long"); 
				double score = res.getDouble("score");  
				Restaurant newRestaurant = new Restaurant(name, lat,lon); 
				restaurants.add(newRestaurant); 
				ranks.add(new Rank(newRestaurant,score)); 
			}
		}
		catch (Exception exh) {
			System.out.println(exh);
			exh.printStackTrace();
		}
		return new Tuple<List<Restaurant>, List<Rank>>(restaurants,ranks);
	}

	public static Tuple<List<Restaurant>, List<Rank>> getRestaurants() {
		//return listQuery("SELECT distinct name from dohmh WHERE lower(name) like '%tofu house%'");
		return listQuery("SELECT DISTINCT lower(name) as name,score,lat,long FROM restaurants");
	}
}
