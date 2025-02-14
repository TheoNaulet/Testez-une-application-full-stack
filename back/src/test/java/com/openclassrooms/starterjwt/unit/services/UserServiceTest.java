package com.openclassrooms.starterjwt.unit.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Enables Mockito support for JUnit 5
public class UserServiceTest {

    @Mock
    private UserRepository userRepository; // Mock the UserRepository to avoid real database interactions

    @InjectMocks
    private UserService userService; // Inject the mocked repository into the UserService

    private User user;

    @BeforeEach
    public void setUp() {
        // Initialize a User object before each test
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
    }

    @Test
    public void testDelete() {
        // Arrange: Simulate the deleteById method to do nothing
        doNothing().when(userRepository).deleteById(1L);

        // Act: Call the delete method from UserService
        userService.delete(1L);

        // Assert: Verify that deleteById was called exactly once
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindById() {
        // Arrange: Simulate a successful findById operation returning the user
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act: Call the findById method
        User foundUser = userService.findById(1L);

        // Assert: Verify that the user is found and matches expected values
        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        // Arrange: Simulate a scenario where findById returns an empty optional
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act: Attempt to find the user
        User foundUser = userService.findById(1L);

        // Assert: Verify that no user is found (null result)
        assertNull(foundUser);
        verify(userRepository, times(1)).findById(1L);
    }
}
