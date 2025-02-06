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
    private JwtUtils jwtUtils;

    @Value("${oc.app.jwtSecret}")
    private String jwtSecret = "testSecret";

    @Value("${oc.app.jwtExpirationMs}")
    private int jwtExpirationMs = 3600000; // 1 heure

    private String validToken;
    private String expiredToken;

    @BeforeEach
void setUp() {
    MockitoAnnotations.openMocks(this);

    // Injecter les valeurs manuellement
    ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecret");
    ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000);

    // Générer un token valide
    validToken = Jwts.builder()
        .setSubject("testUser")
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 3600000))
        .signWith(SignatureAlgorithm.HS512, "testSecret")
        .compact();

    // Générer un token expiré
    expiredToken = Jwts.builder()
        .setSubject("testUser")
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() - 1000)) // Déjà expiré
        .signWith(SignatureAlgorithm.HS512, "testSecret")
        .compact();
}


    @Test
    void testValidateJwtToken_InvalidSignature() {
        String invalidToken = validToken + "invalidPart";
        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }

    @Test
    void testValidateJwtToken_MalformedJwtException() {
        assertFalse(jwtUtils.validateJwtToken("this.is.not.a.jwt"));
    }

    @Test
    void testValidateJwtToken_ExpiredJwtException() {
        assertFalse(jwtUtils.validateJwtToken(expiredToken));
    }

    @Test
    void testValidateJwtToken_IllegalArgumentException() {
        assertFalse(jwtUtils.validateJwtToken(""));
        assertFalse(jwtUtils.validateJwtToken(null));
    }
}
