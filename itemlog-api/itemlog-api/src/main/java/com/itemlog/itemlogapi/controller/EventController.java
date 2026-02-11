package com.itemlog.itemlogapi.controller;

import com.itemlog.itemlogapi.dto.UpdateEventRequest;
import com.itemlog.itemlogapi.dto.EventResponse;
import com.itemlog.itemlogapi.dto.ErrorResponse;
import com.itemlog.itemlogapi.entity.Event;
import com.itemlog.itemlogapi.entity.User;
import com.itemlog.itemlogapi.repository.EventRepository;
import com.itemlog.itemlogapi.repository.UserRepository;
import com.itemlog.itemlogapi.security.TokenGuards;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * DEPRECATED: Use V2 endpoints: /events
 */
@Deprecated
@RestController
@RequestMapping("/users/{userId}/events")
public class EventController {

    private final UserRepository userRepo;
    private final EventRepository eventRepo;

    public EventController(UserRepository userRepo, EventRepository eventRepo) {
        this.userRepo = userRepo;
        this.eventRepo = eventRepo;
    }

    private static ResponseEntity<ErrorResponse> notFound(String msg) {
        return ResponseEntity.status(404).body(new ErrorResponse(msg));
    }

    private static ResponseEntity<ErrorResponse> badRequest(String msg) {
        return ResponseEntity.badRequest().body(new ErrorResponse(msg));
    }

    private static LocalDate parseDateOrNull(String s) {
        if (s == null || s.isBlank()) return null;
        return LocalDate.parse(s.trim()); // expects YYYY-MM-DD
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
    public ResponseEntity<?> listEvents(@PathVariable Integer userId) {
        TokenGuards.requirePathUserMatchesToken(userId);

        if (!userRepo.existsById(userId)) {
            return notFound("User not found.");
        }

        List<EventResponse> result = eventRepo.findByUser_UserId(userId).stream()
                .map(EventController::toResponse)
                .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEvent(@PathVariable Integer userId, @PathVariable Integer eventId) {
        TokenGuards.requirePathUserMatchesToken(userId);

        if (!userRepo.existsById(userId)) {
            return notFound("User not found.");
        }

        Event event = eventRepo.findByEventIdAndUser_UserId(eventId, userId).orElse(null);
        if (event == null) {
            return notFound("Event not found for this user.");
        }

        return ResponseEntity.ok(toResponse(event));
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@PathVariable Integer userId,
                                         @RequestBody UpdateEventRequest request) {
        TokenGuards.requirePathUserMatchesToken(userId);

        if (request.getEventName() == null || request.getEventName().isBlank()
                || request.getEventDate() == null || request.getEventDate().isBlank()) {
            return badRequest("eventName and eventDate are required.");
        }

        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            return notFound("User not found.");
        }

        LocalDate date;
        try {
            date = parseDateOrNull(request.getEventDate());
            if (date == null) return badRequest("eventDate is required.");
        } catch (DateTimeParseException ex) {
            return badRequest("eventDate must be YYYY-MM-DD.");
        }

        Event event = new Event();
        event.setUser(user);
        event.setEventName(request.getEventName().trim());
        event.setEventDate(date);

        Event saved = eventRepo.save(event);

        return ResponseEntity.status(201).body(toResponse(saved));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<?> updateEvent(@PathVariable Integer userId,
                                         @PathVariable Integer eventId,
                                         @RequestBody UpdateEventRequest request) {
        TokenGuards.requirePathUserMatchesToken(userId);

        if (!userRepo.existsById(userId)) {
            return notFound("User not found.");
        }

        Event event = eventRepo.findByEventIdAndUser_UserId(eventId, userId).orElse(null);
        if (event == null) {
            return notFound("Event not found for this user.");
        }

        if (request.getEventName() != null && !request.getEventName().isBlank()) {
            event.setEventName(request.getEventName().trim());
        }

        if (request.getEventDate() != null && !request.getEventDate().isBlank()) {
            try {
                event.setEventDate(parseDateOrNull(request.getEventDate()));
            } catch (DateTimeParseException ex) {
                return badRequest("eventDate must be YYYY-MM-DD.");
            }
        }

        Event saved = eventRepo.save(event);
        return ResponseEntity.ok(toResponse(saved));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable Integer userId,
                                         @PathVariable Integer eventId) {
        TokenGuards.requirePathUserMatchesToken(userId);

        if (!userRepo.existsById(userId)) {
            return notFound("User not found.");
        }

        Event event = eventRepo.findByEventIdAndUser_UserId(eventId, userId).orElse(null);
        if (event == null) {
            return notFound("Event not found for this user.");
        }

        eventRepo.delete(event);
        return ResponseEntity.ok(new java.util.LinkedHashMap<>() {{
            put("message", "Event deleted");
            put("eventId", eventId);
        }});
    }
}


