package com.itemlog.itemlogapi.repository;

import com.itemlog.itemlogapi.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Repository for the Event entity.
// Provides built-in CRUD methods through JpaRepository.
public interface EventRepository extends JpaRepository<Event, Integer> {

    // Finds all events owned by a specific user.
    List<Event> findByUser_UserId(Integer userId);

    // Finds a specific event only if it belongs to the specified user.
    // This helps enforce user ownership checks.
    Optional<Event> findByEventIdAndUser_UserId(Integer eventId, Integer userId);

    // Checks whether a specific event exists for a specific user.
    boolean existsByEventIdAndUser_UserId(Integer eventId, Integer userId);
}

