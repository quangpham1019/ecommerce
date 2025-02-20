package org.ecommerce.UnitTests;

import org.ecommerce.user.application.service.UserApplicationService;
import org.ecommerce.user.domain.model.Role;
import org.ecommerce.user.domain.model.User;
import org.ecommerce.user.domain.model.UserRole;
import org.ecommerce.user.domain.model.UserRoleStatus;
import org.ecommerce.user.domain.service.UserDomainService;
import org.ecommerce.user.infrastructure.repository.jpa.RoleRepository;
import org.ecommerce.user.infrastructure.repository.jpa.UserRepository;
import org.ecommerce.user.infrastructure.repository.jpa.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserApplicationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private UserDomainService userDomainService;

    @InjectMocks
    private UserApplicationService userApplicationService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void getUsers_ShouldReturnAllUsers() {
        // Arrange
        List<User> users = Arrays.asList(
                new User("jim", "jimPass", "jim@gmail.com"),
                new User("kyle", "kylePass", "kyle@gmail.com"),
                new User("yamal", "yamalPass", "yamal@gmail.com")
        );
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> actualUsers = userApplicationService.getUsers();

        // Assert
        assertEquals(3, actualUsers.size());
        assertEquals("kyle", actualUsers.get(1).getUsername());
        assertEquals("yamalPass", actualUsers.get(2).getPassword());
        verify(userRepository).findAll();
    }

    @Test
    void registerUser_ShouldRegisterUser_WhenEmailIsUnique() {
        // Arrange
        User newUser = new User("jim", "jimPass", "jim@gmail.com");
        when(userRepository.save(newUser)).thenReturn(newUser);
        doNothing().when(userDomainService).validateUniqueEmail(newUser.getEmail());

        // Act
        User registeredUser = userApplicationService.registerUser(newUser);

        // Assert
        verify(userRepository).save(newUser);
    }

    @Test
    void registerUser_ShouldReturnRegisteredUser_WhenEmailIsUnique() {

        // Arrange
        User newUser = new User("jim", "jimPass", "jim@gmail.com");
        when(userRepository.save(newUser)).thenReturn(newUser);

        // Act
        User registeredUser = userApplicationService.registerUser(newUser);

        // Assert
        assertEquals("jim", registeredUser.getUsername());
        assertEquals("jimPass", registeredUser.getPassword());
        assertEquals("jim@gmail.com", registeredUser.getEmail());
        verify(userDomainService).validateUniqueEmail(newUser.getEmail());
        verify(userRepository).save(newUser);
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailAlreadyExists() {
        // Arrange
        String existingEmail = "jim@gmail.com";
        User newUser = new User("jim", "jimPass", existingEmail);
        doThrow(new IllegalArgumentException("Email is already in use")).when(userDomainService).validateUniqueEmail(existingEmail);

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userApplicationService.registerUser(newUser));

        // Assert
        assertEquals("Email is already in use", exception.getMessage());
        verify(userDomainService).validateUniqueEmail(newUser.getEmail());
        verify(userRepository, never()).save(newUser);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   "})
    void registerUser_ShouldThrowException_WhenEmailIsNullOrEmpty(String invalidEmail) {
        // Arrange
        User newUser = new User("jim", "jimPass", invalidEmail);
        doThrow(new IllegalArgumentException("Email cannot be null or empty")).when(userDomainService).validateUniqueEmail(invalidEmail);

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userApplicationService.registerUser(newUser));

        // Assert
        assertEquals("Email cannot be null or empty", exception.getMessage());
        verify(userDomainService).validateUniqueEmail(invalidEmail);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUsers_ShouldReturnUsers_WhenEmailsAreUnique() {
        // Arrange
        List<User> users = List.of(
                new User("user1", "user1Pass", "user1@gmail.com"),
                new User("user2", "user2Pass", "user2@gmail.com")
        );
        when(userRepository.saveAll(users)).thenReturn(users);

        // Act
        List<User> result = userApplicationService.registerUsers(users);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userDomainService).validateUniqueEmail(users);
        verify(userRepository).saveAll(users);
    }
    @Test
    void registerUsers_ShouldThrowException_WhenEmailsAreNullOrEmpty() {}
    @Test
    void registerUsers_ShouldThrowException_WhenAnyEmailHasDuplicate() {}

    @Test
    void deleteById_ShouldDeleteUser_WhenUserIsFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        // Act
        userApplicationService.deleteById(userId);

        // Assert
        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void deleteById_ShouldThrowException_WhenUserDoesNotExist() {
        // Arrange
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userApplicationService.deleteById(userId));

        // Assert
        assertEquals("User not found", exception.getMessage());
        verify(userRepository).existsById(userId);
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    void updatePartial_shouldReturnUpdatedUser_WhenUserIsFound() {
        // Arrange
        Long userId = 1L;
        User existingUser = new User("old", "oldPassword", "old@email.com");
        User updatedUser = new User(null, "newPassword", null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act
        User result = userApplicationService.updatePartial(userId, updatedUser);

        // Assert
        assertNotNull(result);
        assertEquals("old", result.getUsername());
        assertEquals("newPassword", result.getPassword());
        assertEquals("old@email.com", result.getEmail());
        verify(userRepository).findById(userId);
    }
    @Test
    void updatePartial_shouldThrowException_WhenUserDoesNotExist() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userApplicationService.updatePartial(userId, new User()));

        // Assert
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void addRoleToUser_ShouldSaveAndReturnNewActiveUserRole_WhenUserIsFound_AndRoleIsFound() {
        // Arrange
        Long userId = 1L;
        Long roleId = 1L;
        User user = new User("John", "johnPassword", "john@example.com");
        Role role = new Role("ADMIN", "perform admin-related tasks");
        UserRole expectedResult = new UserRole(user, role, UserRoleStatus.ACTIVE);

        when(userRoleRepository.findByUser_IdAndRole_Id(userId, roleId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(userRoleRepository
                .save(argThat(userRole ->
                    userRole.getUser().equals(user) &&
                    userRole.getRole().equals(role) &&
                    userRole.getStatus().equals(UserRoleStatus.ACTIVE))))
                .thenReturn(expectedResult);

        // Act
        UserRole actualResult = userApplicationService.addRoleToUser(userId, roleId);

        // Assert
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
        assertEquals(UserRoleStatus.ACTIVE, actualResult.getStatus());

        verify(userRoleRepository).findByUser_IdAndRole_Id(userId, roleId);
        verify(userRepository).findById(userId);
        verify(roleRepository).findById(roleId);
        verify(userRoleRepository).save(actualResult);
    }
    @Test
    void addRoleToUser_ShouldThrowException_WhenUserIsNotFound() {
        // Arrange
        Long userId = 1L;
        Long roleId = 1L;
        when(userRoleRepository.findByUser_IdAndRole_Id(userId, roleId)).thenReturn(Optional.empty());

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userApplicationService.addRoleToUser(userId, roleId));

        // Assert
        assertEquals("User not found", exception.getMessage());
    }
    @Test
    void addRoleToUser_ShouldThrowException_WhenRoleIsNotFound() {
        // Arrange
        Long userId = 1L;
        Long roleId = 1L;
        when(userRoleRepository.findByUser_IdAndRole_Id(userId, roleId)).thenReturn(Optional.empty());

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userApplicationService.addRoleToUser(userId, roleId));

        // Assert
        assertEquals("Role not found", exception.getMessage());
    }
    @Test
    void addRoleToUser_ShouldThrowException_WhenUserAlreadyHasRole_AndRoleIsActive() {
        // Arrange
        Long userId = 1L;
        Long roleId = 2L;

        UserRole existingRole = new UserRole(
                null, null, UserRoleStatus.ACTIVE);

        when(userRoleRepository.findByUser_IdAndRole_Id(userId, roleId)).thenReturn(Optional.of(existingRole));

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userApplicationService.addRoleToUser(userId, roleId));

        // Assert
        assertEquals("User already has this role", exception.getMessage());
    }

    @Test
    void addRoleToUser_ShouldUpdateRoleStatusToActive_WhenUserAlreadyHasRole_AndRoleIsInactive() {
        // Arrange
        Long userId = 1L;
        Long roleId = 2L;

        UserRole existingRole = new UserRole(
                null, null, UserRoleStatus.INACTIVE);

        when(userRoleRepository.findByUser_IdAndRole_Id(userId, roleId)).thenReturn(Optional.of(existingRole));

        // Act
        UserRole updatedUserRole = userApplicationService.addRoleToUser(userId, roleId);

        // Assert
        assertEquals(UserRoleStatus.ACTIVE, existingRole.getStatus());
        assertEquals(UserRoleStatus.ACTIVE, updatedUserRole.getStatus());
        assertEquals(existingRole, updatedUserRole);
    }

    @Test
    void removeRoleFromUser_ShouldSetRoleToInactive_WhenUserHasRole_AndRoleIsActive() {
        // Arrange
        Long userId = 1L;
        Long roleId = 2L;
        UserRole existingRole = spy(new UserRole(
                null,
                null,
                UserRoleStatus.ACTIVE));

        when(userRoleRepository.findByUser_IdAndRole_Id(userId, roleId)).thenReturn(Optional.of(existingRole));

        // Act
        userApplicationService.removeRoleFromUser(userId, roleId);

        // Assert
        assertEquals(UserRoleStatus.INACTIVE, existingRole.getStatus());
        verify(userRoleRepository).findByUser_IdAndRole_Id(userId, roleId);
        verify(existingRole).deactivate();
    }
    @Test
    void removeRoleFromUser_ShouldThrowException_WhenUserDoesNotHaveRole() {
        // Arrange
        Long userId = 1L;
        Long roleId = 2L;

        when(userRoleRepository.findByUser_IdAndRole_Id(userId, roleId)).thenReturn(Optional.empty());

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userApplicationService.removeRoleFromUser(userId, roleId));

        // Assert
        assertEquals("User does not have this role", exception.getMessage());
    }
    @Test
    void removeRoleFromUser_ShouldThrowException_WhenUserHasRole_AndRoleIsInactive() {
        // Arrange
        Long userId = 1L;
        Long roleId = 2L;
        UserRole existingRole = spy(new UserRole(
                null,
                null,
                UserRoleStatus.INACTIVE));

        when(userRoleRepository.findByUser_IdAndRole_Id(userId, roleId)).thenReturn(Optional.of(existingRole));

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class,
                ()-> userApplicationService.removeRoleFromUser(userId, roleId));

        // Assert
        assertEquals("The role is already inactive and cannot be removed", exception.getMessage());
        assertEquals(UserRoleStatus.INACTIVE, existingRole.getStatus());
        verify(userRoleRepository).findByUser_IdAndRole_Id(userId, roleId);
        verify(existingRole, never()).deactivate();
    }

    @Test
    void getUserRoles_shouldReturnAllUserRoles() {
        // Arrange
        List<UserRole> expectedResult = List.of(
                new UserRole(),
                new UserRole());

        when(userRoleRepository.findAll()).thenReturn(expectedResult);

        // Act
        List<UserRole> actualResult = userApplicationService.getUserRoles();

        // Assert
        assertNotNull(actualResult);
        assertEquals(2, actualResult.size());
        verify(userRoleRepository).findAll();
    }
    @Test
    void getUserRolesByUserId_shouldReturnAllRolesOfUser_WhenUserIsFound() {
        // Arrange
        Long userId = 1L;
        List<UserRole> expectedResult = List.of(
                new UserRole(),
                new UserRole());

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRoleRepository.findByUser_Id(userId)).thenReturn(expectedResult);

        // Act
        List<UserRole> actualResult = userApplicationService.getUserRolesByUserId(userId);

        // Assert
        assertNotNull(actualResult);
        assertEquals(2, actualResult.size());
        verify(userRepository).existsById(userId);
        verify(userRoleRepository).findByUser_Id(userId);
    }
    @Test
    void getUserRolesByUserId_ShouldThrowException_WhenUserIsNotFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userApplicationService.getUserRolesByUserId(userId));

        // Assert
        assertEquals("User not found", exception.getMessage());
        verify(userRepository).existsById(userId);
        verify(userRoleRepository, never()).findByUser_Id(userId);
    }
}
