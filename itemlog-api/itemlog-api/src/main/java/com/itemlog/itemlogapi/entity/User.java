package com.itemlog.itemlogapi.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

// Entity representing an application user.
// Each user can own multiple events, and password hashes are hidden from JSON responses.
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    // Stores the BCrypt hash only; never expose this value in API responses.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    // Empty constructor required by JPA.
    public User() {}


    public Integer getUserId() { return userId; }


    public void setUserId(Integer userId) { this.userId = userId; }


    public String getUsername() { return username; }


    public void setUsername(String username) { this.username = username; }


    public String getEmail() { return email; }


    public void setEmail(String email) { this.email = email; }


    public String getPasswordHash() { return passwordHash; }


    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    // One user can have many events.
    // JsonIgnore avoids recursive JSON output through entity relationships.
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Event> events;
}
