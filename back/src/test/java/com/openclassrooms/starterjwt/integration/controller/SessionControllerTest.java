package com.openclassrooms.starterjwt.integration.controller;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Enables Mockito support in JUnit 5
public class SessionControllerTest {

    @Mock
    private SessionService sessionService; // Mocking SessionService

    @Mock
    private SessionMapper sessionMapper; // Mocking SessionMapper

    @InjectMocks
    private SessionController sessionController; // Injecting mocks into SessionController

    private Session session;
    private SessionDto sessionDto;

    @BeforeEach
    public void setUp() {
        // Initializing session and sessionDto before each test
        session = new Session();
        session.setId(1L);
        session.setName("Session 1");

        sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Session 1");
    }

    @Test
    public void testFindById() {
        // Arrange: Simulate finding a session by ID
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Act: Call findById method
        ResponseEntity<?> response = sessionController.findById("1");

        // Assert: Verify response status and data
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    public void testFindById_NotFound() {
        // Arrange: Simulate session not found
        when(sessionService.getById(1L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = sessionController.findById("1");

        // Assert: Expect HTTP 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFindById_BadRequest() {
        // Act: Call method with invalid ID format
        ResponseEntity<?> response = sessionController.findById("invalidId");

        // Assert: Expect HTTP 400 Bad Request
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testFindAll() {
        // Arrange: Simulate fetching all sessions
        List<Session> sessions = Arrays.asList(session);
        List<SessionDto> sessionDtos = Arrays.asList(sessionDto);

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        // Act
        ResponseEntity<?> response = sessionController.findAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDtos, response.getBody());
    }

    @Test
    public void testCreate() {
        // Arrange: Simulate session creation
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Act
        ResponseEntity<?> response = sessionController.create(sessionDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    public void testUpdate() {
        // Arrange: Simulate session update
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(1L, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Act
        ResponseEntity<?> response = sessionController.update("1", sessionDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    public void testUpdate_BadRequest() {
        // Act: Call update with an invalid ID
        ResponseEntity<?> response = sessionController.update("invalidId", sessionDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testDelete() {
        // Arrange: Simulate deleting a session
        when(sessionService.getById(1L)).thenReturn(session);

        // Act
        ResponseEntity<?> response = sessionController.save("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService, times(1)).delete(1L);
    }

    @Test
    public void testDelete_NotFound() {
        // Arrange: Simulate session not found
        when(sessionService.getById(1L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = sessionController.save("1");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDelete_BadRequest() {
        // Act: Call delete with invalid ID
        ResponseEntity<?> response = sessionController.save("invalidId");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testParticipate() {
        // Act: Call participate method
        ResponseEntity<?> response = sessionController.participate("1", "1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService, times(1)).participate(1L, 1L);
    }

    @Test
    public void testParticipate_BadRequest() {
        // Act: Call participate with an invalid ID
        ResponseEntity<?> response = sessionController.participate("invalidId", "1");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testNoLongerParticipate() {
        // Act: Call noLongerParticipate method
        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService, times(1)).noLongerParticipate(1L, 1L);
    }

    @Test
    public void testNoLongerParticipate_BadRequest() {
        // Act: Call noLongerParticipate with invalid ID
        ResponseEntity<?> response = sessionController.noLongerParticipate("invalidId", "1");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
