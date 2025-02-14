package com.openclassrooms.starterjwt.unit.security;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsImplTest {

    @Test
    void testAdminField() {
        // Arrange: Create a UserDetailsImpl instance with admin set to true
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("testUser")
                .firstName("John")
                .lastName("Doe")
                .admin(true) // Setting admin flag
                .password("password123")
                .build();
        
        // Assert: Verify that the admin field is correctly set
        assertTrue(user.getAdmin(), "Admin field should be true");
    }

    @Test
    void testEqualsMethod() {
        // Arrange: Create multiple UserDetailsImpl instances with different attributes
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .id(1L) // ID is the same
                .username("testUser")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .password("password123")
                .build();

        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(1L) // Same ID as user1 (should be considered equal)
                .username("testUser2")
                .firstName("Jane")
                .lastName("Smith")
                .admin(false)
                .password("password456")
                .build();

        UserDetailsImpl user3 = UserDetailsImpl.builder()
                .id(2L) // Different ID (should be considered different)
                .username("testUser3")
                .firstName("Mark")
                .lastName("Brown")
                .admin(true)
                .password("password789")
                .build();

        // Assert: Users with the same ID should be equal
        assertEquals(user1, user2, "Users with the same id should be equal");

        // Assert: Users with different IDs should not be equal
        assertNotEquals(user1, user3, "Users with different ids should not be equal");

        // Assert: User should not be equal to null
        assertNotEquals(user1, null, "User should not be equal to null");

        // Assert: User should not be equal to a completely different object type
        assertNotEquals(user1, new Object(), "User should not be equal to a different object type");
    }

    @Test
    void testEqualsWithSameInstance() {
        // Arrange: Create a UserDetailsImpl instance
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("testUser")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .password("password123")
                .build();
        
        // Assert: An object should always be equal to itself
        assertEquals(user, user, "User should be equal to itself");
    }
}
