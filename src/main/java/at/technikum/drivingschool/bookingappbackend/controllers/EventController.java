package at.technikum.drivingschool.bookingappbackend.controllers;

import at.technikum.drivingschool.bookingappbackend.models.Event;
import at.technikum.drivingschool.bookingappbackend.repository.EventRepository;
import at.technikum.drivingschool.bookingappbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    EventRepository eventRepository;
    public EventController() {
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return ResponseEntity.ok().body(events);
    }

    @GetMapping("/allForUser/{userId}")
    public ResponseEntity<?> getAllEventsForUser(@PathVariable("userId") String userId ) {
        // TODO
        return null;
    }

}
