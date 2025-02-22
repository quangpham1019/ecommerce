package org.ecommerce.UnitTests.domain;

import org.ecommerce.user.domain.model.Role;
import org.ecommerce.user.domain.model.User;
import org.ecommerce.user.domain.model.UserRole;
import org.ecommerce.user.domain.model.UserRoleStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class UserUnitTest {

    @Test
    public void addRole_ShouldReturnNewActiveUserRole_WhenUserDoesNotHaveRole() {
        // Arrange
        User user = new User();
        Role role = new Role();
        UserRole expectedResult = new UserRole(user, role, UserRoleStatus.ACTIVE);
        Set<UserRole> currentUserRoleSet = Mockito.spy(new HashSet<>());
        user.setUserRoles(currentUserRoleSet);

        // Act
        UserRole actualResult = user.addRole(role);

        // Assert
        assertNotNull(actualResult);
        assertEquals(expectedResult.getUser(), actualResult.getUser());
        assertEquals(expectedResult.getRole(), actualResult.getRole());
        assertEquals(expectedResult.getStatus(), actualResult.getStatus());

        verify(currentUserRoleSet).add(any(UserRole.class));
    }
    @Test
    public void addRole_ShouldActivateAndReturnUserRole_WhenRoleIsInactive() {
        // Arrange
        User user = new User();
        Role role = new Role();
        UserRole existingRole = Mockito.spy(new UserRole(user, role, UserRoleStatus.INACTIVE));
        user.setUserRoles(new HashSet<>());
        user.getUserRoles().add(existingRole);

        UserRole expectedResult = new UserRole(user, role, UserRoleStatus.ACTIVE);

        // Act
        UserRole actualResult = user.addRole(role);

        // Assert
        assertNotNull(actualResult);
        assertEquals(expectedResult.getUser(), actualResult.getUser());
        assertEquals(expectedResult.getRole(), actualResult.getRole());
        assertEquals(expectedResult.getStatus(), actualResult.getStatus());

        verify(existingRole).activate();
    }
    @Test
    public void addRole_ShouldThrowException_WhenRoleIsAlreadyActive() {
        // Arrange
        User user = new User();
        Role role = new Role();
        UserRole existingRole = Mockito.spy(new UserRole(user, role, UserRoleStatus.ACTIVE));
        Set<UserRole> currentUserRoleSet = Mockito.spy(new HashSet<>());
        currentUserRoleSet.add(existingRole);
        user.setUserRoles(currentUserRoleSet);

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.addRole(role));

        // Assert
        assertEquals("User already has this role", exception.getMessage());

        verify(user.getUserRoles(), times(1)).add(any(UserRole.class));
        verify(existingRole, never()).activate();
    }
}
