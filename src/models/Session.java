/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author murph
 */

 //used to main the autheicated user's state across the application

 // stores the currently logged in user so that other parts of the app can access user information
 // without querying the database over and over 
public class Session{
    
    // holds the currently authenticated user 
    private static User currentUser;
    
    // sets the active user after successful authentication
    public static void setUser(User user){
        currentUser = user;
    }
    
    //retrieves the currently logged in user 
    public static User getUser(){
        return currentUser;
    }
    
}
