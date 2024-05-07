package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    public void testValidUser() {
        // Given
        User user = new User("test@example.com", "Doe", "John", "password", false);

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidEmail() {
        // Given
        User user = new User("invalid-email", "Doe", "John", "password", false);

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("doit être une adresse électronique syntaxiquement correcte", violation.getMessage());
        assertEquals("email", violation.getPropertyPath().toString());
    }


}
