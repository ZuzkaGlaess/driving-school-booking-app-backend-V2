package at.technikum.drivingschool.bookingappbackend.dto.request;

import at.technikum.drivingschool.bookingappbackend.model.EEventStatus;
import at.technikum.drivingschool.bookingappbackend.repository.EventRepository;
import org.junit.Assert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class EventRequestTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    void testGetRequest() {
        TestRestTemplate restTemplate = new TestRestTemplate();

        ResponseEntity<EEventStatus> responseEntity = restTemplate.getForEntity("http://localhost:8080/events", EEventStatus.class);

        Assert.assertNotNull(responseEntity.getBody());
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

    }

}