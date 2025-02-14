package com.openclassrooms.starterjwt.unit.payload.response;

import org.junit.jupiter.api.Test;

import com.openclassrooms.starterjwt.payload.response.JwtResponse;

import static org.assertj.core.api.Assertions.assertThat;

class JwtResponseTest {

    @Test
    void jwtResponse_ShouldSetAllFieldsCorrectly() {
        // GIVEN: Create an instance of JwtResponse with initial values
        JwtResponse jwtResponse = new JwtResponse("initialToken", 2L, "initialUser", "InitialFirst", "InitialLast", false);

        // WHEN: Modify values using setters
        jwtResponse.setToken("testToken");
        jwtResponse.setId(1L);
        jwtResponse.setType("Bearer");
        jwtResponse.setUsername("testUser");
        jwtResponse.setFirstName("John");
        jwtResponse.setLastName("Doe");
        jwtResponse.setAdmin(true);

        // THEN: Verify that all values were updated correctly
        assertThat(jwtResponse.getToken()).isEqualTo("testToken");
        assertThat(jwtResponse.getType()).isEqualTo("Bearer");
        assertThat(jwtResponse.getId()).isEqualTo(1L);
        assertThat(jwtResponse.getUsername()).isEqualTo("testUser");
        assertThat(jwtResponse.getFirstName()).isEqualTo("John");
        assertThat(jwtResponse.getLastName()).isEqualTo("Doe");
        assertThat(jwtResponse.getAdmin()).isEqualTo(true);
    }
}