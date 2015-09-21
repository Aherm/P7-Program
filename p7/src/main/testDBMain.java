package main;
import java.sql.Statement;
import java.util.Date;
import java.sql.Connection;
import java.sql.ResultSet;

import dataAccessLayer.DBConnect;
import dataAccessLayer.DBInsert;

public class testDBMain {
	public static void main(String[] args) 
	{
		DBConnect connection = new DBConnect();
		connection.connectToLocal("postgres", "postgres", "21");
		DBInsert dbInsert = new DBInsert(connection);
		dbInsert.Insert(421, 1337, 0, 0, "Skrid mads omg", new Date(), 50.23, -23.21, "food, poison, sick");
		DBConnect.closeConnection();
	}
}