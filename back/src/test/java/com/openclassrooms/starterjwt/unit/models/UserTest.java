package com.openclassrooms.starterjwt.unit.models;

import org.junit.jupiter.api.Test;
import com.openclassrooms.starterjwt.models.User;

import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {

    @Test
    void shouldCreateEmptyUserAndValidateMethods() {
        User userA = new User();
        User userB = new User();

        assertThat(userA).isEqualTo(userB);
        assertThat(userA.hashCode()).isEqualTo(userB.hashCode());
        assertThat(userA.toString()).isNotBlank();
    }

    @Test
    void shouldBuildUserWithValidProperties() {
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
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("newPass");

        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getPassword()).isEqualTo("newPass");
    }


    @Test
    void testUserConstructor() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(1L, "test@example.com", "Doe", "John", "password", false, now, now);

        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("password", user.getPassword());
        assertFalse(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void testGettersAndSetters() {
        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setPassword("password");
        user.setAdmin(false);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("password", user.getPassword());
        assertFalse(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }



    @Test
    void shouldVerifyEqualsAndHashCode() {
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
    
        user1.setId(1L);
        user2.setId(1L); 
        user3.setId(2L); 
    
        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isNotEqualTo(user3);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
        assertThat(user1.hashCode()).isNotEqualTo(user3.hashCode());
    }
    

    @Test
    void shouldHandleTimestamps() {
        User user = new User();
        LocalDateTime before = LocalDateTime.now();
        
        user.setUpdatedAt(before.plusDays(1));
        assertThat(user.getUpdatedAt()).isAfter(before);
    }

    @Test
    void shouldCreateUserWithAllFields() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 2, 12, 0);

        User user = new User(10L, "lily.martin@example.com", "Martin", "Lily", "securePass123", true, createdAt, updatedAt);

        assertThat(user.getId()).isEqualTo(10L);
        assertThat(user.getEmail()).isEqualTo("lily.martin@example.com");
        assertThat(user.getLastName()).isEqualTo("Martin");
        assertThat(user.getFirstName()).isEqualTo("Lily");
        assertThat(user.getPassword()).isEqualTo("securePass123");
        assertThat(user.isAdmin()).isTrue();
        assertThat(user.getCreatedAt()).isEqualTo(createdAt);
        assertThat(user.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
void shouldTestEqualsForNullAndDifferentClass() {
    User user = new User();
    User userWithNullId = new User();
    userWithNullId.setId(null);

    assertThat(user).isNotEqualTo(null); // Cas 1 : Comparaison avec null
    assertThat(user).isNotEqualTo(new Object()); // Cas 2 : Comparaison avec un objet d'une autre classe
    assertThat(user).isEqualTo(userWithNullId); // Cas 3 : Deux objets avec ID null doivent être égaux
}

@Test
void shouldTestHashCodeWithNullId() {
    User user1 = new User();
    User user2 = new User();

    assertThat(user1.hashCode()).isEqualTo(user2.hashCode()); // Cas 4 : Deux objets avec ID null → hashCode doit être le même
}

    @Test
    void shouldCreateUserWithRequiredArgsConstructor() {
        User user = new User("test@example.com", "Doe", "John", "password123", true);
        
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.isAdmin()).isTrue();
        assertThat(user.getId()).isNull(); 
        assertThat(user.getCreatedAt()).isNull(); 
        assertThat(user.getUpdatedAt()).isNull();
    }

@Test
void shouldThrowExceptionWhenConstructingWithNullValues() {
    // Vérifier que chaque champ @NonNull lève bien une exception s'il est null
    assertThrows(NullPointerException.class, () -> new User(null, "Doe", "John", "password", true));
    assertThrows(NullPointerException.class, () -> new User("test@example.com", null, "John", "password", true));
    assertThrows(NullPointerException.class, () -> new User("test@example.com", "Doe", null, "password", true));
    assertThrows(NullPointerException.class, () -> new User("test@example.com", "Doe", "John", null, true));
}



@Test
void shouldThrowExceptionWhenSettingNullValues() {
    User user = new User();

    assertThrows(NullPointerException.class, () -> user.setEmail(null));
    assertThrows(NullPointerException.class, () -> user.setLastName(null));
    assertThrows(NullPointerException.class, () -> user.setFirstName(null));
    assertThrows(NullPointerException.class, () -> user.setPassword(null));
}


@Test
void shouldHandleMaxSizeConstraints() {
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

    assertThat(user.getEmail()).hasSize(50);
    assertThat(user.getLastName()).hasSize(20);
    assertThat(user.getFirstName()).hasSize(20);
    assertThat(user.getPassword()).hasSize(120);
}

@Test
void shouldVerifyEqualsAndHashCodeForConstructedUsers() {
    LocalDateTime now = LocalDateTime.now();
    
    User user1 = new User(1L, "test@example.com", "Doe", "John", "password", false, now, now);
    User user2 = new User(1L, "test@example.com", "Doe", "John", "password", false, now, now);
    
    assertThat(user1).isEqualTo(user2);
    assertThat(user1.hashCode()).isEqualTo(user2.hashCode());

    User user3 = new User(2L, "test2@example.com", "Smith", "Alice", "password", true, now, now);
    assertThat(user1).isNotEqualTo(user3);
}


}
