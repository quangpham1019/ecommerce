package org.ecommerce.UnitTests.service;

import org.ecommerce.user.application.service.RoleApplicationService;
import org.ecommerce.user.domain.model.Role;
import org.ecommerce.user.domain.service.RoleDomainService;
import org.ecommerce.user.infrastructure.repository.jpa.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleApplicationServiceTest {

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RoleDomainService roleDomainService;

    @InjectMocks
    private RoleApplicationService roleApplicationService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getRoles_ShouldReturnAllRoles() {
        // Arrange
        List<Role> mockRoles = Arrays.asList(
                new Role("ADMIN", "perform admin-related tasks"),
                new Role("USER", "restricted to user-level tasks"));
        when(roleRepository.findAll()).thenReturn(mockRoles);

        // Act
        List<Role> result = roleApplicationService.getRoles();

        // Assert
        assertEquals(2, result.size());
        assertEquals("ADMIN", result.get(0).getRoleName());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void addRole_ShouldSaveRole_WhenRoleDoesNotExist() {
        // Arrange
        Role newRole = new Role("MANAGER", "Perform managerial tasks");
        when(roleRepository.existsByRoleName("MANAGER")).thenReturn(false);
        when(roleRepository.save(newRole)).thenReturn(newRole);

        // Act
        Role savedRole = roleApplicationService.addRole(newRole);

        // Assert
        assertNotNull(savedRole);
        assertEquals("MANAGER", savedRole.getRoleName());
        verify(roleRepository, times(1)).existsByRoleName("MANAGER");
        verify(roleRepository, times(1)).save(newRole);
    }

    @Test
    void addRole_ShouldThrowException_WhenRoleAlreadyExists() {
        // Arrange
        Role existingRole = new Role("ADMIN", "perform admin-related tasks");
        when(roleRepository.existsByRoleName("ADMIN")).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> roleApplicationService.addRole(existingRole));

        assertEquals("Role already exists", exception.getMessage());
        verify(roleRepository, times(1)).existsByRoleName("ADMIN");
        verify(roleRepository, never()).save(any());
    }


    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   "})
    void addRole_ShouldThrowException_WhenRoleNameIsInvalid(String invalidRoleName) {
        // Arrange
        Role invalidRole = new Role(invalidRoleName, "role with invalid name");
        doThrow(new IllegalArgumentException("Role name cannot be null or empty")).when(roleDomainService).validateRoleName(invalidRoleName);

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> roleApplicationService.addRole(invalidRole));

        // Assert
        assertEquals("Role name cannot be null or empty", exception.getMessage());
        verify(roleRepository, times(1)).existsByRoleName(any());
        verify(roleRepository, never()).save(any());
    }

    @Test
    void deleteRoleById_ShouldDeleteRole_WhenRoleExists() {
        // Arrange
        Role role = new Role("ADMIN", "perform admin-related tasks");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        // Act
        roleApplicationService.deleteRoleById(1L);

        // Assert
        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).delete(role);
    }

    @Test
    void deleteRoleById_ShouldThrowException_WhenRoleDoesNotExist() {
        // Arrange
        long invalidRoleId = 999L; // This role ID doesn't exist
        when(roleRepository.findById(invalidRoleId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> roleApplicationService.deleteRoleById(invalidRoleId));

        assertEquals("Role not found", exception.getMessage());
        verify(roleRepository, times(1)).findById(invalidRoleId);
        verify(roleRepository, never()).delete(any());
    }
}
