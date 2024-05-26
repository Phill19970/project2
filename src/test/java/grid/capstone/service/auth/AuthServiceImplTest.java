package grid.capstone.service.auth;

import grid.capstone.dto.v1.AuthRequest;
import grid.capstone.service.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {


    AuthService authService;

    @Mock
    Authentication authentication;

    @Mock
    UserDetails userDetails;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(authenticationManager, userDetailsService, jwtService);
    }

    @Test
    public void testLogin_SuccessfulAuthentication_ShouldReturnJwtToken() {
        // Given
        AuthRequest authRequest = new AuthRequest("test@example.com", "password");

        String expectedToken = "validJwtToken";

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(authentication);

        given(userDetailsService.loadUserByUsername(authRequest.getEmail())).willReturn(userDetails);
        given(jwtService.generateToken(userDetails)).willReturn(expectedToken);

        // When
        String resultToken = authService.login(authRequest);

        // Then
        assertThat(resultToken).isEqualTo(expectedToken);
    }

    @Test
    public void testLogin_UnsuccessfulAuthentication_ShouldReturnNull() {
        // Given
        AuthRequest authRequest = new AuthRequest("test@example.com", "incorrectPassword");

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willThrow(new AuthenticationException("Authentication failed") {});


        assertThrows(AuthenticationException.class, () -> authService.login(authRequest));

    }

    @Test
    public void testLogin_JwtTokenGeneration_ShouldReturnValidJwtToken() {
        // Given
        AuthRequest authRequest = new AuthRequest("test@example.com", "password");

        String expectedToken = "validJwtToken";

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(authentication);

        given(userDetailsService.loadUserByUsername(authRequest.getEmail())).willReturn(userDetails);

        //TODO: test the actual method
        given(jwtService.generateToken(userDetails)).willReturn(expectedToken);

        // When
        String resultToken = authService.login(authRequest);

        // Then
        assertThat(resultToken).isEqualTo(expectedToken);
    }
}