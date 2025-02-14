package com.openclassrooms.starterjwt.integration.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("user@example.com");
    }

    @Test
    public void testFindById() {
        // Arrange
        User savedUser = userRepository.save(user);

        // Act
        User foundUser = userService.findById(savedUser.getId());

        // Assert
        assertNotNull(foundUser);
        assertEquals(savedUser.getEmail(), foundUser.getEmail());
    }

    @Test
    public void testFindById_NotFound() {
        // Act
        User foundUser = userService.findById(999L);

        // Assert
        assertNull(foundUser);
    }
}