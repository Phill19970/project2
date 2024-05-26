package grid.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import grid.capstone.dto.v1.AuthRequest;
import grid.capstone.filter.JwtTokenFilter;
import grid.capstone.service.auth.AuthService;
import grid.capstone.service.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    static final String BASE_URL = "/api/v1/auth";


    @MockBean
    JwtService jwtService;
    @MockBean
    JwtTokenFilter jwtTokenFilter;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthService authService;

    AuthRequest authRequest;

    @BeforeEach
    void setUp() {

        authRequest = AuthRequest.builder()
                .email("Email@gmail.com")
                .password("password")
                .build();

    }

    @Test
    void loginUser() throws Exception {

        String expectedToken = "ufbsfbsbf";

        given(authService.login(any(AuthRequest.class)))
                .willReturn(expectedToken);

        // Perform the POST request with the AuthRequest as the request body
        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)));


        resultActions
                .andExpect(status().isOk()) // Assert the expected HTTP status
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, "Bearer " + expectedToken)); // Assert the token is present in the response header

        // Verify that the authService.login() method was called once with the correct AuthRequest argument
        then(authService).should(times(1)).login(any(AuthRequest.class));

    }
}