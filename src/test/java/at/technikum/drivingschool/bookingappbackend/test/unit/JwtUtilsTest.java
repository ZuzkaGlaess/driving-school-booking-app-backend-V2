package at.technikum.drivingschool.bookingappbackend.test.unit;

import at.technikum.drivingschool.bookingappbackend.security.jwt.JwtUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;

@ContextConfiguration
@TestPropertySource("classpath:application-test.properties")
public class JwtUtilsTest {

    String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBZG1pbiIsImlhdCI6MTcyODc1NjUwMiwiZXhwIjoxNzI4ODQyOTAyfQ.adm44n0ax4kjuh_qgdfXXW1oVwMSU4iRNIJKWLJYnMc";
    String jwtToken2 = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJJbnN0cnVjdG9yIiwiaWF0IjoxNzI4NzU4NjI0LCJleHAiOjE3Mjg4NDUwMjR9.CeVWjLqe3z4vfe6KEjexnbR4YfwBgQ9ReNNuhOG_ovs";
    JwtUtils jwtUtils = new JwtUtils();
    @Before
    public void setup(){
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "2D4A614E645267556B58703273357638792F423F4428472B4B6250655368566D");
    }

    @Test
    public void extractUsernameFromTokenTest_equals_Admin() {
        String userName = jwtUtils.getUserNameFromJwtToken(jwtToken);
        assertEquals("Admin", userName);
    }

    @Test
    public void extractUsernameFromTokenTest_not_equals_Admin() {
        String userName = jwtUtils.getUserNameFromJwtToken(jwtToken2);
        assertNotEquals("Admin", userName);
        assertEquals("Instructor", userName);
    }

    @Test
    public void validateJWTToken() {
        boolean result = jwtUtils.validateJwtToken(jwtToken);
        assertEquals(true, result);
    }

    @Test
    public void validateJWTToken_False() {
        boolean result = jwtUtils.validateJwtToken("abcdefg");
        assertEquals(false, result);
    }

    @Test
    public void generateTokenFromUsernameTest() {
        String token = jwtUtils.generateTokenFromUsername("Admin");
        assertNotNull(token);
    }
}
