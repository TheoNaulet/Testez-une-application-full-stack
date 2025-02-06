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

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private User user;

    @BeforeEach
    public void setUp() {
        // Initialisation des objets pour les tests
        session = new Session();
        session.setId(1L);
        session.setName("Yoga Session");
        session.setUsers(new ArrayList<>());

        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
    }

    @Test
    public void testCreate() {
        // Arrange
        when(sessionRepository.save(session)).thenReturn(session);

        // Act
        Session createdSession = sessionService.create(session);

        // Assert
        assertNotNull(createdSession);
        assertEquals(session.getName(), createdSession.getName());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testDelete() {
        // Arrange
        doNothing().when(sessionRepository).deleteById(1L);

        // Act
        sessionService.delete(1L);

        // Assert
        verify(sessionRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindAll() {
        // Arrange
        List<Session> sessions = new ArrayList<>();
        sessions.add(session);
        when(sessionRepository.findAll()).thenReturn(sessions);

        // Act
        List<Session> result = sessionService.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(session.getName(), result.get(0).getName());
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    public void testGetById() {
        // Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        // Act
        Session result = sessionService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(session.getName(), result.getName());
        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetById_NotFound() {
        // Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Session result = sessionService.getById(1L);

        // Assert
        assertNull(result);
        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdate() {
        // Arrange
        Session updatedSession = new Session();
        updatedSession.setId(1L);
        updatedSession.setName("Updated Yoga Session");

        when(sessionRepository.save(updatedSession)).thenReturn(updatedSession);

        // Act
        Session result = sessionService.update(1L, updatedSession);

        // Assert
        assertNotNull(result);
        assertEquals(updatedSession.getName(), result.getName());
        verify(sessionRepository, times(1)).save(updatedSession);
    }

    @Test
    public void testParticipate() {
        // Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sessionRepository.save(session)).thenReturn(session);

        // Act
        sessionService.participate(1L, 1L);

        // Assert
        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testParticipate_SessionNotFound() {
        // Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
        verify(sessionRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L); 
        verify(sessionRepository, never()).save(any());
    }

    @Test
    public void testParticipate_UserNotFound() {
        // Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
        verify(sessionRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(sessionRepository, never()).save(any());
    }

    @Test
    public void testParticipate_AlreadyParticipating() {
        // Arrange
        session.getUsers().add(user); // L'utilisateur participe déjà
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 1L));
        verify(sessionRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(sessionRepository, never()).save(any());
    }

    @Test
    public void testNoLongerParticipate() {
        // Arrange
        session.getUsers().add(user); // L'utilisateur participe
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(session)).thenReturn(session);

        // Act
        sessionService.noLongerParticipate(1L, 1L);

        // Assert
        assertFalse(session.getUsers().contains(user));
        verify(sessionRepository, times(1)).findById(1L);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testNoLongerParticipate_SessionNotFound() {
        // Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 1L));
        verify(sessionRepository, times(1)).findById(1L);
        verify(sessionRepository, never()).save(any());
    }

    @Test
    public void testNoLongerParticipate_NotParticipating() {
        // Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 1L));
        verify(sessionRepository, times(1)).findById(1L);
        verify(sessionRepository, never()).save(any());
    }
}
