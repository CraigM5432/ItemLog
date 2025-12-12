/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author murph
 */
public class Event {
    private int eventID, userId;
    private String eventName, eventDate;
    
    public Event(int userID, String eventName, String eventDate){
        this.userId = userID;
        this.eventName = eventName;
        this.eventDate = eventDate;
    }
    
    public Event(int eventID, int userID, String eventName, String eventDate){
        this.eventID = eventID;
        this.userId = userID;
        this.eventName = eventName;
        this.eventDate = eventDate;
    }

    public int getEventID() {
        return eventID;
    }

    public int getUserId() {
        return userId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }
    
    
    
}
