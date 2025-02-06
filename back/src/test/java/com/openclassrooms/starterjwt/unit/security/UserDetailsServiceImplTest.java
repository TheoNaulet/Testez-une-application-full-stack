package com.openclassrooms.starterjwt.unit.security;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User testUser;
    private final String testEmail = "test@example.com";
    private final String wrongEmail = "wrong@example.com";

    @BeforeEach
    void setUp() {
        // Initialisation d'un utilisateur de test
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail(testEmail);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setPassword("password123");
        testUser.setAdmin(false);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Charger un utilisateur par son email - Succès")
    void whenUserExists_thenLoadUserByUsernameShouldReturnUserDetails() {
        // Arrange : Simuler la réponse du repository
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));

        // Act : Appeler la méthode à tester
        UserDetails userDetails = userDetailsService.loadUserByUsername(testEmail);

        // Assert : Vérifier que les détails de l'utilisateur sont corrects
        assertNotNull(userDetails, "Les détails de l'utilisateur ne devraient pas être null");
        assertEquals(testUser.getEmail(), userDetails.getUsername(), "L'email de l'utilisateur devrait correspondre");
        assertEquals(testUser.getPassword(), userDetails.getPassword(), "Le mot de passe de l'utilisateur devrait correspondre");

        // Vérifier que la méthode du repository a bien été appelée
        verify(userRepository, times(1)).findByEmail(testEmail);
    }

    @Test
    @DisplayName("Charger un utilisateur par son email - Utilisateur non trouvé")
    void whenUserDoesNotExist_thenThrowUsernameNotFoundException() {
        // Arrange : Simuler une réponse vide du repository
        when(userRepository.findByEmail(wrongEmail)).thenReturn(Optional.empty());

        // Act & Assert : Vérifier que l'exception est levée
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(wrongEmail);
        }, "Une exception devrait être levée si l'utilisateur n'existe pas");

        // Vérifier que la méthode du repository a bien été appelée
        verify(userRepository, times(1)).findByEmail(wrongEmail);
    }
}