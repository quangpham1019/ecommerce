package org.ecommerce.UnitTests.domain;

import org.ecommerce.user.domain.model.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class EmailTest
{
    @ParameterizedTest
    @ValueSource( strings = {"test@test.com", "test@t.co", "t@t.co", "-@t.coom"})
    void constructor_ShouldSetEmailAddress_IfEmailIsNotNull_AndEmailFormatIsValid(String validAddress) {
        // Arrange

        // Act
        Email email = new Email(validAddress);

        // Assert
        assertEquals(validAddress, email.getAddress());
    }
    @ParameterizedTest
    @NullSource
    @ValueSource( strings = {"test@.com", "@test.com", "testtest.com", "test.com@gmail", "test@test", "test@test.c"})
    void constructor_ShouldThrowException_IfEmailIsNull_OrEmailFormatIsNotValid(String invalidAddress) {
        // Arrange

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Email(invalidAddress));

        // Assert
        assertEquals("Invalid email address", exception.getMessage());
    }
    @ParameterizedTest
    @ValueSource( strings = {"test@test.com", "test@t.co", "t@t.co", "-@t.coom"})
    void isValidEmail_ShouldReturnTrue_IfEmailFormatIsValid(String validAddress) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        Email email = new Email();
        Method isValidAddress = Email.class.getDeclaredMethod("isValidEmail", String.class);
        isValidAddress.setAccessible(true);

        // Act
        boolean isValid = (boolean) isValidAddress.invoke(email, validAddress);

        // Assert
        assertTrue(isValid);
    }
    @ParameterizedTest
    @ValueSource( strings = {"test@.com", "@test.com", "testtest.com", "test.com@gmail", "test@test", "test@test.c"})
    void isValidEmail_ShouldReturnFalse_IfEmailFormatIsNotValid(String InvalidAddress) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        Email email = new Email();
        Method isValidAddress = Email.class.getDeclaredMethod("isValidEmail", String.class);
        isValidAddress.setAccessible(true);

        // Act
        boolean isValid = (boolean) isValidAddress.invoke(email, InvalidAddress);

        // Assert
        assertFalse(isValid);
    }
}
