package org.ecommerce.UserContext.UnitTests.domain;

import org.ecommerce.user.domain.model.User;
import org.ecommerce.user.domain.model.value_objects.Email;
import org.ecommerce.user.domain.service.UserDomainService;
import org.ecommerce.user.infrastructure.repository.jpa.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserDomainServiceTest {


    @Mock
    private UserRepository userRepository;

    private UserDomainService userDomainService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDomainService = new UserDomainService(userRepository);
    }

    @Test
    void validateUniqueEmail_ShouldNotThrowException_WhenEmailIsUnique() {
        // Arrange
        Email email = new Email("unique@example.com");
        when(userRepository.existsByEmail_Address(email.getAddress())).thenReturn(false);

        // Act & Assert
        assertDoesNotThrow(() -> userDomainService.validateUniqueEmail(email));
    }

    @Test
    void validateUniqueEmail_ShouldThrowException_WhenEmailIsDuplicateInRepository() {
        // Arrange
        Email email = new Email("duplicate@example.com");
        when(userRepository.existsByEmail_Address(email.getAddress())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userDomainService.validateUniqueEmail(email));
        assertEquals("Email is already in use", exception.getMessage());
    }

    @Test
    void validateUniqueEmail_ShouldThrowException_WhenDuplicateEmailsExistInList() {
        // Arrange
        User user1 = new User("user1", "password", new Email("user1@example.com"));
        User user2 = new User("user2", "password", new Email("user1@example.com")); // Duplicate email
        List<User> users = List.of(user1, user2);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userDomainService.validateUniqueEmail(users));
        assertEquals("There are duplicate emails in list.", exception.getMessage());
    }

    @Test
    void validateUniqueEmail_ShouldNotThrowException_WhenEmailsAreUniqueInList() {
        // Arrange
        User user1 = new User("user1", "password", new Email("user1@example.com"));
        User user2 = new User("user2", "password", new Email("user2@example.com"));
        List<User> users = List.of(user1, user2);
        when(userRepository.findEmailsByEmails(Set.of("user1@example.com", "user2@example.com"))).thenReturn(List.of());

        // Act & Assert
        assertDoesNotThrow(() -> userDomainService.validateUniqueEmail(users));
    }

    @Test
    void validateUniqueEmail_ShouldThrowException_WhenDuplicateEmailsExistInListAndRepository() {
        // Arrange
        User user1 = new User("user1", "password", new Email("user1@example.com"));
        User user2 = new User("user2", "password", new Email("user2@example.com"));
        List<User> users = List.of(user1, user2);
        when(userRepository.findEmailsByEmails(Set.of("user1@example.com", "user2@example.com")))
                .thenReturn(List.of("user2@example.com"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userDomainService.validateUniqueEmail(users));
        assertEquals("Duplicate emails: [user2@example.com]", exception.getMessage());
    }
}
