package com.openclassrooms.starterjwt.unit.security;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Enables Mockito support for JUnit 5
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository; // Mock for UserRepository

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService; // Injects the mock repository into UserDetailsServiceImpl

    private User testUser;
    private final String testEmail = "test@example.com";
    private final String wrongEmail = "wrong@example.com";

    @BeforeEach
    void setUp() {
        // Initialize a test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail(testEmail);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setPassword("password123");
        testUser.setAdmin(false);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Load a user by email - Success")
    void whenUserExists_thenLoadUserByUsernameShouldReturnUserDetails() {
        // Arrange: Mock repository response to return the test user
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));

        // Act: Call the method to retrieve user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(testEmail);

        // Assert: Verify that the returned user details are correct
        assertNotNull(userDetails, "User details should not be null");
        assertEquals(testUser.getEmail(), userDetails.getUsername(), "User email should match");
        assertEquals(testUser.getPassword(), userDetails.getPassword(), "User password should match");

        // Verify that the repository method was called once
        verify(userRepository, times(1)).findByEmail(testEmail);
    }

    @Test
    @DisplayName("Load a user by email - User not found")
    void whenUserDoesNotExist_thenThrowUsernameNotFoundException() {
        // Arrange: Mock repository to return an empty result
        when(userRepository.findByEmail(wrongEmail)).thenReturn(Optional.empty());

        // Act & Assert: Verify that an exception is thrown when the user is not found
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(wrongEmail);
        }, "An exception should be thrown if the user does not exist");

        // Verify that the repository method was called once
        verify(userRepository, times(1)).findByEmail(wrongEmail);
    }
}
