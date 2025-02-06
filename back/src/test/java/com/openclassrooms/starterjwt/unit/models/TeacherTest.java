package com.openclassrooms.starterjwt.unit.models;

import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateTeacherWithValidData() {
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(LocalDateTime.of(2024, 1, 1, 10, 0))
                .updatedAt(LocalDateTime.of(2024, 1, 2, 12, 0))
                .build();

        assertThat(teacher).isNotNull();
        assertThat(teacher.getId()).isEqualTo(1L);
        assertThat(teacher.getFirstName()).isEqualTo("John");
        assertThat(teacher.getLastName()).isEqualTo("Doe");
        assertThat(teacher.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0));
        assertThat(teacher.getUpdatedAt()).isEqualTo(LocalDateTime.of(2024, 1, 2, 12, 0));
    }

    @Test
    void shouldFailValidationWhenFieldsAreInvalid() {
        Teacher teacher = new Teacher(); // Objet vide => invalide

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        assertThat(violations).hasSize(2); // firstName & lastName @NotBlank
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName"))).isTrue();
    }

    @Test
    void shouldVerifyEqualsAndHashCode() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");
    
        Teacher teacher2 = new Teacher();
        teacher2.setId(1L);
        teacher2.setFirstName("John");
        teacher2.setLastName("Doe");
    
        Teacher teacher3 = new Teacher();
        teacher3.setId(2L);
        teacher3.setFirstName("John");
        teacher3.setLastName("Doe");
    
        Teacher teacherWithNullId = new Teacher();
        teacherWithNullId.setId(null);
        teacherWithNullId.setFirstName("John");
        teacherWithNullId.setLastName("Doe");
    
        // 🔹 Vérifier que deux objets identiques sont égaux
        assertThat(teacher1).isEqualTo(teacher2);
        assertThat(teacher1.hashCode()).isEqualTo(teacher2.hashCode());
    
        // Vérifier que deux objets avec IDs différents ne sont pas égaux
        assertThat(teacher1).isNotEqualTo(teacher3);
        assertThat(teacher1.hashCode()).isNotEqualTo(teacher3.hashCode());
    
        // Vérifier que comparer à `null` retourne `false`
        assertThat(teacher1).isNotEqualTo(null);
    
        // Vérifier que comparer avec une autre classe retourne `false`
        assertThat(teacher1).isNotEqualTo("Some String");
    
        // Vérifier que comparer un ID `null` avec un ID défini retourne `false`
        assertThat(teacher1).isNotEqualTo(teacherWithNullId);
    }
    
    @Test
    void shouldTestToStringMethod() {
        Teacher teacher = new Teacher();
        teacher.setId(5L);
        teacher.setFirstName("Alice");
        teacher.setLastName("Smith");

        assertThat(teacher.toString()).contains("Alice", "Smith");
    }

    @Test
    void shouldReturnTrueForEqualObjects() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");

        Teacher teacher2 = new Teacher();
        teacher2.setId(1L);
        teacher2.setFirstName("John");
        teacher2.setLastName("Doe");

        assertThat(teacher1).isEqualTo(teacher2);
        assertThat(teacher1.hashCode()).isEqualTo(teacher2.hashCode());
    }

    @Test
    void shouldReturnFalseForDifferentObjects() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L); // ID différent
        teacher2.setFirstName("John");
        teacher2.setLastName("Doe");

        assertThat(teacher1).isNotEqualTo(teacher2);
        assertThat(teacher1.hashCode()).isNotEqualTo(teacher2.hashCode());
    }

    @Test
void shouldReturnTrueForSameIdEvenIfNamesAreDifferent() {
    Teacher teacher1 = new Teacher();
    teacher1.setId(1L);
    teacher1.setFirstName("John");
    teacher1.setLastName("Doe");

    Teacher teacher2 = new Teacher();
    teacher2.setId(1L);
    teacher2.setFirstName("Jane"); 
    teacher2.setLastName("Doe");

    assertThat(teacher1).isEqualTo(teacher2); 
}

@Test
void shouldReturnTrueWhenBothIdsAreNull() {
    Teacher teacher1 = new Teacher();
    teacher1.setId(null);
    teacher1.setFirstName("John");
    teacher1.setLastName("Doe");

    Teacher teacher2 = new Teacher();
    teacher2.setId(null);
    teacher2.setFirstName("John");
    teacher2.setLastName("Doe");

    assertThat(teacher1).isEqualTo(teacher2);
}

@Test
void shouldReturnFalseWhenOneIdIsNullAndOtherIsNot() {
    Teacher teacher1 = new Teacher();
    teacher1.setId(null);
    teacher1.setFirstName("John");
    teacher1.setLastName("Doe");

    Teacher teacher2 = new Teacher();
    teacher2.setId(1L);
    teacher2.setFirstName("John");
    teacher2.setLastName("Doe");

    assertThat(teacher1).isNotEqualTo(teacher2); 
}

@Test
void shouldReturnTrueForSameIdWithDifferentTimestamps() {
    Teacher teacher1 = new Teacher();
    teacher1.setId(1L);
    teacher1.setFirstName("John");
    teacher1.setLastName("Doe");
    teacher1.setCreatedAt(LocalDateTime.of(2024, 1, 1, 10, 0));
    teacher1.setUpdatedAt(LocalDateTime.of(2024, 1, 2, 12, 0));

    Teacher teacher2 = new Teacher();
    teacher2.setId(1L);
    teacher2.setFirstName("John");
    teacher2.setLastName("Doe");
    teacher2.setCreatedAt(LocalDateTime.of(2025, 1, 1, 10, 0)); // Différent
    teacher2.setUpdatedAt(LocalDateTime.of(2025, 1, 2, 12, 0)); // Différent

    assertThat(teacher1).isEqualTo(teacher2); 
}




    @Test
    void shouldReturnFalseWhenComparingWithNullOrDifferentClass() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        assertThat(teacher).isNotEqualTo(null);
        assertThat(teacher).isNotEqualTo("Some String");
    }

    @Test
    void shouldReturnTrueForSameInstance() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        assertThat(teacher).isEqualTo(teacher);
        assertThat(teacher.hashCode()).isEqualTo(teacher.hashCode());
    }
}
