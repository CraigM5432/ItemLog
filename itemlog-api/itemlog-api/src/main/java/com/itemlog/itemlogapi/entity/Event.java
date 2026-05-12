package com.itemlog.itemlogapi.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

// Entity representing an event created by a user.
// Events act as the parent context for items and transactions.
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Integer eventId;

    // Each event belongs to one user.
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    // Database-managed timestamp.
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // Empty constructor required by JPA.
    public Event() {}

    // Getter for eventId.
    public Integer getEventId() { return eventId; }

    // Setter for eventId.
    public void setEventId(Integer eventId) { this.eventId = eventId; }

    // Getter for user.
    public User getUser() { return user; }

    // Setter for user.
    public void setUser(User user) { this.user = user; }

    // Getter for eventName.
    public String getEventName() { return eventName; }

    // Setter for eventName.
    public void setEventName(String eventName) { this.eventName = eventName; }

    // Getter for eventDate.
    public LocalDate getEventDate() { return eventDate; }

    // Setter for eventDate.
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }

    // Getter for createdAt.
    public LocalDateTime getCreatedAt() { return createdAt; }
}