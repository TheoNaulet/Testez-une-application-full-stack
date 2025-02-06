package com.openclassrooms.starterjwt.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository mockTeacherRepo;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void shouldReturnAllTeachers_whenFindAllIsCalled() {

        // Arrange
        Teacher teacher1 = new Teacher(1L, "Smith", "Alice", LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher2 = new Teacher(2L, "Brown", "Bob", LocalDateTime.now(), LocalDateTime.now());
        when(mockTeacherRepo.findAll()).thenReturn(Arrays.asList(teacher1, teacher2));

        // Act
        List<Teacher> teachers = teacherService.findAll();

        // Assert
        assertThat(teachers).hasSize(2).containsExactly(teacher1, teacher2);
        verify(mockTeacherRepo, times(1)).findAll();
    }

    @Test
    void shouldReturnTeacher_whenValidIdIsProvided() {

        // Arrange
        Long validId = 10L;
        Teacher expectedTeacher = new Teacher(validId, "Williams", "Cathy", LocalDateTime.now(), LocalDateTime.now());
        when(mockTeacherRepo.findById(validId)).thenReturn(Optional.of(expectedTeacher));

        // Act
        Teacher result = teacherService.findById(validId);

        // Assert
        assertThat(result).isNotNull().isEqualTo(expectedTeacher);
        verify(mockTeacherRepo, times(1)).findById(validId);
    }

    @Test
    void shouldReturnNull_whenNonExistingIdIsProvided() {

        // Arrange
        Long nonExistentId = 99L;
        when(mockTeacherRepo.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act
        Teacher result = teacherService.findById(nonExistentId);

        // Assert
        assertThat(result).isNull();
        verify(mockTeacherRepo, times(1)).findById(nonExistentId);
    }
}
