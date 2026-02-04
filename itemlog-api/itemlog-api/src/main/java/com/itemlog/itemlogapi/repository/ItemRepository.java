package com.itemlog.itemlogapi.repository;

import com.itemlog.itemlogapi.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findByEvent_EventId(Integer eventId);

    Optional<Item> findByItemIdAndEvent_EventId(Integer itemId, Integer eventId);
}


