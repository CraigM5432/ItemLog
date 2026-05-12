package com.itemlog.itemlogapi.repository;

import com.itemlog.itemlogapi.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Repository for the Item entity.
// Provides built-in CRUD methods through JpaRepository.
public interface ItemRepository extends JpaRepository<Item, Integer> {

    // Finds all items belonging to a specific event.
    List<Item> findByEvent_EventId(Integer eventId);

    // Finds a specific item only if it belongs to the specified event.
    // This helps prevent accessing items from the wrong event.
    Optional<Item> findByItemIdAndEvent_EventId(Integer itemId, Integer eventId);
}


