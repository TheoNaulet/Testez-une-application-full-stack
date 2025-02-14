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

@ExtendWith(MockitoExtension.class) // Enables Mockito extension for JUnit 5
public class LoginRequestTest {

    // Create a Validator instance to validate the LoginRequest object
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    public void testLoginRequestValidation_Success() {
        // Arrange: Create a valid LoginRequest object
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        // Act: Validate the object
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert: Ensure no validation errors are present
        assertTrue(violations.isEmpty(), "No validation errors should be present");
    }

    @Test
    public void testLoginRequestValidation_EmailBlank() {
        // Arrange: Create a LoginRequest with a blank email
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(""); // Blank email
        loginRequest.setPassword("password123");

        // Act: Validate the object
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert: Ensure a validation error is present for the email field
        assertFalse(violations.isEmpty(), "Validation errors should be present for blank email");
        assertEquals(1, violations.size(), "There should be one validation error for blank email");
        ConstraintViolation<LoginRequest> violation = violations.iterator().next();
        assertEquals("email", violation.getPropertyPath().toString(), 
                "The validation error should be for the email field");
    }

    @Test
    public void testLoginRequestValidation_PasswordBlank() {
        // Arrange: Create a LoginRequest with a blank password
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword(""); // Blank password

        // Act: Validate the object
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert: Ensure a validation error is present for the password field
        assertFalse(violations.isEmpty(), "Validation errors should be present for blank password");
        assertEquals(1, violations.size(), "There should be one validation error for blank password");
        ConstraintViolation<LoginRequest> violation = violations.iterator().next();
        assertEquals("password", violation.getPropertyPath().toString(), 
                "The validation error should be for the password field");
    }

    @Test
    public void testLoginRequestValidation_EmailAndPasswordBlank() {
        // Arrange: Create a LoginRequest with both email and password blank
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(""); // Blank email
        loginRequest.setPassword(""); // Blank password

        // Act: Validate the object
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert: Ensure validation errors are present for both fields
        assertFalse(violations.isEmpty(), "Validation errors should be present for blank email and password");
        assertEquals(2, violations.size(), "There should be two validation errors for blank email and password");
    }
}
