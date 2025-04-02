package org.ecommerce.UserContext.UnitTests.domain;

import org.ecommerce.user.domain.exception.InvalidEmailException;
import org.ecommerce.user.domain.model.value_objects.Email;
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
    @ValueSource( strings = {"", "    "})
    void constructor_ShouldThrowException_IfEmailIsNullOrEmpty(String invalidAddress) {
        // Arrange

        // Act
        Exception exception = assertThrows(InvalidEmailException.class, () -> new Email(invalidAddress));

        // Assert
        assertEquals("Email cannot be null or empty", exception.getMessage());
    }
    @ParameterizedTest
    @ValueSource( strings = {"test@.com", "@test.com", "testtest.com", "test.com@gmail", "test@test", "test@test.c"})
    void constructor_ShouldThrowException_IfEmailFormatIsNotValid(String invalidAddress) {
        // Arrange

        // Act
        Exception exception = assertThrows(InvalidEmailException.class, () -> new Email(invalidAddress));

        // Assert
        assertEquals("Invalid email format", exception.getMessage());
    }
    @ParameterizedTest
    @ValueSource( strings = {"test@test.com", "test@t.co", "t@t.co", "-@t.coom"})
    void isValidEmail_ShouldDoNothing_IfEmailFormatIsValid(String validAddress) throws NoSuchMethodException {
        // Arrange
        Email email = new Email();
        Method isValidAddress = Email.class.getDeclaredMethod("isValidEmail", String.class);
        isValidAddress.setAccessible(true);

        // Act

        // Assert
        assertDoesNotThrow(() -> isValidAddress.invoke(email, validAddress));
    }
    @ParameterizedTest
    @NullSource
    @ValueSource( strings = {"", "    "})
    void isValidEmail_ShouldThrowException_IfEmailIsNullOrEmpty(String InvalidAddress) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        Email email = new Email();
        Method isValidAddress = Email.class.getDeclaredMethod("isValidEmail", String.class);
        isValidAddress.setAccessible(true);

        // Act
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> isValidAddress.invoke(email, InvalidAddress));

        // Assert
        // Extract the cause of the InvocationTargetException
        assertTrue(exception.getCause() instanceof InvalidEmailException);
        assertEquals("Email cannot be null or empty", exception.getCause().getMessage());
    }
    @ParameterizedTest
    @ValueSource( strings = {"test@.com", "@test.com", "testtest.com", "test.com@gmail", "test@test", "test@test.c"})
    void isValidEmail_ShouldThrowException_IfEmailFormatIsNotValid(String InvalidAddress) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        Email email = new Email();
        Method isValidAddress = Email.class.getDeclaredMethod("isValidEmail", String.class);
        isValidAddress.setAccessible(true);

        // Act
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> isValidAddress.invoke(email, InvalidAddress));

        // Assert
        assertTrue(exception.getCause() instanceof InvalidEmailException);
        assertEquals("Invalid email format", exception.getCause().getMessage());
    }
}
