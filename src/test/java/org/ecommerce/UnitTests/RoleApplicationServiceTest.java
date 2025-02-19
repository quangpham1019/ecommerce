package org.ecommerce.UnitTests;

import org.ecommerce.user.application.service.RoleApplicationService;
import org.ecommerce.user.domain.model.Role;
import org.ecommerce.user.domain.service.RoleDomainService;
import org.ecommerce.user.infrastructure.repository.jpa.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleApplicationServiceTest {

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RoleDomainService roleDomainService;

    @InjectMocks
    private RoleApplicationService roleApplicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRoles_ShouldReturnAllRoles() {
        // Arrange
        List<Role> mockRoles = Arrays.asList(new Role("ADMIN", "perform admin-related tasks"), new Role("USER", "restricted to user-level tasks"));
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

    @Test
    void addRole_ShouldThrowException_WhenRoleNameIsNull() {
        // Arrange
        Role invalidRole = new Role(null, "null role");
        doThrow(new IllegalArgumentException("Role name cannot be null or empty")).when(roleDomainService).validateRoleName(null);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> roleApplicationService.addRole(invalidRole));

        assertEquals("Role name cannot be null or empty", exception.getMessage());
        verify(roleRepository, atMostOnce()).existsByRoleName(any());
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
    void deleteRoleById_ShouldThrowException_WhenRoleNotFound() {
        // Arrange
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> roleApplicationService.deleteRoleById(99L));

        assertEquals("Role not found", exception.getMessage());
        verify(roleRepository, times(1)).findById(99L);
        verify(roleRepository, never()).delete(any());
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
