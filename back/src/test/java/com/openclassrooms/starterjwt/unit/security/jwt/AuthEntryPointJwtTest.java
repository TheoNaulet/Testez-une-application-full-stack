package com.openclassrooms.starterjwt.unit.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.MimeTypeUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class) // Enables Mockito extensions for JUnit 5
class AuthEntryPointJwtTest {

    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt; // Injects the mock into AuthEntryPointJwt

    @Test
    void commenceTest() throws IOException, ServletException {
        // Arrange: Create mock HTTP request and response
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setServletPath("/api/protected"); // Simulating an API path

        // Simulate an authentication exception with a custom message
        AuthenticationException authException = new AuthenticationException("Unauthorized error message") {};

        // Act: Call the commence method to handle unauthorized access
        authEntryPointJwt.commence(request, response, authException);

        // Assert: Verify the response status is 401 (Unauthorized)
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);

        // Assert: Verify the response content type is JSON
        assertThat(response.getContentType()).isEqualTo(MimeTypeUtils.APPLICATION_JSON_VALUE);

        // Parse the response body into a Map for assertion
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseBody = objectMapper.readValue(response.getContentAsString(), Map.class);

        // Assert: Verify the response body fields
        assertThat(responseBody.get("status")).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(responseBody.get("error")).isEqualTo("Unauthorized");
        assertThat(responseBody.get("message")).isEqualTo("Unauthorized error message");
        assertThat(responseBody.get("path")).isEqualTo("/api/protected");
    }
}
