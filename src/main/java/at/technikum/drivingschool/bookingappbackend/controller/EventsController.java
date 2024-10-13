package at.technikum.drivingschool.bookingappbackend.controller;

import at.technikum.drivingschool.bookingappbackend.dto.request.EventRequest;
import at.technikum.drivingschool.bookingappbackend.dto.request.UpdateEventRequest;
import at.technikum.drivingschool.bookingappbackend.dto.response.EventListResponse;
import at.technikum.drivingschool.bookingappbackend.exception.EventNotFoundException;
import at.technikum.drivingschool.bookingappbackend.model.Event;
import at.technikum.drivingschool.bookingappbackend.service.EventsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api")
public class EventsController {

    @Autowired
    EventsService eventsService;

    public EventsController() {
    }

    /**
     * Retrieve all saved events
     * @return List<Event>
     */
    @GetMapping("/events")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN') or hasRole('STUDENT')")
    public ResponseEntity<?> getAllEvents() {
        return ResponseEntity.ok().body(new EventListResponse(eventsService.getAllEvents()));
    }

    /**
     * Retrieve one event by id
     * Instructor or Admin can get a single event
     * @return List<Event>
     */
    @GetMapping("/events/{eventId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getEvent(@PathVariable("eventId") Long eventId) {
        Event event = eventsService.getEvent(eventId).orElseThrow(() -> new EventNotFoundException("Event not Found with id: " + eventId));

        return ResponseEntity.ok().body(event);
    }

    /**
     * Create a new Event in the Database
     * Instructor or Admin can create a new event
     * @param event
     * @return
     */
    @PostMapping("/events")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventRequest event) {
        eventsService.createEvent(
                event.getTitle(),
                event.getEventType(),
                event.getEventStatus(),
                event.getPrice(),
                event.getStartDate()
        );
        return ResponseEntity.ok().body("{\"message\":\"Event saved successfully\"}");
    }

    /**
     * Create a new Event in the Database
     * Instructor or Admin can create a new event
     * @param event
     * @return
     */
    @PutMapping("/events/{eventId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateEvent(@PathVariable("eventId") Long eventId, @Valid @RequestBody UpdateEventRequest event) {
        eventsService.updateEvent(
                eventId,
                event.getTitle(),
                event.getEventType(),
                event.getEventStatus(),
                event.getPrice(),
                event.getStartDate()
        );
        return ResponseEntity.ok().body("{\"message\":\"Event updated successfully\"}");
    }

    /**
     * Delete Event in the Database
     * Instructor or Admin can delete the event
     * @param eventId
     * @return
     */
    @DeleteMapping("/events/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteEvent(@PathVariable("eventId") Long eventId) {
        eventsService.deleteEvent(eventId);
        return ResponseEntity.ok().body("{\"message\":\"Event successfully deleted\"}");
    }


}
