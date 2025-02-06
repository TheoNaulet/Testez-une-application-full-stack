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

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        // Réinitialiser le contexte de sécurité avant chaque test
        SecurityContextHolder.clearContext();
    }

    // Test pour la méthode authenticateUser
    @Test
    public void testAuthenticateUser_Success() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("Mypassword8$");

        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L, "yoga@studio.com", "John", "Doe", false, "password");

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtUtils.generateJwtToken(authentication)).thenReturn("fake-jwt-token");

        User user = new User();
        user.setAdmin(false);
        when(userRepository.findByEmail("yoga@studio.com")).thenReturn(java.util.Optional.of(user));

        // When
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals("yoga@studio.com", jwtResponse.getUsername());
        assertEquals("fake-jwt-token", jwtResponse.getToken());
        assertEquals(false, jwtResponse.getAdmin());
    }

    // Test pour la méthode registerUser (cas réussi)
    @Test
    public void testRegisterUser_Success() {
        // Given
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("newuser@studio.com");
        signUpRequest.setFirstName("New");
        signUpRequest.setLastName("User");
        signUpRequest.setPassword("password");

        when(userRepository.existsByEmail("newuser@studio.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded-password");

        // When
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully!", ((MessageResponse) response.getBody()).getMessage());
    }

    // Test pour la méthode registerUser (cas d'échec : email déjà existant)
    @Test
    public void testRegisterUser_EmailAlreadyExists() {
        // Given
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("existing@studio.com");
        signUpRequest.setFirstName("Existing");
        signUpRequest.setLastName("User");
        signUpRequest.setPassword("password");

        when(userRepository.existsByEmail("existing@studio.com")).thenReturn(true);

        // When
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Email is already taken!", ((MessageResponse) response.getBody()).getMessage());
    }
}