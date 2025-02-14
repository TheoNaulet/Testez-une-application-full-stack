package com.openclassrooms.starterjwt.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.controllers.AuthController;

@ExtendWith(MockitoExtension.class) // Enables Mockito extension for unit testing
public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager; // Mocking the authentication manager

    @Mock
    private JwtUtils jwtUtils; // Mocking the JWT utility class

    @Mock
    private PasswordEncoder passwordEncoder; // Mocking the password encoder

    @Mock
    private UserRepository userRepository; // Mocking the user repository

    @InjectMocks
    private AuthController authController; // Injecting mocks into the AuthController

    @BeforeEach
    public void setUp() {
        // Clear the security context before each test to ensure test isolation
        SecurityContextHolder.clearContext();
    }

    // Test for the authenticateUser method (successful authentication)
    @Test
    public void testAuthenticateUser_Success() {
        // Given: Create a mock login request
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("Mypassword8$");

        // Create a mock user details object
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L, "yoga@studio.com", "John", "Doe", false, "password");

        // Mock authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Mock JWT token generation
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("fake-jwt-token");

        // Mock finding the user in the repository
        User user = new User();
        user.setAdmin(false);
        when(userRepository.findByEmail("yoga@studio.com")).thenReturn(java.util.Optional.of(user));

        // When: Call the authenticateUser method
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Then: Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals("yoga@studio.com", jwtResponse.getUsername());
        assertEquals("fake-jwt-token", jwtResponse.getToken());
        assertEquals(false, jwtResponse.getAdmin());
    }

    // Test for the registerUser method (successful registration)
    @Test
    public void testRegisterUser_Success() {
        // Given: Create a mock signup request
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("newuser@studio.com");
        signUpRequest.setFirstName("New");
        signUpRequest.setLastName("User");
        signUpRequest.setPassword("password");

        // Mock user repository behavior (email does not exist)
        when(userRepository.existsByEmail("newuser@studio.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded-password");

        // When: Call the registerUser method
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Then: Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully!", ((MessageResponse) response.getBody()).getMessage());
    }

    // Test for the registerUser method (failure case: email already exists)
    @Test
    public void testRegisterUser_EmailAlreadyExists() {
        // Given: Create a mock signup request with an existing email
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("existing@studio.com");
        signUpRequest.setFirstName("Existing");
        signUpRequest.setLastName("User");
        signUpRequest.setPassword("password");

        // Mock user repository behavior (email already exists)
        when(userRepository.existsByEmail("existing@studio.com")).thenReturn(true);

        // When: Call the registerUser method
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Then: Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Email is already taken!", ((MessageResponse) response.getBody()).getMessage());
    }
}
