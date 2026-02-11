package com.itemlog.itemlogapi.controller;

import com.itemlog.itemlogapi.dto.CreateEventRequest;
import com.itemlog.itemlogapi.dto.EventResponse;
import com.itemlog.itemlogapi.dto.UpdateEventRequest;
import com.itemlog.itemlogapi.entity.Event;
import com.itemlog.itemlogapi.entity.User;
import com.itemlog.itemlogapi.exception.NotFoundException;
import com.itemlog.itemlogapi.repository.EventRepository;
import com.itemlog.itemlogapi.repository.UserRepository;
import com.itemlog.itemlogapi.security.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventControllerV2 {

    private final UserRepository userRepo;
    private final EventRepository eventRepo;

    public EventControllerV2(UserRepository userRepo, EventRepository eventRepo) {
        this.userRepo = userRepo;
        this.eventRepo = eventRepo;
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
    public ResponseEntity<List<EventResponse>> listMyEvents() {
        Integer userId = CurrentUser.id();
        if (userId == null) throw new NotFoundException("User not found.");

        List<EventResponse> result = eventRepo.findByUser_UserId(userId).stream()
                .map(EventControllerV2::toResponse)
                .toList();

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<EventResponse> createMyEvent(@Valid @RequestBody CreateEventRequest request) {
        Integer userId = CurrentUser.id();
        if (userId == null) throw new NotFoundException("User not found.");

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));

        Event event = new Event();
        event.setUser(user);
        event.setEventName(request.getEventName().trim());
        event.setEventDate(LocalDate.parse(request.getEventDate()));

        Event saved = eventRepo.save(event);
        return ResponseEntity.status(201).body(toResponse(saved));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getMyEvent(@PathVariable Integer eventId) {
        Integer userId = CurrentUser.id();
        if (userId == null) throw new NotFoundException("User not found.");

        Event event = eventRepo.findByEventIdAndUser_UserId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found."));

        return ResponseEntity.ok(toResponse(event));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponse> updateMyEvent(@PathVariable Integer eventId,
                                                       @Valid @RequestBody UpdateEventRequest request) {
        Integer userId = CurrentUser.id();
        if (userId == null) throw new NotFoundException("User not found.");

        Event event = eventRepo.findByEventIdAndUser_UserId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found."));

        if (request.getEventName() != null && !request.getEventName().isBlank()) {
            event.setEventName(request.getEventName().trim());
        }
        if (request.getEventDate() != null && !request.getEventDate().isBlank()) {
            event.setEventDate(LocalDate.parse(request.getEventDate()));
        }

        return ResponseEntity.ok(toResponse(eventRepo.save(event)));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> deleteMyEvent(@PathVariable Integer eventId) {
        Integer userId = CurrentUser.id();
        if (userId == null) throw new NotFoundException("User not found.");

        Event event = eventRepo.findByEventIdAndUser_UserId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found."));

        eventRepo.delete(event);
        return ResponseEntity.ok(java.util.Map.of("message", "Event deleted", "eventId", eventId));
    }
}
