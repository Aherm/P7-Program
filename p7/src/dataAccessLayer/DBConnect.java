package dataAccessLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    private static Connection conn;
    private static DBConnect instance = null;

    // Specifies the address of the driver, located within the added postgressql libraries.
    // Alternative for mysql: com.mysql.jdbc.Driver , provided that the driver is included within the project.
    private static final String driver = "org.postgresql.Driver";

    // Specifies the address of the server and that the server uses postgresql.
    // Alternative for mysql: jdbc:mysql://localhost/
    private static final String url = "jdbc:postgresql://172.25.23.162/";

    private static final String urlLocal = "jdbc:postgresql://localhost/";
    
    // The name of the database
    private static final String dbName = "gis";

    // The database user
    private static final String userName = "gisuser";

    // The database password
    private static final String password = "42";

    
    // Connects to the server
    public DBConnect(){

    }
    
    // new Connect 
    public void connectTo(String dbname){
    	
    	try {
             // Specifies driver details
             Class.forName(driver).newInstance();

             System.out.println("Connecting to database");

             // Specifies connection details
             conn = DriverManager.getConnection(url + dbname, userName, password);

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
    
    public void connectToLocal(String dbname,String _userName, String _password){
    	
    	try {
             // Specifies driver details
             Class.forName(driver).newInstance();

             System.out.println("Connecting to database");

             // Specifies connection details
             conn = DriverManager.getConnection(urlLocal + dbname, _userName, _password);

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
    public Connection getDBcon(){
        return conn;
    }

    public static DBConnect getInstance(){
        if (instance == null){
            instance = new DBConnect();
        }
        return instance;
    }


    public static void closeConnection(){
        try{
            conn.close();
            System.out.println("The connection is closed");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}