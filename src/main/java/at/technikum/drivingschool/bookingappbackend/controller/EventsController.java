package at.technikum.drivingschool.bookingappbackend.controller;

import at.technikum.drivingschool.bookingappbackend.dto.request.UpdateEventRequest;
import at.technikum.drivingschool.bookingappbackend.model.EEventStatus;
import at.technikum.drivingschool.bookingappbackend.model.EEventType;
import at.technikum.drivingschool.bookingappbackend.model.Event;
import at.technikum.drivingschool.bookingappbackend.dto.request.EventRequest;
import at.technikum.drivingschool.bookingappbackend.dto.response.EventListResponse;
import at.technikum.drivingschool.bookingappbackend.repository.EventRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class EventsController {

    @Autowired
    EventRepository eventRepository;
    public EventsController() {
    }

    /**
     * Retrieve all saved events
     * @return List<Event>
     */
    @GetMapping("/events")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN') or hasRole('STUDENT')")
    public ResponseEntity<?> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return ResponseEntity.ok().body(new EventListResponse(events));
    }

    /**
     * Retrieve one event by id
     * Instructor or Admin can get a single event
     * @return List<Event>
     */
    @GetMapping("/event")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getEvent(@RequestParam("eventId") Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if(event.isPresent()) {
            return ResponseEntity.ok().body(event);
        }

        return ResponseEntity.status(404).body("{\"message\":\"Event not Found\"}");
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
        eventRepository.save(
                new Event(
                        event.getTitle(),
                        EEventType.valueOf(event.getEventType()),
                        EEventStatus.valueOf(event.getEventStatus()),
                        event.getPrice(),
                        event.getStartDate()
                        ));
        return ResponseEntity.ok().body("{\"message\":\"Event saved successfully\"}");
    }

    /**
     * Create a new Event in the Database
     * Instructor or Admin can create a new event
     * @param event
     * @return
     */
    @PutMapping("/events")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateEvent(@Valid @RequestBody UpdateEventRequest event) {
        eventRepository.save(
                new Event(
                        event.getId(),
                        event.getTitle(),
                        EEventType.valueOf(event.getEventType()),
                        EEventStatus.valueOf(event.getEventStatus()),
                        event.getPrice(),
                        event.getStartDate()
                ));
        return ResponseEntity.ok().body("{\"message\":\"Event updated successfully\"}");
    }

    /**
     * Delete Event in the Database
     * Instructor or Admin can delete the event
     * @param eventId
     * @return
     */
    @DeleteMapping("/events")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteEvent(@RequestParam("eventId") Long eventId) {
        eventRepository.deleteById(eventId);
        return ResponseEntity.ok().body("{\"message\":\"Event successfully deleted\"}");
    }


}
