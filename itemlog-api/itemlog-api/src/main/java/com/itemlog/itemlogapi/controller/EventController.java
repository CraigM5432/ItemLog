package com.itemlog.itemlogapi.controller;

import com.itemlog.itemlogapi.dto.CreateEventRequest;
import com.itemlog.itemlogapi.dto.EventResponse;
import com.itemlog.itemlogapi.dto.MessageResponse;
import com.itemlog.itemlogapi.dto.UpdateEventRequest;
import com.itemlog.itemlogapi.entity.Event;
import com.itemlog.itemlogapi.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
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

    @GetMapping
    public ResponseEntity<List<EventResponse>> listEvents(@PathVariable Integer userId) {
        List<EventResponse> result = eventService.listEvents(userId).stream()
                .map(EventController::toResponse)
                .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable Integer userId,
                                                  @PathVariable Integer eventId) {
        Event event = eventService.getEvent(userId, eventId);
        return ResponseEntity.ok(toResponse(event));
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@PathVariable Integer userId,
                                                     @Valid @RequestBody CreateEventRequest request) {
        Event saved = eventService.createEvent(userId, request);
        return ResponseEntity.status(201).body(toResponse(saved));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Integer userId,
                                                     @PathVariable Integer eventId,
                                                     @Valid @RequestBody UpdateEventRequest request) {
        Event saved = eventService.updateEvent(userId, eventId, request);
        return ResponseEntity.ok(toResponse(saved));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<MessageResponse> deleteEvent(@PathVariable Integer userId,
                                                       @PathVariable Integer eventId) {
        eventService.deleteEvent(userId, eventId);
        return ResponseEntity.ok(new MessageResponse("Event deleted"));
    }
}

