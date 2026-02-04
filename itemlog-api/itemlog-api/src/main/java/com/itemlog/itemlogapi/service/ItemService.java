package com.itemlog.itemlogapi.service;

import com.itemlog.itemlogapi.dto.CreateItemRequest;
import com.itemlog.itemlogapi.dto.UpdateItemRequest;
import com.itemlog.itemlogapi.entity.Event;
import com.itemlog.itemlogapi.entity.Item;
import com.itemlog.itemlogapi.exception.NotFoundException;
import com.itemlog.itemlogapi.repository.EventRepository;
import com.itemlog.itemlogapi.repository.ItemRepository;
import com.itemlog.itemlogapi.repository.TransactionRepository;
import com.itemlog.itemlogapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final UserRepository userRepo;
    private final EventRepository eventRepo;
    private final ItemRepository itemRepo;
    private final TransactionRepository transactionRepo;

    public ItemService(UserRepository userRepo,
                       EventRepository eventRepo,
                       ItemRepository itemRepo,
                       TransactionRepository transactionRepo) {
        this.userRepo = userRepo;
        this.eventRepo = eventRepo;
        this.itemRepo = itemRepo;
        this.transactionRepo = transactionRepo;
    }

    public List<Item> listItems(Integer userId, Integer eventId) {
        Event event = getEventOwnedByUser(userId, eventId);
        return itemRepo.findByEvent_EventId(event.getEventId());
    }

    public Item getItem(Integer userId, Integer eventId, Integer itemId) {
        getEventOwnedByUser(userId, eventId);

        return itemRepo.findByItemIdAndEvent_EventId(itemId, eventId)
                .orElseThrow(() -> new NotFoundException("Item not found for this event."));
    }

    public Item createItem(Integer userId, Integer eventId, CreateItemRequest request) {
        Event event = getEventOwnedByUser(userId, eventId);

        Item item = new Item();
        item.setEvent(event);
        item.setName(request.getName().trim());
        item.setPrice(request.getPrice());
        item.setSize(trimOrNull(request.getSize()));
        item.setQuantity(request.getQuantity());
        item.setImagePath(trimOrNull(request.getImagePath()));
        item.setDescription(trimOrNull(request.getDescription()));

        return itemRepo.save(item);
    }

    public Item updateItem(Integer userId, Integer eventId, Integer itemId, UpdateItemRequest request) {
        Item item = getItem(userId, eventId, itemId);

        if (request.getName() != null && !request.getName().isBlank()) {
            item.setName(request.getName().trim());
        }
        if (request.getPrice() != null) {
            item.setPrice(request.getPrice());
        }
        if (request.getSize() != null) {
            item.setSize(trimOrNull(request.getSize()));
        }
        if (request.getQuantity() != null) {
            item.setQuantity(request.getQuantity());
        }
        if (request.getImagePath() != null) {
            item.setImagePath(trimOrNull(request.getImagePath()));
        }
        if (request.getDescription() != null) {
            item.setDescription(trimOrNull(request.getDescription()));
        }

        return itemRepo.save(item);
    }

    public void deleteItem(Integer userId, Integer eventId, Integer itemId) {
        Item item = getItem(userId, eventId, itemId);

        // Blocking deletion if transactions exist
        if (transactionRepo.existsByItem_ItemId(itemId)) {
            throw new IllegalArgumentException(
                    "Cannot delete item because it has transactions."
            );
        }

        itemRepo.delete(item);
    }

    private Event getEventOwnedByUser(Integer userId, Integer eventId) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException("User not found.");
        }

        return eventRepo.findByEventIdAndUser_UserId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found for this user."));
    }

    private String trimOrNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}

