package dataAccessLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    private static Connection con;
    private static DBConnect instance = null;

    // Specifies the address of the driver, located within the added postgressql libraries.
    // Alternative for mysql: com.mysql.jdbc.Driver , provided that the driver is included within the project.
    private static final String driver = "org.postgresql.Driver";
    private static final String url = "jdbc:postgresql://localhost/";

    public DBConnect(){

    }
    
    public void connectTo(String dbname,String _userName, String _password){
    	
    	try {
             // Specifies driver details
             Class.forName(driver).newInstance();

             System.out.println("Connecting to database");

             // Specifies connection details
             con = DriverManager.getConnection(url + dbname, _userName, _password);

             System.out.println("Connected" + " " + dbname);
         }
         catch (SQLException se){
             System.out.println(se);
             se.printStackTrace();
         }
         catch (Exception e){
             e.printStackTrace();
         }	
    }    

    // Getter function for other database classes to fetch the connection object
    public Connection getCon(){
        return con;
    }

    public static DBConnect getInstance(){
        if (instance == null){
            instance = new DBConnect();
        }
        return instance;
    }

    public static void closeConnection(){
        try{
            con.close();
            System.out.println("The connection is closed");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}