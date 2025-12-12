/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author murph
 */
public class User {
    
    private int userId;
    private String username;
    private String email;

    // REQUIRED for login + session
    public User(int userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public User(){
        
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}

    

