package com.itemlog.itemlogapi.controller;

import com.itemlog.itemlogapi.dto.CreateItemRequest;
import com.itemlog.itemlogapi.dto.ItemResponse;
import com.itemlog.itemlogapi.dto.MessageResponse;
import com.itemlog.itemlogapi.dto.UpdateItemRequest;
import com.itemlog.itemlogapi.entity.Item;
import com.itemlog.itemlogapi.exception.NotFoundException;
import com.itemlog.itemlogapi.security.CurrentUser;
import com.itemlog.itemlogapi.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Handles item management within a selected event.
// User ownership is enforced by passing the authenticated userId into ItemService.
@RestController
@RequestMapping("/events/{eventId}/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    private static ItemResponse toResponse(Item i) {
        return new ItemResponse(
                i.getItemId(),
                i.getEvent().getEventId(),
                i.getName(),
                i.getPrice(),
                i.getSize(),
                i.getQuantity(),
                i.getImagePath(),
                i.getDescription(),
                i.getCreatedAt()
        );
    }

    private Integer currentUserId() {
        Integer userId = CurrentUser.id();

        if (userId == null) {
            throw new NotFoundException("User not found.");
        }

        return userId;
    }

    @GetMapping
    public ResponseEntity<List<ItemResponse>> listItems(@PathVariable Integer eventId) {
        Integer userId = currentUserId();

        List<ItemResponse> result = itemService.listItems(userId, eventId).stream()
                .map(ItemController::toResponse)
                .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponse> getItem(@PathVariable Integer eventId,
                                                @PathVariable Integer itemId) {
        Integer userId = currentUserId();

        Item item = itemService.getItem(userId, eventId, itemId);

        return ResponseEntity.ok(toResponse(item));
    }

    @PostMapping
    public ResponseEntity<ItemResponse> createItem(@PathVariable Integer eventId,
                                                   @Valid @RequestBody CreateItemRequest request) {
        Integer userId = currentUserId();

        Item saved = itemService.createItem(userId, eventId, request);

        return ResponseEntity.status(201).body(toResponse(saved));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable Integer eventId,
                                                   @PathVariable Integer itemId,
                                                   @Valid @RequestBody UpdateItemRequest request) {
        Integer userId = currentUserId();

        Item saved = itemService.updateItem(userId, eventId, itemId, request);

        return ResponseEntity.ok(toResponse(saved));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<MessageResponse> deleteItem(@PathVariable Integer eventId,
                                                      @PathVariable Integer itemId) {
        Integer userId = currentUserId();

        // The service blocks deletion if this item already has transaction history.
        itemService.deleteItem(userId, eventId, itemId);

        return ResponseEntity.ok(new MessageResponse("Item deleted"));
    }
}
