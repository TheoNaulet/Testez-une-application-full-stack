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

@ExtendWith(MockitoExtension.class) // Enables Mockito extensions for JUnit 5
class TeacherServiceTest {

    @Mock
    private TeacherRepository mockTeacherRepo; // Mocking the TeacherRepository

    @InjectMocks
    private TeacherService teacherService; // Injecting mocks into the TeacherService

    @Test
    void shouldReturnAllTeachers_whenFindAllIsCalled() {
        // Arrange: Create test data
        Teacher teacher1 = new Teacher(1L, "Smith", "Alice", LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher2 = new Teacher(2L, "Brown", "Bob", LocalDateTime.now(), LocalDateTime.now());

        // Mocking repository behavior
        when(mockTeacherRepo.findAll()).thenReturn(Arrays.asList(teacher1, teacher2));

        // Act: Call the method under test
        List<Teacher> teachers = teacherService.findAll();

        // Assert: Verify results and mock interactions
        assertThat(teachers).hasSize(2).containsExactly(teacher1, teacher2);
        verify(mockTeacherRepo, times(1)).findAll(); // Ensure findAll() is called once
    }

    @Test
    void shouldReturnTeacher_whenValidIdIsProvided() {
        // Arrange: Define valid teacher data
        Long validId = 10L;
        Teacher expectedTeacher = new Teacher(validId, "Williams", "Cathy", LocalDateTime.now(), LocalDateTime.now());

        // Mocking repository behavior to return expected teacher
        when(mockTeacherRepo.findById(validId)).thenReturn(Optional.of(expectedTeacher));

        // Act: Call the method under test
        Teacher result = teacherService.findById(validId);

        // Assert: Verify the retrieved teacher matches expected data
        assertThat(result).isNotNull().isEqualTo(expectedTeacher);
        verify(mockTeacherRepo, times(1)).findById(validId); // Ensure findById() is called once
    }

    @Test
    void shouldReturnNull_whenNonExistingIdIsProvided() {
        // Arrange: Define a non-existing ID
        Long nonExistentId = 99L;

        // Mocking repository behavior to return empty result
        when(mockTeacherRepo.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act: Call the method under test
        Teacher result = teacherService.findById(nonExistentId);

        // Assert: Ensure the result is null for non-existent ID
        assertThat(result).isNull();
        verify(mockTeacherRepo, times(1)).findById(nonExistentId); // Ensure findById() is called once
    }
}
