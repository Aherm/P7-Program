package dataAccessLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class DBInsert {
	DBConnect connection;
	
	public DBInsert(DBConnect connection)
	{
		this.connection = connection;
	}
	
	public void Insert(int tweetID, int userID, int responseID, int retweetID, String tweet, Date date, double lat, double lon, String keywords)
	{
		Connection con = connection.getInstance().getDBcon();
		Timestamp timestamp = new Timestamp(date.getTime()); 
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		try {
			
			String insertSQL= "INSERT INTO tweets" +
									"(tweetID, userID, responseID, retweetID, tweet, createAt, lat, lon, keywords) VALUES" +
									"(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pst = con.prepareStatement(insertSQL);
			pst.setInt(1, tweetID);
			pst.setInt(2, userID);
			pst.setInt(3, responseID);
			pst.setInt(4, retweetID);
			pst.setString(5, tweet);
			pst.setString(6, sdf.format(timestamp));
			pst.setDouble(7, lat);
			pst.setDouble(8, lon);
			pst.setString(9, keywords);
			pst.executeUpdate();
		}
		catch(Exception E) {
			System.out.println("Mads' mor");
			E.printStackTrace();
		}
		DBConnect.closeConnection();
	}
}
/*
Statement st = con.createStatement();
st.executeUpdate("INSERT INTO tweets VALUES (" + tweetID 				+ ", " 
			  											  + userID 					+ ", " 
														  + responseID 				+ ", " 
														  + retweetID 				+ ", \'" 
												 		  + tweet 					+ "\', \'" 
														  + sdf.format(timestamp)	+ "\', " 
														  + lat 					+ ", " 
														  + lon 					+ ", \'" 
														  + keywords + "\'"
														  + ")");
*/