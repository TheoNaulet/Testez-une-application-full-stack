package com.openclassrooms.starterjwt.unit.models;

import org.junit.jupiter.api.Test;
import com.openclassrooms.starterjwt.models.User;

import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateEmptyUserAndValidateMethods() {
        // GIVEN: Create two empty User instances
        User userA = new User();
        User userB = new User();

        // THEN: Validate that they are equal, have the same hashCode, and a valid toString()
        assertThat(userA).isEqualTo(userB);
        assertThat(userA.hashCode()).isEqualTo(userB.hashCode());
        assertThat(userA.toString()).isNotBlank();
    }

    @Test
    void shouldBuildUserWithValidProperties() {
        // GIVEN: Create a User instance using builder
        User user = User.builder()
                .id(10L)
                .firstName("Lily")
                .lastName("Martin")
                .email("lily.martin@example.com")
                .password("securePass123")
                .admin(true)
                .createdAt(LocalDateTime.of(2024, 1, 1, 10, 0))
                .updatedAt(LocalDateTime.of(2024, 1, 2, 12, 0))
                .build();

        // THEN: Verify all values are correctly assigned
        assertThat(user.getId()).isEqualTo(10L);
        assertThat(user.getFirstName()).isEqualTo("Lily");
        assertThat(user.getLastName()).isEqualTo("Martin");
        assertThat(user.getEmail()).isEqualTo("lily.martin@example.com");
        assertThat(user.getPassword()).isEqualTo("securePass123");
        assertThat(user.isAdmin()).isTrue();
        assertThat(user.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0));
        assertThat(user.getUpdatedAt()).isEqualTo(LocalDateTime.of(2024, 1, 2, 12, 0));
    }

    @Test
    void shouldUpdateUserDetails() {
        // GIVEN: Create a User instance
        User user = new User();

        // WHEN: Set new values
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("newPass");

        // THEN: Verify the updated values
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getPassword()).isEqualTo("newPass");
    }

    @Test
    void shouldVerifyEqualsAndHashCode() {
        // GIVEN: Create multiple User instances
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();

        // Assign the same ID to user1 and user2
        user1.setId(1L);
        user2.setId(1L); 
        user3.setId(2L); 

        // THEN: Validate equals() and hashCode() behavior
        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isNotEqualTo(user3);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
        assertThat(user1.hashCode()).isNotEqualTo(user3.hashCode());
    }

    @Test
    void shouldHandleTimestamps() {
        // GIVEN: Create a User instance and capture time before setting updatedAt
        User user = new User();
        LocalDateTime before = LocalDateTime.now();
        
        // WHEN: Set updatedAt to a later time
        user.setUpdatedAt(before.plusDays(1));

        // THEN: Ensure updatedAt is after the captured time
        assertThat(user.getUpdatedAt()).isAfter(before);
    }

    @Test
    void shouldTestEqualsForNullAndDifferentClass() {
        // GIVEN: Create User instances
        User user = new User();
        User userWithNullId = new User();
        userWithNullId.setId(null);

        // THEN: Validate equals() behavior
        assertThat(user).isNotEqualTo(null); // Case 1: Comparison with null
        assertThat(user).isNotEqualTo(new Object()); // Case 2: Comparison with a different class
        assertThat(user).isEqualTo(userWithNullId); // Case 3: Two users with null IDs should be equal
    }

    @Test
    void shouldTestHashCodeWithNullId() {
        // GIVEN: Create two User instances with null IDs
        User user1 = new User();
        User user2 = new User();

        // THEN: Verify they have the same hashCode
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }

    @Test
    void shouldCreateUserWithRequiredArgsConstructor() {
        // GIVEN: Create a User using the required args constructor
        User user = new User("test@example.com", "Doe", "John", "password123", true);
        
        // THEN: Validate assigned values
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.isAdmin()).isTrue();
        assertThat(user.getId()).isNull(); // No ID assigned yet
        assertThat(user.getCreatedAt()).isNull(); 
        assertThat(user.getUpdatedAt()).isNull();
    }

    @Test
    void shouldThrowExceptionWhenConstructingWithNullValues() {
        // THEN: Ensure NullPointerException is thrown when constructing with null values
        assertThrows(NullPointerException.class, () -> new User(null, "Doe", "John", "password", true));
        assertThrows(NullPointerException.class, () -> new User("test@example.com", null, "John", "password", true));
        assertThrows(NullPointerException.class, () -> new User("test@example.com", "Doe", null, "password", true));
        assertThrows(NullPointerException.class, () -> new User("test@example.com", "Doe", "John", null, true));
    }

    @Test
    void shouldThrowExceptionWhenSettingNullValues() {
        // GIVEN: Create a User instance
        User user = new User();

        // THEN: Ensure NullPointerException is thrown when setting null values
        assertThrows(NullPointerException.class, () -> user.setEmail(null));
        assertThrows(NullPointerException.class, () -> user.setLastName(null));
        assertThrows(NullPointerException.class, () -> user.setFirstName(null));
        assertThrows(NullPointerException.class, () -> user.setPassword(null));
    }

    @Test
    void shouldHandleMaxSizeConstraints() {
        // GIVEN: Create a User instance with max-size constraints
        User user = new User(
            99L,
            "a".repeat(50), // max 50 chars
            "b".repeat(20), // max 20 chars
            "c".repeat(20), // max 20 chars
            "d".repeat(120), // max 120 chars
            true,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        // THEN: Verify the length constraints
        assertThat(user.getEmail()).hasSize(50);
        assertThat(user.getLastName()).hasSize(20);
        assertThat(user.getFirstName()).hasSize(20);
        assertThat(user.getPassword()).hasSize(120);
    }

    @Test
    void shouldVerifyEqualsAndHashCodeForConstructedUsers() {
        // GIVEN: Create User instances
        LocalDateTime now = LocalDateTime.now();
        User user1 = new User(1L, "test@example.com", "Doe", "John", "password", false, now, now);
        User user2 = new User(1L, "test@example.com", "Doe", "John", "password", false, now, now);
        User user3 = new User(2L, "test2@example.com", "Smith", "Alice", "password", true, now, now);

        // THEN: Validate equals() and hashCode()
        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
        assertThat(user1).isNotEqualTo(user3);
    }
}
