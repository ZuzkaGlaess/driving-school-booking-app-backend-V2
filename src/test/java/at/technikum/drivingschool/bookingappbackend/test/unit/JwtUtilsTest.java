package at.technikum.drivingschool.bookingappbackend.test.unit;

import at.technikum.drivingschool.bookingappbackend.security.jwt.JwtUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;

@ContextConfiguration
@TestPropertySource("classpath:application-test.properties")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JwtUtilsTest {

    static String jwtToken;
    JwtUtils jwtUtils = new JwtUtils();
    @Before
    public void setup(){
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "2D4A614E645267556B58703273357638792F423F4428472B4B6250655368566D");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 86400000);
    }

    @Test
    public void a_generateTokenFromUsernameTest() {
        String token = jwtUtils.generateTokenFromUsername("Admin");
        jwtToken = token;
        assertNotNull(token);
    }

    @Test
    public void b_validateJWTToken() {
        boolean result = jwtUtils.validateJwtToken(jwtToken);
        assertEquals(true, result);
    }

    @Test
    public void c_extractUsernameFromTokenTest_equals_Admin() {
        String userName = jwtUtils.getUserNameFromJwtToken(jwtToken);
        assertEquals("Admin", userName);
    }

    @Test
    public void z_validateJWTToken_False() {
        boolean result = jwtUtils.validateJwtToken("abcdefg");
        assertEquals(false, result);
    }
}
