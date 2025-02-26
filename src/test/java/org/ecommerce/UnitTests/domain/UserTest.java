package org.ecommerce.UnitTests.domain;

import org.ecommerce.user.domain.model.Role;
import org.ecommerce.user.domain.model.User;
import org.ecommerce.user.domain.model.UserAddress;
import org.ecommerce.user.domain.model.UserRole;
import org.ecommerce.user.domain.model.enums.UserRoleStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserTest {

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

        UserRole curUserRole = Mockito.spy(new UserRole(user, role, UserRoleStatus.INACTIVE));
        Set<UserRole> currentUserRoleSet = new HashSet<>();
        currentUserRoleSet.add(curUserRole);
        user.setUserRoles(currentUserRoleSet);

        UserRole expectedResult = new UserRole(user, role, UserRoleStatus.ACTIVE);

        // Act
        UserRole actualResult = user.addRole(role);

        // Assert
        assertNotNull(actualResult);
        assertEquals(expectedResult.getUser(), actualResult.getUser());
        assertEquals(expectedResult.getRole(), actualResult.getRole());
        assertEquals(expectedResult.getStatus(), actualResult.getStatus());

        verify(curUserRole).activate();
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

        verify(currentUserRoleSet, times(1)).add(any(UserRole.class));
        verify(existingRole, never()).activate();
    }

    @Test
    public void getUserRoles_ShouldReturnSetOfActiveUserRoles() {
        // Arrange
        User user = new User();
        Role role1 = new Role("ADMIN", "admin");
        Role role2 = new Role("USER", "user");
        Role role3 = new Role("MOD", "mod");
        Set<UserRole> currentUserRoleSet = Set.of(
                new UserRole(user, role1, UserRoleStatus.ACTIVE),
                new UserRole(user, role2, UserRoleStatus.INACTIVE),
                new UserRole(user, role3, UserRoleStatus.INACTIVE)
        );
        user.setUserRoles(currentUserRoleSet);

        // Act
        Set<UserRole> result = user.getUserRoles();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    @Test
    public void getUserRoles_ShouldReturnNewSetOfActiveUserRoles() {
        // Arrange
        User user = new User();
        Set<UserRole> expectedResultSet = new HashSet<>();
        UserRole testUserRole = new UserRole(null, null, UserRoleStatus.ACTIVE);

        expectedResultSet.add(testUserRole);
        user.setUserRoles(expectedResultSet);

        // Act
        Set<UserRole> actualResultSet = user.getUserRoles();

        // Assert
        assertEquals(1, actualResultSet.size());
        assertEquals(expectedResultSet.iterator().next(), actualResultSet.iterator().next());
        assertFalse(expectedResultSet == actualResultSet);
    }


    @Test
    public void addAddress_ShouldAddNewAddress_WhenUserDoesNotHaveAddress() {
        // Arrange
        User user = new User();
        UserAddress userAddress = new UserAddress(user, "john", null, null, false);
        user.setUserAddressSet(new HashSet<>());

        // Act
        user.addAddress(userAddress);

        // Assert
        assertEquals(1, user.getUserAddressSet().size());
        assertTrue(user.getUserAddressSet().contains(userAddress));
    }

    @Test
    public void addAddress_ShouldUpdateDefaultAddressToNewAddress_WhenUserDoesNotHaveAddress_AndIsDefaultShippingIsTrue() {
        // Arrange
        User user = new User();
        UserAddress oldAddress = new UserAddress(user, "john", null, null, true);
        UserAddress userAddress = new UserAddress(user, "jim", null, null, true);

        Set<UserAddress> addressSet = new HashSet<>();
        addressSet.add(oldAddress);
        user.setUserAddressSet(addressSet);

        // Act
        user.addAddress(userAddress);

        // Assert
        assertFalse(oldAddress.isDefaultShipping());
        assertTrue(userAddress.isDefaultShipping());
        assertEquals(2, user.getUserAddressSet().size());
    }

    @Test
    public void addAddress_ShouldThrowException_WhenUserAlreadyHasAddress() {
        // Arrange
        User user = new User();
        UserAddress userAddress = new UserAddress(user, "john", null, null, true);
        Set<UserAddress> addressSet = new HashSet<>();
        addressSet.add(userAddress);
        user.setUserAddressSet(addressSet);

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.addAddress(userAddress);
        });

        // Assert
        assertEquals("Address already exists", exception.getMessage());
    }
}
