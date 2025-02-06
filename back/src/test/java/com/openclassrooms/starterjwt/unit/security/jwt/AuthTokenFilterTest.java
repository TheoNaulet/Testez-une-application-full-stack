package com.openclassrooms.starterjwt.unit.security.jwt;

import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;

class AuthTokenFilterTest {

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private FilterChain filterChain;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilter_ValidToken_ShouldAuthenticateUser() throws ServletException, IOException {
        // Préparer les mocks
        String token = "validToken";
        String username = "testUser";
        UserDetails userDetails = new User(username, "password", Collections.emptyList());

        request.addHeader("Authorization", "Bearer " + token);

        when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Exécuter le filtre via doFilter (qui appelle doFilterInternal)
        authTokenFilter.doFilter(request, response, filterChain);

        // Vérifier que l'utilisateur a été authentifié
        UsernamePasswordAuthenticationToken authentication =
            (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        assert authentication != null;
        assert authentication.getPrincipal().equals(userDetails);

        // Vérifier que le filtre continue son exécution
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilter_InvalidToken_ShouldNotAuthenticateUser() throws ServletException, IOException {
        // Préparer les mocks
        String token = "invalidToken";

        request.addHeader("Authorization", "Bearer " + token);

        when(jwtUtils.validateJwtToken(token)).thenReturn(false);

        // Exécuter le filtre
        authTokenFilter.doFilter(request, response, filterChain);

        // Vérifier que l'utilisateur n'a pas été authentifié
        assert SecurityContextHolder.getContext().getAuthentication() == null;

        // Vérifier que le filtre continue son exécution
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilter_NoToken_ShouldNotAuthenticateUser() throws ServletException, IOException {
        // Exécuter le filtre sans token
        authTokenFilter.doFilter(request, response, filterChain);

        // Vérifier que l'utilisateur n'a pas été authentifié
        assert SecurityContextHolder.getContext().getAuthentication() == null;

        // Vérifier que le filtre continue son exécution
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
