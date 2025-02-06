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
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private User user;
    private final Long userId = 1L;
    private final String userEmail = "test@example.com";

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(userId);
        user.setEmail(userEmail);
    }

    @Test
    public void testFindById_UserFound() {
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
    public void testFindById_UserNotFound() {
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
    public void testFindById_InvalidIdFormat() {
        // Act
        ResponseEntity<?> response = userController.findById("invalidId");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, never()).findById(any());
        verify(userMapper, never()).toDto((User) any());
    }

    @Test
    public void testDelete_UserFoundAndAuthorized() {
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
    public void testDelete_UserFoundButUnauthorized() {
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
    public void testDelete_UserNotFound() {
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
    public void testDelete_InvalidIdFormat() {
        // Act
        ResponseEntity<?> response = userController.save("invalidId");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, never()).findById(any());
        verify(userService, never()).delete(any());
    }
}