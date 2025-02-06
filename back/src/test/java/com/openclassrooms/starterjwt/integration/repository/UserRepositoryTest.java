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

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenFindByEmail_thenReturnUser() {
        // given
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password123");
        entityManager.persist(user);
        entityManager.flush();

        // when
        Optional<User> found = userRepository.findByEmail(user.getEmail());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void whenFindByEmail_thenReturnEmpty() {
        // given
        String email = "nonexistent@example.com";

        // when
        Optional<User> found = userRepository.findByEmail(email);

        // then
        assertThat(found).isNotPresent();
    }

    @Test
    public void whenExistsByEmail_thenReturnTrue() {
        // given
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password123");
        entityManager.persist(user);
        entityManager.flush();

        // when
        Boolean exists = userRepository.existsByEmail(user.getEmail());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    public void whenExistsByEmail_thenReturnFalse() {
        // given
        String email = "nonexistent@example.com";

        // when
        Boolean exists = userRepository.existsByEmail(email);

        // then
        assertThat(exists).isFalse();
    }
}