/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author murph
 */
public class Session{
    
    private static User currentUser;
    
    public static void setUser(User user){
        currentUser = user;
    }
    
    public static User getUser(){
        return currentUser;
    }
    
}
