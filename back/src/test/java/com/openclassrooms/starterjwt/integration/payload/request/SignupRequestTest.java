package com.openclassrooms.starterjwt.integration.payload.request;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.payload.request.SignupRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SignupRequestTest {

    @Test
    void testLombokGeneratedMethods() {
        // Arrange
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("test@example.com");
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("test@example.com");
        signupRequest2.setFirstName("John");
        signupRequest2.setLastName("Doe");
        signupRequest2.setPassword("password123");

        SignupRequest signupRequest3 = new SignupRequest();
        signupRequest3.setEmail("different@example.com");
        signupRequest3.setFirstName("John");
        signupRequest3.setLastName("Doe");
        signupRequest3.setPassword("password123");

        // Act & Assert
        // Test toString()
        assertNotNull(signupRequest1.toString(), "toString() should not return null");

        // Test equals()
        assertEquals(signupRequest1, signupRequest2, "Two SignupRequest objects with the same values should be equal");
        assertNotEquals(signupRequest1, signupRequest3, "Two SignupRequest objects with different values should not be equal");

        // Test hashCode()
        assertEquals(signupRequest1.hashCode(), signupRequest2.hashCode(), "Two equal SignupRequest objects should have the same hashCode");
        assertNotEquals(signupRequest1.hashCode(), signupRequest3.hashCode(), "Two different SignupRequest objects should have different hashCodes");

        // Test equals() with null
        assertNotEquals(signupRequest1, null, "A SignupRequest object should not be equal to null");

        // Test equals() with a different class
        assertNotEquals(signupRequest1, new Object(), "A SignupRequest object should not be equal to an object of a different class");

        // Test hashCode() consistency
        assertEquals(signupRequest1.hashCode(), signupRequest1.hashCode(), "hashCode() should be consistent");
    }

    @Test
    void testEqualsSameInstance() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        assertEquals(signupRequest, signupRequest, "An object should be equal to itself");
    }

    @Test
    void testEqualsWithNullField() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail(null); 
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail(null); 
        signupRequest2.setFirstName("John");
        signupRequest2.setLastName("Doe");
        signupRequest2.setPassword("password123");

        assertEquals(signupRequest1, signupRequest2, "Two SignupRequest objects with the same values including null should be equal");
    }

    @Test
    void testEqualsDifferentClass() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        Object otherObject = new Object();

        assertNotEquals(signupRequest, otherObject, "A SignupRequest object should not be equal to an object of a different class");
    }

    @Test
    void testHashCodeWithNullField() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail(null); 
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail(null); 

        signupRequest2.setFirstName("John");
        signupRequest2.setLastName("Doe");
        signupRequest2.setPassword("password123");

        assertEquals(signupRequest1.hashCode(), signupRequest2.hashCode(), "Two objects with the same values including null should have the same hashCode");
    }

    @Test
    void testHashCodeConsistency() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        int hashCode1 = signupRequest.hashCode();
        int hashCode2 = signupRequest.hashCode();
        
        assertEquals(hashCode1, hashCode2, "hashCode() should be consistent for the same object");
    }

    @Test
    void testEqualsWithOneNullField() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail(null);
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("test@example.com"); // Non-null
        signupRequest2.setFirstName("John");
        signupRequest2.setLastName("Doe");
        signupRequest2.setPassword("password123");

        assertNotEquals(signupRequest1, signupRequest2, "Objects with one null field and one non-null field should not be equal");
    }

    @Test
    void testEqualsWithMultipleDifferentFields() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("test@example.com");
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("different@example.com");
        signupRequest2.setFirstName("Jane"); 
        signupRequest2.setLastName("Smith");
        signupRequest2.setPassword("password456");

        assertNotEquals(signupRequest1, signupRequest2, "Objects with multiple different fields should not be equal");
    }

    @Test
    void testHashCodeWithPartialData() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("test@example.com");
        signupRequest1.setFirstName(null); 
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("test@example.com");
        signupRequest2.setFirstName("John"); 
        signupRequest2.setLastName("Doe");
        signupRequest2.setPassword("password123");

        assertNotEquals(signupRequest1.hashCode(), signupRequest2.hashCode(), "Objects with partial null fields should have different hashCodes");
    }


    @Test
    void testEqualsWithNullObject() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        assertFalse(signupRequest.equals(null), "An object should not be equal to null");
    }

    @Test
    void testEqualsWithDifferentType() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        String differentTypeObject = "I'm a string";

        assertNotEquals(signupRequest, differentTypeObject, "An object should not be equal to an instance of a different class");
    }


    @Test
    void testEqualsWithDifferentPassword() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("test@example.com");
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("test@example.com"); 
        signupRequest2.setFirstName("John");
        signupRequest2.setLastName("Doe");
        signupRequest2.setPassword("differentPassword");

        assertNotEquals(signupRequest1, signupRequest2, "Objects with a different password should not be equal");
    }

    @Test
    void testEqualsWithDifferentFirstName() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("test@example.com");
        signupRequest1.setFirstName(null); 
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("test@example.com");
        signupRequest2.setFirstName("John"); 
        signupRequest2.setLastName("Doe");
        signupRequest2.setPassword("password123");

        assertNotEquals(signupRequest1, signupRequest2, "Objects with one null firstName and one non-null firstName should not be equal");
    }

    @Test
    void testEqualsWithAllNullFields() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail(null);
        signupRequest1.setFirstName(null);
        signupRequest1.setLastName(null);
        signupRequest1.setPassword(null);

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail(null);
        signupRequest2.setFirstName(null);
        signupRequest2.setLastName(null);
        signupRequest2.setPassword(null);

        assertEquals(signupRequest1, signupRequest2, "Two objects with all null fields should be equal");
    }

    @Test
    void testEqualsWithDifferentLastName() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("test@example.com");
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName(null); 
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("test@example.com");
        signupRequest2.setFirstName("John");
        signupRequest2.setLastName("Doe");
        signupRequest2.setPassword("password123");

        assertNotEquals(signupRequest1, signupRequest2, "Objects with one null lastName and one non-null lastName should not be equal");
    }

    @Test
    void testEqualsWithOneEmptyOneFilled() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("test@example.com");
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();

        assertNotEquals(signupRequest1, signupRequest2, "A fully filled object should not be equal to an empty object");
    }

    @Test
    void testHashCodeWithAllNullFields() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail(null);
        signupRequest1.setFirstName(null);
        signupRequest1.setLastName(null);
        signupRequest1.setPassword(null);

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail(null);
        signupRequest2.setFirstName(null);
        signupRequest2.setLastName(null);
        signupRequest2.setPassword(null);

        assertEquals(signupRequest1.hashCode(), signupRequest2.hashCode(), "Objects with all null fields should have the same hashCode");
    }
}