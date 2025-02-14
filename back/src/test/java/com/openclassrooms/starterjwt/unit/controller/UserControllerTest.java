package com.openclassrooms.starterjwt.unit.controller;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService; // Mocking the UserService to simulate service layer interactions without calling the actual implementation.

    @Mock
    private UserMapper userMapper; // Mocking the UserMapper to simulate mapping operations without needing actual DTO transformations.

    @InjectMocks
    private UserController userController;

    private User user;
    private final Long userId = 1L;
    private final String userEmail = "test@example.com";

    @BeforeEach
    public void setUp() { // This method is executed before each test to initialize a User instance with predefined values.
        user = new User();
        user.setId(userId);
        user.setEmail(userEmail);
    }

    @Test
    public void testFindById_UserFound() { // This test verifies that the controller correctly handles a case where a user is found in the system.
        // Arrange
        when(userService.findById(userId)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(new com.openclassrooms.starterjwt.dto.UserDto());

        // Act
        ResponseEntity<?> response = userController.findById(userId.toString());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).findById(userId);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    public void testFindById_UserNotFound() { // This test checks that the controller returns a NOT_FOUND response when the user is not found in the system.
        // Arrange
        when(userService.findById(userId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.findById(userId.toString());

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).findById(userId);
        verify(userMapper, never()).toDto((User) any());
    }

    @Test
    public void testFindById_InvalidIdFormat() { // This test ensures that the controller properly handles cases where an invalid user ID format is provided.
        // Act
        ResponseEntity<?> response = userController.findById("invalidId");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, never()).findById(any());
        verify(userMapper, never()).toDto((User) any());
    }

    @Test
    public void testDelete_UserFoundAndAuthorized() { // This test verifies that a user with proper authorization can successfully delete their account.
        // Arrange
        when(userService.findById(userId)).thenReturn(user);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(userEmail);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Act
        ResponseEntity<?> response = userController.save(userId.toString());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).findById(userId);
        verify(userService, times(1)).delete(userId);
    }

    @Test
    public void testDelete_UserFoundButUnauthorized() { // This test checks that an unauthorized user cannot delete another user's account.
        // Arrange
        when(userService.findById(userId)).thenReturn(user);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("another@example.com");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Act
        ResponseEntity<?> response = userController.save(userId.toString());

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userService, times(1)).findById(userId);
        verify(userService, never()).delete(userId);
    }

    @Test
    public void testDelete_UserNotFound() { // This test ensures that the controller returns a NOT_FOUND response when trying to delete a non-existing user.
        // Arrange
        when(userService.findById(userId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.save(userId.toString());

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).findById(userId);
        verify(userService, never()).delete(userId);
    }

    @Test
    public void testDelete_InvalidIdFormat() { // This test verifies that the controller returns a BAD_REQUEST response when an invalid ID format is provided for deletion.
        // Act
        ResponseEntity<?> response = userController.save("invalidId");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, never()).findById(any());
        verify(userService, never()).delete(any());
    }
}
