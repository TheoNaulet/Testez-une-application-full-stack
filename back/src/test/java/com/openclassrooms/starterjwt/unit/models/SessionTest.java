package com.openclassrooms.starterjwt.unit.models;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SessionTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateSessionWithValidData() {
        Teacher teacher = new Teacher(); 
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("Alice");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Bob");

        Session session = Session.builder()
                .id(100L)
                .name("Math Class")
                .date(new Date())
                .description("Advanced mathematics session")
                .teacher(teacher)
                .users(List.of(user1, user2))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertNotNull(session);
        assertThat(session.getId()).isEqualTo(100L);
        assertThat(session.getName()).isEqualTo("Math Class");
        assertThat(session.getDescription()).isEqualTo("Advanced mathematics session");
        assertThat(session.getTeacher()).isNotNull();
        assertThat(session.getUsers()).hasSize(2);
    }

    @Test
    void shouldFailValidationWhenFieldsAreInvalid() {
        Session session = new Session();

        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        assertThat(violations).hasSizeGreaterThan(0);
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("date"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description"))).isTrue();
    }

    @Test
    void shouldVerifyEqualsAndHashCode() {
        Session session1 = new Session();
        session1.setId(1L);
    
        Session session2 = new Session();
        session2.setId(1L);
    
        Session session3 = new Session();
        session3.setId(2L);
    
        // Test equality
        assertThat(session1).isEqualTo(session2);
        assertThat(session1).isNotEqualTo(session3);
    
        // Test hashcode
        assertThat(session1.hashCode()).isEqualTo(session2.hashCode());
        assertThat(session1.hashCode()).isNotEqualTo(session3.hashCode());
    
        // Test null and different class
        assertThat(session1).isNotEqualTo(null);
        assertThat(session1).isNotEqualTo(new Object());
    }


    @Test
    void shouldTestHashCode() {
        Session session1 = new Session();
        session1.setId(1L);

        Session session2 = new Session();
        session2.setId(1L);

        Session session3 = new Session();
        session3.setId(2L);

        assertThat(session1.hashCode()).isEqualTo(session2.hashCode());

        assertThat(session1.hashCode()).isNotEqualTo(session3.hashCode());
    }

    @Test
    void shouldTestToStringMethod() {
        Session session = new Session();
        session.setId(5L);
        session.setName("Physics Lecture");
        session.setDescription("Introduction to Physics");
        session.setDate(new Date());
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());

        String sessionString = session.toString();

        assertThat(sessionString).contains("Physics Lecture");
        assertThat(sessionString).contains("Introduction to Physics");
        assertThat(sessionString).contains("id=5");
    }

    @Test
    void shouldTestEqualsAndHashCodeForDifferentCases() {
        Session session1 = new Session();
        session1.setId(1L);

        Session session2 = new Session();
        session2.setId(1L);

        Session session3 = new Session();
        session3.setId(2L);

        Session sessionNull = null;
        Object otherObject = new Object();

        // Cas 1 : Même instance
        assertThat(session1.equals(session1)).isTrue();

        // Cas 2 : Objet null
        assertThat(session1.equals(sessionNull)).isFalse();

        // Cas 3 : Différentes classes
        assertThat(session1.equals(otherObject)).isFalse();

        // Cas 4 : Même ID → doit être égal
        assertThat(session1.equals(session2)).isTrue();

        // Cas 5 : ID différent → doit être différent
        assertThat(session1.equals(session3)).isFalse();

        // Cas 6 : ID null dans un des objets → doit être faux
        Session session4 = new Session();
        session4.setId(null);
        assertThat(session1.equals(session4)).isFalse();

        // Cas 7 : Deux objets avec ID null → doivent être égaux
        Session session5 = new Session();
        Session session6 = new Session();
        assertThat(session5.equals(session6)).isTrue();

        // Tests sur hashCode

        // Cas 8 : Deux objets avec le même ID doivent avoir le même hashCode
        assertThat(session1.hashCode()).isEqualTo(session2.hashCode());

        // Cas 9 : Deux objets avec des IDs différents doivent avoir des hashCodes différents
        assertThat(session1.hashCode()).isNotEqualTo(session3.hashCode());

        // Cas 10 : Deux objets avec un ID null doivent avoir le même hashCode
        assertThat(session5.hashCode()).isEqualTo(session6.hashCode());
    }
}