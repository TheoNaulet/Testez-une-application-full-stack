package com.openclassrooms.starterjwt.integration.payload.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.payload.request.LoginRequest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LoginRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    public void testLoginRequestValidation_Success() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert
        assertTrue(violations.isEmpty(), "No validation errors should be present");
    }

    @Test
    public void testLoginRequestValidation_EmailBlank() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(""); // Blank email
        loginRequest.setPassword("password123");

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert
        assertFalse(violations.isEmpty(), "Validation errors should be present for blank email");
        assertEquals(1, violations.size(), "There should be one validation error for blank email");
        ConstraintViolation<LoginRequest> violation = violations.iterator().next();
        assertEquals("email", violation.getPropertyPath().toString(), "The validation error should be for the email field");
    }

    @Test
    public void testLoginRequestValidation_PasswordBlank() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword(""); // Blank password

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert
        assertFalse(violations.isEmpty(), "Validation errors should be present for blank password");
        assertEquals(1, violations.size(), "There should be one validation error for blank password");
        ConstraintViolation<LoginRequest> violation = violations.iterator().next();
        assertEquals("password", violation.getPropertyPath().toString(), "The validation error should be for the password field");
    }

    @Test
    public void testLoginRequestValidation_EmailAndPasswordBlank() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(""); // Blank email
        loginRequest.setPassword(""); // Blank password

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert
        assertFalse(violations.isEmpty(), "Validation errors should be present for blank email and password");
        assertEquals(2, violations.size(), "There should be two validation errors for blank email and password");
    }
}