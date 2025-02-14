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
        // Initialize validator for testing bean validation constraints
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateSessionWithValidData() {
        // GIVEN: Create a teacher and users for the session
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

        // WHEN: Create a valid session instance
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

        // THEN: Validate that the session was created correctly
        assertNotNull(session);
        assertThat(session.getId()).isEqualTo(100L);
        assertThat(session.getName()).isEqualTo("Math Class");
        assertThat(session.getDescription()).isEqualTo("Advanced mathematics session");
        assertThat(session.getTeacher()).isNotNull();
        assertThat(session.getUsers()).hasSize(2);
    }

    @Test
    void shouldFailValidationWhenFieldsAreInvalid() {
        // GIVEN: Create a session with missing required fields
        Session session = new Session();

        // WHEN: Validate the session
        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        // THEN: Ensure that there are validation errors
        assertThat(violations).hasSizeGreaterThan(0);
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("date"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description"))).isTrue();
    }

    @Test
    void shouldVerifyEqualsAndHashCode() {
        // GIVEN: Create three session instances with different IDs
        Session session1 = new Session();
        session1.setId(1L);

        Session session2 = new Session();
        session2.setId(1L);

        Session session3 = new Session();
        session3.setId(2L);

        // THEN: Validate equality and hashCode behavior
        assertThat(session1).isEqualTo(session2);
        assertThat(session1).isNotEqualTo(session3);
        assertThat(session1.hashCode()).isEqualTo(session2.hashCode());
        assertThat(session1.hashCode()).isNotEqualTo(session3.hashCode());
        assertThat(session1).isNotEqualTo(null);
        assertThat(session1).isNotEqualTo(new Object());
    }

    @Test
    void shouldTestToStringMethod() {
        // GIVEN: Create a session instance with sample data
        Session session = new Session();
        session.setId(5L);
        session.setName("Physics Lecture");
        session.setDescription("Introduction to Physics");
        session.setDate(new Date());
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());

        // WHEN: Call toString()
        String sessionString = session.toString();

        // THEN: Validate that key fields are included in the string representation
        assertThat(sessionString).contains("Physics Lecture");
        assertThat(sessionString).contains("Introduction to Physics");
        assertThat(sessionString).contains("id=5");
    }

    @Test
    void shouldTestEqualsAndHashCodeForDifferentCases() {
        // GIVEN: Create session instances for comparison
        Session session1 = new Session();
        session1.setId(1L);

        Session session2 = new Session();
        session2.setId(1L);

        Session session3 = new Session();
        session3.setId(2L);

        Session sessionNull = null;
        Object otherObject = new Object();

        // THEN: Validate equals() behavior
        assertThat(session1.equals(session1)).isTrue(); // Case 1: Same instance
        assertThat(session1.equals(sessionNull)).isFalse(); // Case 2: Comparison with null
        assertThat(session1.equals(otherObject)).isFalse(); // Case 3: Comparison with different class
        assertThat(session1.equals(session2)).isTrue(); // Case 4: Same ID → should be equal
        assertThat(session1.equals(session3)).isFalse(); // Case 5: Different IDs → should not be equal

        // Case 6: One session has a null ID → should not be equal
        Session session4 = new Session();
        session4.setId(null);
        assertThat(session1.equals(session4)).isFalse();

        // Case 7: Two sessions with null IDs → should be equal
        Session session5 = new Session();
        Session session6 = new Session();
        assertThat(session5.equals(session6)).isTrue();

        // Validate hashCode()
        assertThat(session1.hashCode()).isEqualTo(session2.hashCode()); // Case 8: Same ID → same hashCode
        assertThat(session1.hashCode()).isNotEqualTo(session3.hashCode()); // Case 9: Different IDs → different hashCodes
        assertThat(session5.hashCode()).isEqualTo(session6.hashCode()); // Case 10: Both null IDs → same hashCode
    }
}
