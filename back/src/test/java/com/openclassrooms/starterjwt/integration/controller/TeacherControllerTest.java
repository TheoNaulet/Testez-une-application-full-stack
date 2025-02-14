package com.openclassrooms.starterjwt.integration.controller;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // Loads the full Spring Boot application context for integration testing
@AutoConfigureMockMvc // Automatically configures MockMvc for HTTP request testing
public class TeacherControllerTest {
    
    @Autowired
    private MockMvc mockMvc; // MockMvc to simulate HTTP requests

    @Autowired
    private WebApplicationContext context; // Provides access to the web application context

    @Autowired
    private AuthenticationManager authenticationManager; // Handles authentication

    @Autowired
    private JwtUtils jwtUtils; // Utility class to generate JWT tokens

    @BeforeEach // This is the correct annotation instead of @Before("")
    public void setUp(){
        // Initialize MockMvc with the web application context
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("findById method, response entity OK")
    void testFindById_responseEntityOK() throws Exception {
        // Arrange: Define test user credentials
        String email = "email@email.com";
        String password = "password";

        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate a JWT token for authentication
        String token = jwtUtils.generateJwtToken(authentication);

        // Act: Perform an HTTP GET request to retrieve teacher details by ID
        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/teacher/{id}", "1") // Requesting teacher with ID 1
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token) // Attach JWT token in the request header
                        .contentType(APPLICATION_JSON))
                // Assert: Expect HTTP 200 OK response
                .andExpect(status().isOk())
                // Verify that the returned teacher's last name is "DELAHAYE"
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("DELAHAYE"))
                .andReturn();
    }

    @Test
    @DisplayName("findById method, response entity not found")
    void testFindById_responseEntityNotFound() throws Exception {
        // Arrange: Define test user credentials
        String email = "email@email.com";
        String password = "password";

        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate a JWT token for authentication
        String token = jwtUtils.generateJwtToken(authentication);

        // Act: Perform an HTTP GET request for a non-existing teacher ID
        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/teacher/{id}", "3") // Requesting teacher with ID 3 (assumed not to exist)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token) // Attach JWT token in the request header
                                .contentType(APPLICATION_JSON))
                // Assert: Expect HTTP 404 Not Found response
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
