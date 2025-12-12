/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import JFrames.DBConnection;
import models.Event;

public class EventDAO {

    public int createEvent(Event event) {

        String sql = "INSERT INTO events (user_id, event_name, event_date) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, event.getUserId());
            stmt.setString(2, event.getEventName());
            stmt.setString(3, event.getEventDate());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Event creation failed, no rows affected.");
            }

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1); 
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; 
    }
}

