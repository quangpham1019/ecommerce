package org.ecommerce.UserContext.UnitTests.domain;
import org.ecommerce.user.domain.service.RoleDomainService;
import org.ecommerce.user.infrastructure.repository.jpa.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleDomainServiceTest {

    @Mock
    private RoleRepository roleRepository;

    private RoleDomainService roleDomainService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roleDomainService = new RoleDomainService(roleRepository);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ADMIN", "USER", "Moderator", "Buyer"})
    void validateRoleName_ShouldNotThrowException_WhenRoleNameIsValid(String validRoleName) {
        // Arrange

        // Act & Assert
        assertDoesNotThrow(() -> roleDomainService.validateRoleName(validRoleName));
    }
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "    "})
    void validateRoleName_ShouldThrowException_WhenRoleNameIsNullOrEmpty(String invalidRoleName) {
        // Arrange

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> roleDomainService.validateRoleName(invalidRoleName));
        assertEquals("Role name cannot be null or empty", exception.getMessage());
    }
}

