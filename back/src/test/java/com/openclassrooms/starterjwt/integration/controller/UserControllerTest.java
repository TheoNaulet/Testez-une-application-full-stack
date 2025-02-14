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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // Loads the full application context for integration testing
@AutoConfigureMockMvc // Automatically configures MockMvc for HTTP request testing
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc to simulate HTTP requests

    @Autowired
    private WebApplicationContext webContext; // Provides access to the web application context

    @Autowired
    private AuthenticationManager authManager; // Authentication manager for user authentication

    @Autowired
    private JwtUtils jwtUtil; // Utility class to generate JWT tokens

    @BeforeEach
    public void init() {
        // Initialize MockMvc with the web application context
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
    }

    @Test
    @DisplayName("findById endpoint should return HTTP 200 OK")
    public void shouldReturnUserDetailsWhenUserExists() throws Exception {
        // Arrange: Define user credentials
        String userEmail = "email@email.com";
        String userPassword = "password";

        // Authenticate the user
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(userEmail, userPassword));
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Generate JWT token for the authenticated user
        String jwtToken = jwtUtil.generateJwtToken(auth);

        // Act: Perform an HTTP GET request to retrieve user details by ID
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken) // Attach JWT token in the request header
                        .contentType(APPLICATION_JSON))
                // Assert: Expect HTTP 200 OK response
                .andExpect(status().isOk())
                // Verify that the returned user's last name is "Admin"
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Admin"))
                .andReturn();
    }
}
