package JFrames;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
   
    //responsible for establishing a connection between the application and the DB.

    //It centralises database connection logic so that DAO classes can easily resuse it without duplication code.
public class DBConnection {

    //JDBC connection URL pointing to the lcoal MySQL databse
    private static final String URL = "jdbc:mysql://localhost:3306/itemlog_db?useSSL=false&serverTimezone=UTC";
    
    // database username
    private static final String USER = "root";
    
    //// database password - stored locally for development purposes
    private static final String PASSWORD = "LiverpoolGremio93!"; 
    
    
    //establishes and returns a database connection
    public static Connection getConnection() {
        try {
            //attempt to establish a connection using JDBC
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection established to Database");
            return connection;
        } catch (SQLException e) {
            
            //handling database connection failure
            System.out.println("Connection Failed: " + e.getMessage());
            return null;
        }
    }
}

