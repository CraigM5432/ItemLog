package com.itemlog.itemlogapi.controller;

import com.itemlog.itemlogapi.dto.CreateItemRequest;
import com.itemlog.itemlogapi.dto.ItemResponse;
import com.itemlog.itemlogapi.dto.MessageResponse;
import com.itemlog.itemlogapi.dto.UpdateItemRequest;
import com.itemlog.itemlogapi.entity.Item;
import com.itemlog.itemlogapi.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events/{eventId}/items")
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

    @GetMapping
    public ResponseEntity<List<ItemResponse>> listItems(@PathVariable Integer userId,
                                                        @PathVariable Integer eventId) {

        List<ItemResponse> result = itemService.listItems(userId, eventId).stream()
                .map(ItemController::toResponse)
                .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponse> getItem(@PathVariable Integer userId,
                                                @PathVariable Integer eventId,
                                                @PathVariable Integer itemId) {
        Item item = itemService.getItem(userId, eventId, itemId);
        return ResponseEntity.ok(toResponse(item));
    }

    @PostMapping
    public ResponseEntity<ItemResponse> createItem(@PathVariable Integer userId,
                                                   @PathVariable Integer eventId,
                                                   @Valid @RequestBody CreateItemRequest request) {
        Item saved = itemService.createItem(userId, eventId, request);
        return ResponseEntity.status(201).body(toResponse(saved));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable Integer userId,
                                                   @PathVariable Integer eventId,
                                                   @PathVariable Integer itemId,
                                                   @Valid @RequestBody UpdateItemRequest request) {
        Item saved = itemService.updateItem(userId, eventId, itemId, request);
        return ResponseEntity.ok(toResponse(saved));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<MessageResponse> deleteItem(@PathVariable Integer userId,
                                                      @PathVariable Integer eventId,
                                                      @PathVariable Integer itemId) {
        itemService.deleteItem(userId, eventId, itemId);
        return ResponseEntity.ok(new MessageResponse("Item deleted"));
    }
}

