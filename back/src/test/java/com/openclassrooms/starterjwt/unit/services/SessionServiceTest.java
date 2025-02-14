package com.openclassrooms.starterjwt.unit.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Enables Mockito support for JUnit 5
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository; // Mock for SessionRepository

    @Mock
    private UserRepository userRepository; // Mock for UserRepository

    @InjectMocks
    private SessionService sessionService; // Injects the mocks into SessionService

    private Session session;
    private User user;

    @BeforeEach
    public void setUp() {
        // Initialize test objects
        session = new Session();
        session.setId(1L);
        session.setName("Yoga Session");
        session.setUsers(new ArrayList<>()); // Initialize user list

        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
    }

    @Test
    public void testCreate() {
        // Arrange: Mock repository behavior
        when(sessionRepository.save(session)).thenReturn(session);

        // Act: Call the service method
        Session createdSession = sessionService.create(session);

        // Assert: Check if the session is created correctly
        assertNotNull(createdSession);
        assertEquals(session.getName(), createdSession.getName());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testDelete() {
        // Arrange: Mock repository behavior
        doNothing().when(sessionRepository).deleteById(1L);

        // Act: Call the delete method
        sessionService.delete(1L);

        // Assert: Verify that deleteById was called once
        verify(sessionRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindAll() {
        // Arrange: Mock repository response
        List<Session> sessions = new ArrayList<>();
        sessions.add(session);
        when(sessionRepository.findAll()).thenReturn(sessions);

        // Act: Call the service method
        List<Session> result = sessionService.findAll();

        // Assert: Verify that the list contains the expected session
        assertEquals(1, result.size());
        assertEquals(session.getName(), result.get(0).getName());
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    public void testGetById() {
        // Arrange: Mock repository response
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        // Act: Call the method
        Session result = sessionService.getById(1L);

        // Assert: Ensure the session was found
        assertNotNull(result);
        assertEquals(session.getName(), result.getName());
        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetById_NotFound() {
        // Arrange: Mock empty repository response
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act: Call the method
        Session result = sessionService.getById(1L);

        // Assert: Ensure null is returned when session is not found
        assertNull(result);
        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdate() {
        // Arrange: Create updated session object
        Session updatedSession = new Session();
        updatedSession.setId(1L);
        updatedSession.setName("Updated Yoga Session");

        // Mock repository behavior
        when(sessionRepository.save(updatedSession)).thenReturn(updatedSession);

        // Act: Call update method
        Session result = sessionService.update(1L, updatedSession);

        // Assert: Ensure the update is successful
        assertNotNull(result);
        assertEquals(updatedSession.getName(), result.getName());
        verify(sessionRepository, times(1)).save(updatedSession);
    }

    @Test
    public void testParticipate() {
        // Arrange: Mock session and user repository responses
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sessionRepository.save(session)).thenReturn(session);

        // Act: User participates in the session
        sessionService.participate(1L, 1L);

        // Assert: Ensure the user is added to the session
        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testParticipate_SessionNotFound() {
        // Arrange: Mock empty session response
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert: Expect NotFoundException
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
        verify(sessionRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(sessionRepository, never()).save(any());
    }

    @Test
    public void testParticipate_UserNotFound() {
        // Arrange: Mock session exists, but user does not
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert: Expect NotFoundException
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
        verify(sessionRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(sessionRepository, never()).save(any());
    }

    @Test
    public void testParticipate_AlreadyParticipating() {
        // Arrange: User already participating
        session.getUsers().add(user);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act & Assert: Expect BadRequestException
        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 1L));
        verify(sessionRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(sessionRepository, never()).save(any());
    }

    @Test
    public void testNoLongerParticipate() {
        // Arrange: User is currently participating
        session.getUsers().add(user);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(session)).thenReturn(session);

        // Act: Remove user from session
        sessionService.noLongerParticipate(1L, 1L);

        // Assert: Ensure user is removed from session
        assertFalse(session.getUsers().contains(user));
        verify(sessionRepository, times(1)).findById(1L);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testNoLongerParticipate_SessionNotFound() {
        // Arrange: Mock empty session response
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert: Expect NotFoundException
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 1L));
        verify(sessionRepository, times(1)).findById(1L);
        verify(sessionRepository, never()).save(any());
    }

    @Test
    public void testNoLongerParticipate_NotParticipating() {
        // Arrange: User is not in the session
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        // Act & Assert: Expect BadRequestException
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 1L));
        verify(sessionRepository, times(1)).findById(1L);
        verify(sessionRepository, never()).save(any());
    }
}
