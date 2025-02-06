package com.openclassrooms.starterjwt.unit.controller;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
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

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    private Teacher teacher;
    private final Long teacherId = 1L;

    @BeforeEach
    public void setUp() {
        teacher = new Teacher();
        teacher.setId(teacherId);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
    }

    @Test
    public void testFindById_TeacherFound() {
        // Arrange
        when(teacherService.findById(teacherId)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(new com.openclassrooms.starterjwt.dto.TeacherDto());

        // Act
        ResponseEntity<?> response = teacherController.findById(teacherId.toString());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(teacherService, times(1)).findById(teacherId);
        verify(teacherMapper, times(1)).toDto(teacher);
    }

    @Test
    public void testFindById_TeacherNotFound() {
        // Arrange
        when(teacherService.findById(teacherId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = teacherController.findById(teacherId.toString());

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(teacherService, times(1)).findById(teacherId);
        verify(teacherMapper, never()).toDto(teacher);
    }

    @Test
    public void testFindById_InvalidIdFormat() {
        // Act
        ResponseEntity<?> response = teacherController.findById("invalidId");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(teacherService, never()).findById(any());
        verify(teacherMapper, never()).toDto(teacher);
    }

    @Test
    public void testFindAll_TeachersFound() {
        // Arrange
        List<Teacher> teachers = Arrays.asList(teacher);
        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(Arrays.asList(new com.openclassrooms.starterjwt.dto.TeacherDto()));

        // Act
        ResponseEntity<?> response = teacherController.findAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(teacherService, times(1)).findAll();
        verify(teacherMapper, times(1)).toDto(teachers);
    }

    @Test
    public void testFindAll_NoTeachersFound() {
        // Arrange
        List<Teacher> teachers = Arrays.asList();
        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<?> response = teacherController.findAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(teacherService, times(1)).findAll();
        verify(teacherMapper, times(1)).toDto(teachers);
    }
}