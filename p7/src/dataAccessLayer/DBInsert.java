package dataAccessLayer;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

public class DBInsert {

	public DBInsert() {}

	public static void insertTweets(TweetStorage tweets) {
		try {
			Connection con = DBConnect.getInstance().getCon();
			String insertSQL= "INSERT INTO tweets " +
							  "(tweetID, userID, responseID, retweetID, tweetText, createdAt, " +
							  "lat, lon)" + " VALUES " +
							  "(?, ?, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement st = con.prepareStatement(insertSQL);
			// Remove the comments in this method if performance issues become a problem
			//int i = 0;

			for(int i = 0; i < tweets.size(); i++) {
				Tweet tweet = tweets.get(i);

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

		}
		catch(Exception E) {
			E.printStackTrace();
		}
	}
}