/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author murph
 */
import JFrames.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.User;

public class UserDAO {
    
    // creating a new user record in the database 
    // user - username 
    // email - the user's emial address
    // passwordHash - hashed version of the user's password
    // returns true if the user was created successfully
    
    public boolean createUser(String username, String email, String passwordHash) {

        String sql = "INSERT INTO users(username, email, password_hash) VALUES (?, ?, ?)";
        
        // try with resources ensures the connection is automatically closed
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // parameterised query preventing SQL injection
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, passwordHash);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
            return false;
        }
    }

    
    // Verifying user credentials against the database
    // checkLogin checks whether a matching username and password hash exist.
    
    public boolean checkLogin(String username, String passwordHash) {

        String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, passwordHash);

            ResultSet rs = stmt.executeQuery();
            return rs.next();  // true if user exits

        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
            return false;
        }
    }
        
    // Authenticate's a user and returns a populated User object
    
    // login method is used after a successful login to retrieve the user's database ID and profile details
        public User login(String username, String passwordHash) {

            String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, username);
                stmt.setString(2, passwordHash);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    // build and return a User model object
                    return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email")
                    );
                }

            } catch (SQLException e) {
                System.out.println("Login error: " + e.getMessage());
            }

            return null; // login failed
        }

}
