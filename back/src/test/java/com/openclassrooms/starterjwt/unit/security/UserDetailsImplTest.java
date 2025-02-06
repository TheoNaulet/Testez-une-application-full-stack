package com.openclassrooms.starterjwt.unit.security;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsImplTest {

    @Test
    void testAdminField() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("testUser")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .password("password123")
                .build();
        
        assertTrue(user.getAdmin(), "Admin field should be true");
    }

    @Test
    void testEqualsMethod() {
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .id(1L)
                .username("testUser")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .password("password123")
                .build();

        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(1L)
                .username("testUser2")
                .firstName("Jane")
                .lastName("Smith")
                .admin(false)
                .password("password456")
                .build();

        UserDetailsImpl user3 = UserDetailsImpl.builder()
                .id(2L)
                .username("testUser3")
                .firstName("Mark")
                .lastName("Brown")
                .admin(true)
                .password("password789")
                .build();

        assertEquals(user1, user2, "Users with the same id should be equal");
        assertNotEquals(user1, user3, "Users with different ids should not be equal");
        assertNotEquals(user1, null, "User should not be equal to null");
        assertNotEquals(user1, new Object(), "User should not be equal to a different object type");
    }

    @Test
    void testEqualsWithSameInstance() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("testUser")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .password("password123")
                .build();
        
        assertEquals(user, user, "User should be equal to itself");
    }
}