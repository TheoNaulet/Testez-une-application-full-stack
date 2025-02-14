package com.openclassrooms.starterjwt.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@DataJpaTest // Configures the test to focus on JPA components
@ActiveProfiles("test") // Uses the 'test' profile configuration for the database
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager; // Provides an entity manager for test persistence

    @Autowired
    private UserRepository userRepository; // Injects the UserRepository to test database queries

    @Test
    public void whenFindByEmail_thenReturnUser() {
        // Arrange: Create and persist a test user in the database
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password123");
        entityManager.persist(user); // Saves the user into the test database
        entityManager.flush(); // Ensures the data is written to the database before the test executes

        // Act: Try to retrieve the user by email
        Optional<User> found = userRepository.findByEmail(user.getEmail());

        // Assert: Verify that the user is found and the email matches
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void whenFindByEmail_thenReturnEmpty() {
        // Arrange: Use a non-existent email
        String email = "nonexistent@example.com";

        // Act: Try to retrieve a user with this email
        Optional<User> found = userRepository.findByEmail(email);

        // Assert: Verify that the user is not found
        assertThat(found).isNotPresent();
    }

    @Test
    public void whenExistsByEmail_thenReturnTrue() {
        // Arrange: Create and persist a test user in the database
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password123");
        entityManager.persist(user); // Save user into the test database
        entityManager.flush(); // Ensure the data is stored

        // Act: Check if the email exists in the database
        Boolean exists = userRepository.existsByEmail(user.getEmail());

        // Assert: Verify that the email exists
        assertThat(exists).isTrue();
    }

    @Test
    public void whenExistsByEmail_thenReturnFalse() {
        // Arrange: Use a non-existent email
        String email = "nonexistent@example.com";

        // Act: Check if the email exists in the database
        Boolean exists = userRepository.existsByEmail(email);

        // Assert: Verify that the email does not exist
        assertThat(exists).isFalse();
    }
}
