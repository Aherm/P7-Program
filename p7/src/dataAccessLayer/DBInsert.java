package dataAccessLayer;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DBInsert {
	DBConnect connection;
	
	public DBInsert(DBConnect connection)
	{
		this.connection = connection;
	}
	
	public void insertTweet(TweetStorage tweets, Date date) {
		try{
			Connection con = connection.getInstance().getDBcon();
			String insertSQL= "INSERT INTO tweets " +
							  "(tweetID, userID, responseID, retweetID, tweet, createAt, " + 
							  "lat, lon)" + " VALUES " +
							  "(?, ?, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement st = con.prepareStatement(insertSQL);
			//int i = 0;			Remove these comments, if performance issues become a problem
					
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
			st.executeBatch();		//comment this is above comments are removed
			
		} catch(Exception E) {
			System.out.println("mads mor");
			E.printStackTrace();
		}
	}
	
	//if we need to insert keywords into the database at some point, this needs to be changed
	public void insertKeywordsPreparedStatement(HashMap<String, Tweet> tweets)
	{
		try{
			Connection con = connection.getInstance().getDBcon();
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
			System.out.println("lol mads mor XD og far :)))))");
			e.printStackTrace();
		}
	}
	
	public void insertKeywords(HashMap<String, Tweet> tweets)
	{
		try{
			Connection con = connection.getInstance().getDBcon();
			String insertSQL = "INSERT INTO keywords" +
					"(tweetID, keyword) VALUES ";

			for (Map.Entry<String, Tweet> entry : tweets.entrySet()) {
				String key = entry.getKey();
				Tweet value = entry.getValue();

				for (String kw : value.getKeywords()){
					insertSQL += "(" + value.getTweetID() + "," + "'" + kw + "'" + ") , ";
				}
			}

			insertSQL = insertSQL.substring(0, insertSQL.length() - 1);
			insertSQL += ";";
			System.out.println("query for insertKeywords: " + insertSQL);

			Statement statement = con.createStatement();

			// insert the data
			statement.executeUpdate(insertSQL);
		}
		catch(Exception E) {
			System.out.println("problems in DBInsert : mads dad and mom");
			E.printStackTrace();
		}
	}
}