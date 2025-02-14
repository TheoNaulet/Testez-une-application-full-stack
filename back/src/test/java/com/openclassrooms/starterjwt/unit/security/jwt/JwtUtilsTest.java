package com.openclassrooms.starterjwt.unit.security.jwt;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.test.util.ReflectionTestUtils;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;

import io.jsonwebtoken.*;

public class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils; // Injects the mock into JwtUtils

    @Value("${oc.app.jwtSecret}")
    private String jwtSecret = "testSecret"; // Secret key for signing JWTs

    @Value("${oc.app.jwtExpirationMs}")
    private int jwtExpirationMs = 3600000; // Expiration time (1 hour)

    private String validToken;
    private String expiredToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes mocks

        // Manually inject values into JwtUtils using ReflectionTestUtils
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecret");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000);

        // Generate a valid JWT token
        validToken = Jwts.builder()
            .setSubject("testUser") // Sets the subject (username)
            .setIssuedAt(new Date()) // Sets issued date
            .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Sets expiration (1 hour from now)
            .signWith(SignatureAlgorithm.HS512, "testSecret") // Signs with secret key
            .compact();

        // Generate an expired JWT token
        expiredToken = Jwts.builder()
            .setSubject("testUser")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() - 1000)) // Already expired
            .signWith(SignatureAlgorithm.HS512, "testSecret")
            .compact();
    }

    @Test
    void testValidateJwtToken_InvalidSignature() {
        // Arrange: Modify the valid token to make it invalid
        String invalidToken = validToken + "invalidPart";

        // Act & Assert: Validation should fail due to an invalid signature
        assertFalse(jwtUtils.validateJwtToken(invalidToken), "Token with invalid signature should not be valid");
    }

    @Test
    void testValidateJwtToken_MalformedJwtException() {
        // Act & Assert: Validation should fail for a malformed JWT
        assertFalse(jwtUtils.validateJwtToken("this.is.not.a.jwt"), "Malformed token should not be valid");
    }

    @Test
    void testValidateJwtToken_ExpiredJwtException() {
        // Act & Assert: Validation should fail for an expired token
        assertFalse(jwtUtils.validateJwtToken(expiredToken), "Expired token should not be valid");
    }

    @Test
    void testValidateJwtToken_IllegalArgumentException() {
        // Act & Assert: Validation should fail for an empty or null token
        assertFalse(jwtUtils.validateJwtToken(""), "Empty token should not be valid");
        assertFalse(jwtUtils.validateJwtToken(null), "Null token should not be valid");
    }
}
