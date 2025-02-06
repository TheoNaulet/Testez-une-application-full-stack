package com.openclassrooms.starterjwt.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@DataJpaTest
@ActiveProfiles("test")
public class TeacherRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    public void whenFindById_thenReturnTeacher() {
        // given
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        entityManager.persist(teacher);
        entityManager.flush();

        // when
        Teacher found = teacherRepository.findById(teacher.getId()).orElse(null);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(teacher.getId());
        assertThat(found.getFirstName()).isEqualTo(teacher.getFirstName());
        assertThat(found.getLastName()).isEqualTo(teacher.getLastName());
    }

    @Test
    public void whenFindById_thenReturnNull() {
        // given
        Long nonExistentId = 999L;

        // when
        Teacher found = teacherRepository.findById(nonExistentId).orElse(null);

        // then
        assertThat(found).isNull();
    }

    @Test
    public void whenSaveTeacher_thenTeacherIsPersisted() {
        // given
        Teacher teacher = new Teacher();
        teacher.setFirstName("Jane");
        teacher.setLastName("Smith");

        // when
        Teacher savedTeacher = teacherRepository.save(teacher);

        // then
        assertThat(savedTeacher).isNotNull();
        assertThat(savedTeacher.getId()).isNotNull();
        assertThat(savedTeacher.getFirstName()).isEqualTo("Jane");
        assertThat(savedTeacher.getLastName()).isEqualTo("Smith");
    }

    @Test
    public void whenDeleteTeacher_thenTeacherIsRemoved() {
        // given
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        entityManager.persist(teacher);
        entityManager.flush();

        // when
        teacherRepository.deleteById(teacher.getId());
        Teacher deletedTeacher = teacherRepository.findById(teacher.getId()).orElse(null);

        // then
        assertThat(deletedTeacher).isNull();
    }
}