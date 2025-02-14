package com.openclassrooms.starterjwt.integration.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test") // Specifies that the test profile is being used
@SpringBootTest // Loads the full application context for integration testing
@Transactional // Ensures that tests are rolled back after execution
public class TeacherServiceTest {

    @Autowired
    private TeacherService teacherService; // Injecting the TeacherService

    @Autowired
    private TeacherRepository teacherRepository; // Injecting the TeacherRepository

    @Test
    void testFindAll() {
        // Arrange: Create and save two teacher entities in the database
        Teacher teacher1 = Teacher.builder()
                .lastName("André")
                .firstName("John")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Teacher teacher2 = Teacher.builder()
                .lastName("Smith")
                .firstName("Jane")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);

        // Act: Retrieve all teachers
        List<Teacher> teachers = teacherService.findAll();

        // Assert: Verify that both teachers are returned
        assertThat(teachers).hasSize(2);
        assertThat(teachers).extracting(Teacher::getLastName).contains("André", "Smith");
    }

    @Test
    void testFindByIdExistingTeacher() {
        // Arrange: Add a teacher to the database
        Teacher teacher = Teacher.builder()
                .lastName("André")
                .firstName("Gauthier")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher); // Save and retrieve the persisted entity

        // Act: Retrieve the teacher by ID
        Teacher result = teacherService.findById(teacher.getId());

        // Assert: Verify that the teacher is returned correctly
        assertThat(result).isNotNull();
        assertThat(result.getLastName()).isEqualTo("André");
        assertThat(result.getFirstName()).isEqualTo("Gauthier");
    }

    @Test
    void testFindByIdNonExistingTeacher() {
        // Act: Try to retrieve a teacher with a non-existing ID
        Teacher result = teacherService.findById(999L);

        // Assert: Verify that the result is null
        assertThat(result).isNull();
    }
}
