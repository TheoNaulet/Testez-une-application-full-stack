package com.openclassrooms.starterjwt.unit.controller;

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
        // Initialize Session and SessionDto objects before each test
        session = new Session();
        session.setId(1L);
        session.setName("Session 1");

        sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Session 1");
    }

    @Test
    public void testFindById_Success() {
        // Simulate an existing session
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Call the method under test
        ResponseEntity<?> response = sessionController.findById("1");

        // Verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    public void testFindById_NotFound() {
        // Simulate a non-existing session
        when(sessionService.getById(1L)).thenReturn(null);

        // Call the method under test
        ResponseEntity<?> response = sessionController.findById("1");

        // Verify that the response status is NOT_FOUND
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFindById_BadRequest() {
        // Call the method with an invalid ID
        ResponseEntity<?> response = sessionController.findById("invalid");

        // Verify that the response status is BAD_REQUEST
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testFindAll() {
        // Simulate a list of sessions
        List<Session> sessions = Arrays.asList(session);
        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(Arrays.asList(sessionDto));

        // Call the method under test
        ResponseEntity<?> response = sessionController.findAll();

        // Verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Arrays.asList(sessionDto), response.getBody());
    }

    @Test
    public void testCreate() {
        // Simulate creating a new session
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Call the method under test
        ResponseEntity<?> response = sessionController.create(sessionDto);

        // Verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    public void testUpdate_Success() {
        // Simulate updating an existing session
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(1L, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Call the method under test
        ResponseEntity<?> response = sessionController.update("1", sessionDto);

        // Verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    public void testUpdate_BadRequest() {
        // Call the method with an invalid ID
        ResponseEntity<?> response = sessionController.update("invalid", sessionDto);

        // Verify that the response status is BAD_REQUEST
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testDelete_Success() {
        // Simulate a session exists before deletion
        when(sessionService.getById(1L)).thenReturn(session);

        // Call the delete method
        ResponseEntity<?> response = sessionController.save("1");

        // Verify the response status and check if delete was called once
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService, times(1)).delete(1L);
    }

    @Test
    public void testDelete_NotFound() {
        // Simulate a session not found
        when(sessionService.getById(1L)).thenReturn(null);

        // Call the delete method
        ResponseEntity<?> response = sessionController.save("1");

        // Verify that the response status is NOT_FOUND
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDelete_BadRequest() {
        // Call the delete method with an invalid ID
        ResponseEntity<?> response = sessionController.save("invalid");

        // Verify that the response status is BAD_REQUEST
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testParticipate_Success() {
        // Call the participate method
        ResponseEntity<?> response = sessionController.participate("1", "1");

        // Verify the response status and check if participate was called once
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService, times(1)).participate(1L, 1L);
    }

    @Test
    public void testParticipate_BadRequest() {
        // Call the participate method with an invalid ID
        ResponseEntity<?> response = sessionController.participate("invalid", "1");

        // Verify that the response status is BAD_REQUEST
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testNoLongerParticipate_Success() {
        // Call the noLongerParticipate method
        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "1");

        // Verify the response status and check if noLongerParticipate was called once
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService, times(1)).noLongerParticipate(1L, 1L);
    }

    @Test
    public void testNoLongerParticipate_BadRequest() {
        // Call the noLongerParticipate method with an invalid ID
        ResponseEntity<?> response = sessionController.noLongerParticipate("invalid", "1");

        // Verify that the response status is BAD_REQUEST
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
