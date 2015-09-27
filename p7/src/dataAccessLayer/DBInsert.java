package dataAccessLayer;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DBInsert {
	
	public DBInsert(){}
	
	public void insertTweet(TweetStorage tweets, Date date) {
		try{
			Connection con = DBConnect.getInstance().getCon();
			String insertSQL= "INSERT INTO tweets " +
							  "(tweetID, userID, responseID, retweetID, tweetText, createdAt, " +
							  "lat, lon)" + " VALUES " +
							  "(?, ?, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement st = con.prepareStatement(insertSQL);
			// Remove the comments in this method if performance issues become a problem
			//int i = 0;

			for(int i = tweets.size() - 1; i >= 0; i--) {
				Tweet tweet = tweets.get(i);
				if (tweet.getCreatedAt().before(date)) {
					break;
				}
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
				
				st.setLong(1, tweet.getTweetID());
				st.setLong(2, tweet.getUserID());
				st.setLong(3, tweet.getResponseID());
				st.setLong(4, tweet.getRetweetID());
				st.setString(5, tweet.getTweetText());
				st.setString(6, sdf.format(tweet.getCreatedAt()));
				st.setDouble(7, tweet.getLat());
				st.setDouble(8, tweet.getLon());
				st.addBatch();
				//i++;
				//if(i % 1000 == 0 || i == tweets.size())
				//		st.executeBatch();
			}
			st.executeBatch();
			
		} catch(Exception E) {
			E.printStackTrace();
		}
	}
	
	/*
	public void insertKeywords(HashMap<String, Tweet> tweets)
	{
		try{
			Connection con = DBConnect.getInstance().getCon();
			String insertSQL = "INSERT INTO keywords " +
					"(tweetID, keyword) VALUES " +
					"(?, ?)";
			
			PreparedStatement st = con.prepareStatement(insertSQL);
			
			for (Map.Entry<String, Tweet> entry : tweets.entrySet()) {
				String key = entry.getKey();
				Tweet value = entry.getValue();
				
				for(String keyword : value.getKeywords())
				{
					st.setLong(1, value.getTweetID());		
					st.setString(2, keyword);
					st.addBatch();
				}
			}
			st.executeBatch();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	*/
}