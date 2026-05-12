package com.itemlog.itemlogapi.controller;

import com.itemlog.itemlogapi.dto.CreateEventRequest;
import com.itemlog.itemlogapi.dto.EventResponse;
import com.itemlog.itemlogapi.dto.UpdateEventRequest;
import com.itemlog.itemlogapi.entity.Event;
import com.itemlog.itemlogapi.exception.NotFoundException;
import com.itemlog.itemlogapi.security.CurrentUser;
import com.itemlog.itemlogapi.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Handles event endpoints for the authenticated user.
// Delegates business logic to EventService and gets user identity from the JWT context.
@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    private static EventResponse toResponse(Event e) {
        return new EventResponse(
                e.getEventId(),
                e.getEventName(),
                e.getEventDate(),
                e.getCreatedAt()
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
    public ResponseEntity<List<EventResponse>> listMyEvents() {
        Integer userId = currentUserId();

        List<EventResponse> result = eventService.listEvents(userId).stream()
                .map(EventController::toResponse)
                .toList();

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<EventResponse> createMyEvent(
            @Valid @RequestBody CreateEventRequest request
    ) {
        Integer userId = currentUserId();

        Event saved = eventService.createEvent(userId, request);

        return ResponseEntity.status(201).body(toResponse(saved));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getMyEvent(@PathVariable Integer eventId) {
        Integer userId = currentUserId();

        Event event = eventService.getEvent(userId, eventId);

        return ResponseEntity.ok(toResponse(event));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponse> updateMyEvent(
            @PathVariable Integer eventId,
            @Valid @RequestBody UpdateEventRequest request
    ) {
        Integer userId = currentUserId();

        Event updated = eventService.updateEvent(userId, eventId, request);

        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> deleteMyEvent(@PathVariable Integer eventId) {
        Integer userId = currentUserId();

        eventService.deleteEvent(userId, eventId);

        return ResponseEntity.ok(
                java.util.Map.of(
                        "message", "Event deleted",
                        "eventId", eventId
                )
        );
    }
}
