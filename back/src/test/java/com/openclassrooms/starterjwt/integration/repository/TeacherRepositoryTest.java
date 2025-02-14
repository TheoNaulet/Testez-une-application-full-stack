package com.openclassrooms.starterjwt.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@DataJpaTest // Configures the test to focus on JPA components
@ActiveProfiles("test") // Uses the 'test' profile configuration for the database
public class TeacherRepositoryTest {

    @Autowired
    private TestEntityManager entityManager; // Provides an entity manager for direct interaction with the database

    @Autowired
    private TeacherRepository teacherRepository; // Injects the TeacherRepository for testing

    @Test
    public void whenFindById_thenReturnTeacher() {
        // Arrange: Create and persist a teacher entity in the test database
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        entityManager.persist(teacher); // Save the entity in the database
        entityManager.flush(); // Ensure data is written before the test executes

        // Act: Retrieve the teacher by ID
        Teacher found = teacherRepository.findById(teacher.getId()).orElse(null);

        // Assert: Verify that the teacher is correctly retrieved
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(teacher.getId());
        assertThat(found.getFirstName()).isEqualTo(teacher.getFirstName());
        assertThat(found.getLastName()).isEqualTo(teacher.getLastName());
    }

    @Test
    public void whenFindById_thenReturnNull() {
        // Arrange: Use a non-existent teacher ID
        Long nonExistentId = 999L;

        // Act: Try to find a teacher with a non-existing ID
        Teacher found = teacherRepository.findById(nonExistentId).orElse(null);

        // Assert: Verify that the teacher is not found (null result)
        assertThat(found).isNull();
    }

    @Test
    public void whenSaveTeacher_thenTeacherIsPersisted() {
        // Arrange: Create a new teacher entity
        Teacher teacher = new Teacher();
        teacher.setFirstName("Jane");
        teacher.setLastName("Smith");

        // Act: Save the teacher entity in the database
        Teacher savedTeacher = teacherRepository.save(teacher);

        // Assert: Verify that the teacher has been successfully persisted
        assertThat(savedTeacher).isNotNull();
        assertThat(savedTeacher.getId()).isNotNull(); // Ensure the ID is generated
        assertThat(savedTeacher.getFirstName()).isEqualTo("Jane");
        assertThat(savedTeacher.getLastName()).isEqualTo("Smith");
    }

    @Test
    public void whenDeleteTeacher_thenTeacherIsRemoved() {
        // Arrange: Create and persist a teacher entity in the database
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        entityManager.persist(teacher);
        entityManager.flush(); // Ensure the teacher is stored

        // Act: Delete the teacher by ID
        teacherRepository.deleteById(teacher.getId());
        Teacher deletedTeacher = teacherRepository.findById(teacher.getId()).orElse(null);

        // Assert: Verify that the teacher is no longer in the database
        assertThat(deletedTeacher).isNull();
    }
}
