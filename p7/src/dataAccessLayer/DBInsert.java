package dataAccessLayer;

import modelLayer.Tweet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBInsert {
	DBConnect connection;
	
	public DBInsert(DBConnect connection)
	{
		this.connection = connection;
	}
	
	public void insertTweetPreparedStatement(HashMap<String, Tweet> tweets)
	{
		Connection con = connection.getInstance().getDBcon();
		try{
			String insertSQL= "INSERT INTO tweets" +
							  "(tweetID, userID, responseID, retweetID, tweet, createAt, " + 
							  "lat, lon) VALUES" +
							  "(?, ?, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement st = con.prepareStatement(insertSQL);
			//int i = 0;			Remove these comments, if performance issues become a problem
			for(Map.Entry<String, Tweet> entry : tweets.entrySet()){
				String key = entry.getKey();
				Tweet value = entry.getValue();
				
				Timestamp timestamp = new Timestamp(value.getCreatedAt().getTime());
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
				
				st.setLong(1, value.getTweetID());
				st.setLong(2, value.getUserID());
				st.setLong(3, value.getResponseID());
				st.setLong(4, value.getRetweetID());
				st.setString(5, value.getTweetText());
				st.setString(6, sdf.format(timestamp));
				st.setDouble(7, value.getLat());
				st.setDouble(8, value.getLon());
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
	
	public void insertTweet(HashMap<String, Tweet> tweets)
	{
		Connection con = connection.getInstance().getDBcon();
		try {
			String insertSQL = "INSERT INTO tweets" +
				"(tweetID, userID, responseID, retweetID, tweet, createAt, lat, lon) VALUES ";
									//"(?, ?, ?, ?, ?, ?, ?, ?)";

			for (Map.Entry<String, Tweet> entry : tweets.entrySet()) {
				String key = entry.getKey();
				Tweet value = entry.getValue();

				Timestamp timestamp = new Timestamp(value.getCreatedAt().getTime());
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

				insertSQL += "(" + value.getTweetID() + "," + value.getUserID() + "," + value.getResponseID() + "," + value.getRetweetID() + "," + "'" + value.getTweetText() + "'"  + "," +
						"'" + value.getCreatedAt() + "'" + "," + value.getLat() + "," + value.getLon() + "),";
			}

			insertSQL = insertSQL.substring(0,insertSQL.length()-1);
			insertSQL += ";";
			System.out.println("query for insertTweets: " + insertSQL);


			/*
			PreparedStatement pst = con.prepareStatement(insertSQL);
			pst.setInt(1, tweetID);
			pst.setInt(2, userID);
			pst.setInt(3, responseID);
			pst.setInt(4, retweetID);
			pst.setString(5, tweetText);
			pst.setString(6, sdf.format(timestamp));
			pst.setDouble(7, lat);
			pst.setDouble(8, lon);
			*/
			//pst.executeUpdate();

			Statement statement = con.createStatement();

			// insert the data
			statement.executeUpdate(insertSQL);
		}
		catch(Exception E) {
			System.out.println("problems in DBInsert");
			E.printStackTrace();
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
			System.out.println("problems in DBInsert");
			E.printStackTrace();
		}
	}
}