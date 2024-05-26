package grid.capstone.service.jwt;

import io.jsonwebtoken.JwtParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private JwtParser jwtParser;

    private final String testUsername = "testuser";

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testGenerateToken() {
        // Mock user details
        UserDetails userDetails = new User(testUsername, "testpassword", new ArrayList<>());

        // Generate token
        String token = jwtService.generateToken(userDetails);

        // Verify that the token is not empty
        assertNotNull(token);

        // Verify that the token contains the correct subject (username)
        assertEquals(testUsername, jwtService.extractUsername(token));
    }

    @Test
    public void testIsTokenValid() {
        // Generate a valid token with 1-hour expiration
        String validToken = jwtService.generateToken(new HashMap<>(), new User(testUsername, "testpassword", new ArrayList<>()));


        // Validate the valid token (should return true)
        assertTrue(jwtService.isTokenValid(validToken));

    }


    @Test
    public void testExtractUsername() {
        // Generate a token with a subject (username)
        String token = jwtService.generateToken(new HashMap<>(), new User(testUsername, "testpassword", new ArrayList<>()));

        // Extract the username from the token
        String extractedUsername = jwtService.extractUsername(token);

        // Verify that the extracted username is the same as the original one
        assertEquals(testUsername, extractedUsername);
    }

    @Test
    public void testExtractClaim() {
        // Generate a valid token with some claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 123);
        String token = jwtService.generateToken(claims,  new User(testUsername, "testpassword", new ArrayList<>()));

        // Extract the 'userId' claim from the token
        int userId = jwtService.extractClaim(token, claimsMethod -> claimsMethod.get("userId", Integer.class));

        // Check if the extracted claim is as expected
        assertThat(userId).isEqualTo(123);
    }

}