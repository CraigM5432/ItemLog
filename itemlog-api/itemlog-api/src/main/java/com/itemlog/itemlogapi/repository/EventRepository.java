package com.itemlog.itemlogapi.repository;

import com.itemlog.itemlogapi.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findByUser_UserId(Integer userId);

    Optional<Event> findByEventIdAndUser_UserId(Integer eventId, Integer userId);

    boolean existsByEventIdAndUser_UserId(Integer eventId, Integer userId);
}

