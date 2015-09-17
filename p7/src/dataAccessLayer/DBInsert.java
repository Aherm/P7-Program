package dataAccessLayer;

import java.sql.Connection;
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
	
	public void Insert(long tweetID, long userID, long responseID, long retweetID, String tweet, Date date, double lat, double lon, String keywords)
	{
		Connection con = connection.getInstance().getDBcon();
		Timestamp timestamp = new Timestamp(date.getTime()); 
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		try {
			Statement st = con.createStatement();
			st.executeQuery("INSERT INTO tweets VALUES (" + tweetID 				+ ", " 
			  											  + userID 					+ ", " 
														  + responseID 				+ ", " 
														  + retweetID 				+ ", \'" 
												 		  + tweet 					+ "\', \'" 
														  + sdf.format(timestamp)	+ "\', " 
														  + lat 					+ ", " 
														  + lon 					+ ", \'" 
														  + keywords + "\'"
														  + ")");
		}
		catch(Exception E) {
			System.out.println("Mads' mor");
			E.printStackTrace();
		}
		DBConnect.closeConnection();
	}
}
