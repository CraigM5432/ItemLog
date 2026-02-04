package com.itemlog.itemlogapi.service;

import com.itemlog.itemlogapi.dto.CreateEventRequest;
import com.itemlog.itemlogapi.dto.UpdateEventRequest;
import com.itemlog.itemlogapi.entity.Event;
import com.itemlog.itemlogapi.entity.User;
import com.itemlog.itemlogapi.exception.NotFoundException;
import com.itemlog.itemlogapi.repository.EventRepository;
import com.itemlog.itemlogapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class EventService {

    private final UserRepository userRepo;
    private final EventRepository eventRepo;

    public EventService(UserRepository userRepo, EventRepository eventRepo) {
        this.userRepo = userRepo;
        this.eventRepo = eventRepo;
    }

    public List<Event> listEvents(Integer userId) {
        ensureUserExists(userId);
        return eventRepo.findByUser_UserId(userId);
    }

    public Event getEvent(Integer userId, Integer eventId) {
        ensureUserExists(userId);
        return eventRepo.findByEventIdAndUser_UserId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found for this user."));
    }

    public Event createEvent(Integer userId, CreateEventRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));

        LocalDate date = parseDateStrict(request.getEventDate());

        Event event = new Event();
        event.setUser(user);
        event.setEventName(request.getEventName().trim());
        event.setEventDate(date);

        return eventRepo.save(event);
    }

    public Event updateEvent(Integer userId, Integer eventId, UpdateEventRequest request) {
        Event event = getEvent(userId, eventId); // includes user exists + ownership check

        if (request.getEventName() != null && !request.getEventName().isBlank()) {
            event.setEventName(request.getEventName().trim());
        }

        if (request.getEventDate() != null && !request.getEventDate().isBlank()) {
            event.setEventDate(parseDateStrict(request.getEventDate()));
        }

        return eventRepo.save(event);
    }

    public void deleteEvent(Integer userId, Integer eventId) {
        Event event = getEvent(userId, eventId); // includes user exists + ownership check
        eventRepo.delete(event);
    }

    private void ensureUserExists(Integer userId) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException("User not found.");
        }
    }

    private LocalDate parseDateStrict(String dateStr) {
        try {
            return LocalDate.parse(dateStr.trim()); // ISO YYYY-MM-DD
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("eventDate must be in YYYY-MM-DD format.");
        }
    }
}

