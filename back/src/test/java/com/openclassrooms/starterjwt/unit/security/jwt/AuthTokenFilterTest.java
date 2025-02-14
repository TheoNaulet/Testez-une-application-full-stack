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
    private AuthTokenFilter authTokenFilter; // Injects mocks into AuthTokenFilter

    @Mock
    private JwtUtils jwtUtils; // Mock for JWT utility class

    @Mock
    private UserDetailsServiceImpl userDetailsService; // Mock for user details service

    @Mock
    private FilterChain filterChain; // Mock for servlet filter chain

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes mocks
        request = new MockHttpServletRequest(); // Mock HTTP request
        response = new MockHttpServletResponse(); // Mock HTTP response
        SecurityContextHolder.clearContext(); // Clear authentication context before each test
    }

    @Test
    void doFilter_ValidToken_ShouldAuthenticateUser() throws ServletException, IOException {
        // Arrange: Prepare mock data
        String token = "validToken";
        String username = "testUser";
        UserDetails userDetails = new User(username, "password", Collections.emptyList()); // Mock user details

        request.addHeader("Authorization", "Bearer " + token); // Set Authorization header

        // Mock JWT validation and user retrieval behavior
        when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Act: Execute the filter (calls doFilterInternal)
        authTokenFilter.doFilter(request, response, filterChain);

        // Assert: Verify that the user has been authenticated
        UsernamePasswordAuthenticationToken authentication =
            (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        assert authentication != null;
        assert authentication.getPrincipal().equals(userDetails);

        // Verify that the filter continues execution
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilter_InvalidToken_ShouldNotAuthenticateUser() throws ServletException, IOException {
        // Arrange: Prepare an invalid token
        String token = "invalidToken";

        request.addHeader("Authorization", "Bearer " + token);

        // Mock JWT validation failure
        when(jwtUtils.validateJwtToken(token)).thenReturn(false);

        // Act: Execute the filter
        authTokenFilter.doFilter(request, response, filterChain);

        // Assert: Ensure authentication context is still null (user not authenticated)
        assert SecurityContextHolder.getContext().getAuthentication() == null;

        // Verify that the filter continues execution
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilter_NoToken_ShouldNotAuthenticateUser() throws ServletException, IOException {
        // Act: Execute the filter without a token
        authTokenFilter.doFilter(request, response, filterChain);

        // Assert: Ensure authentication context remains null (no user authentication)
        assert SecurityContextHolder.getContext().getAuthentication() == null;

        // Verify that the filter continues execution
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
