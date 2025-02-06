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

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class TeacherServiceTest {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRepository teacherRepository;


    @Test
    void testFindAll() {
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

        // Act: Récupérer tous les enseignants
        List<Teacher> teachers = teacherService.findAll();

        // Assert: Vérifier que les enseignants sont bien retournés
        assertThat(teachers).hasSize(2);
        assertThat(teachers).extracting(Teacher::getLastName).contains("André", "Smith");
    }

    @Test
    void testFindByIdExistingTeacher() {
        // Arrange: Ajouter un enseignant dans la base de données
        Teacher teacher = Teacher.builder()
                .lastName("André")
                .firstName("Gauthier")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher);

        // Act: Récupérer l'enseignant par ID
        Teacher result = teacherService.findById(teacher.getId());

        // Assert: Vérifier que l'enseignant est bien retourné
        assertThat(result).isNotNull();
        assertThat(result.getLastName()).isEqualTo("André");
        assertThat(result.getFirstName()).isEqualTo("Gauthier");
    }

    @Test
    void testFindByIdNonExistingTeacher() {
        // Act: Essayer de récupérer un enseignant avec un ID inexistant
        Teacher result = teacherService.findById(999L);

        // Assert: Vérifier que le résultat est null
        assertThat(result).isNull();
    }
}
