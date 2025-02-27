package org.ecommerce.UnitTests.service;

import org.ecommerce.user.application.dto.userAddressDTO.UserAddressCreateDTO;
import org.ecommerce.user.application.dto.userAddressDTO.UserAddressResponseDTO;
import org.ecommerce.user.application.dto.userDTO.UserCreateDTO;
import org.ecommerce.user.application.dto.userDTO.UserProfileDTO;
import org.ecommerce.user.application.dto.userDTO.UserResponseDTO;
import org.ecommerce.user.application.mapper.interfaces.UserAddressMapper;
import org.ecommerce.user.application.mapper.interfaces.UserMapper;
import org.ecommerce.user.application.service.UserApplicationService;
import org.ecommerce.user.domain.model.*;
import org.ecommerce.user.domain.model.enums.UserRoleStatus;
import org.ecommerce.user.domain.model.value_objects.Email;
import org.ecommerce.user.domain.model.value_objects.UserStoredAddress;
import org.ecommerce.user.domain.service.UserDomainService;
import org.ecommerce.user.infrastructure.repository.jpa.RoleRepository;
import org.ecommerce.user.infrastructure.repository.jpa.UserRepository;
import org.ecommerce.user.infrastructure.repository.jpa.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
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
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserAddressMapper userAddressMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserApplicationService userApplicationService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void getUsers_ShouldReturnAllUsers() {
        // Arrange
        List<User> users = Arrays.asList(
                new User("jim", "jimPass", new Email("jim@gmail.com")),
                new User("kyle", "kylePass", new Email("kyle@gmail.com")),
                new User("yamal", "yamalPass", new Email("yamal@gmail.com"))
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
    void getUserProfile_ShouldReturnUserProfile_IfUserExists() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        List<String> roles = Arrays.asList("USER", "ADMIN");
        UserProfileDTO expectedResult = new UserProfileDTO(1L, "john", "john@gmail.com", roles);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toUserProfileDTO(user)).thenReturn(expectedResult);

        // Act
        UserProfileDTO acutualResult = userApplicationService.getUserProfile(userId);

        // Assert
        assertEquals("john", acutualResult.getUsername());
        assertEquals("john@gmail.com", acutualResult.getEmail());
        assertTrue(acutualResult.getRoleNames().containsAll(roles));
    }
    @Test
    void getUserProfile_ShouldThrowException_IfUserDoesNotExist() {
        // Arrange
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userApplicationService.getUserProfile(userId));

        // Assert
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void registerUser_ShouldRegisterAndReturnUser_WhenEmailIsUnique() {
        // Arrange
        UserCreateDTO newUserCreateDTO = new UserCreateDTO("jim", "jimPass", "jim@gmail.com");
        User newUser = new User("jim", "hashedJimPass", new Email("jim@gmail.com"));
        UserResponseDTO expectedUserResponseDTO = new UserResponseDTO(1L, "jim", "jim@gmail.com");

        doNothing().when(userDomainService).validateUniqueEmail(newUserCreateDTO.getEmail());
        when(passwordEncoder.encode(newUserCreateDTO.getPassword())).thenReturn("hashedJimPass");
        when(userMapper.toEntity(newUserCreateDTO)).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(newUser);
        when(userMapper.toResponseDto(newUser)).thenReturn(expectedUserResponseDTO);

        // Act
        UserResponseDTO actualUserResponseDTO = userApplicationService.registerUser(newUserCreateDTO);

        // Assert
        assertEquals(expectedUserResponseDTO, actualUserResponseDTO);
        verify(userDomainService).validateUniqueEmail(newUserCreateDTO.getEmail());
        verify(passwordEncoder).encode("jimPass");
        verify(userMapper).toEntity(newUserCreateDTO);
        verify(userMapper).toResponseDto(newUser);
        verify(userRepository).save(newUser);
    }
    @Test
    void registerUser_ShouldEncodePasswordAndSaveUser_WhenEmailIsUnique() {}
    @Test
    void registerUser_ShouldThrowException_WhenEmailAlreadyExists() {
        // Arrange
        Email existingEmail = new Email("jim@gmail.com");
        UserCreateDTO newUserCreateDTO = new UserCreateDTO("jim", "jimPass", existingEmail.getAddress());
        doThrow(new IllegalArgumentException("Email is already in use"))
                .when(userDomainService).validateUniqueEmail(existingEmail);

        // Act
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> userApplicationService.registerUser(newUserCreateDTO));

        // Assert
        assertEquals("Email is already in use", exception.getMessage());
        verify(userDomainService).validateUniqueEmail(newUserCreateDTO.getEmail());
        verify(userMapper, never()).toEntity(any(UserCreateDTO.class));
        verify(userMapper, never()).toResponseDto(any(User.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUsers_ShouldReturnUsers_WhenEmailsAreUnique() {
        // Arrange
        List<User> users = List.of(
                new User("user1", "user1Pass", new Email("user1@gmail.com")),
                new User("user2", "user2Pass", new Email("user2@gmail.com"))
        );
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
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
    void registerUsers_ShouldEncodePasswordAndSaveUsers_WhenEmailsAreUnique() {}
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
        User existingUser = new User("old", "oldPassword", new Email("old@email.com"));
        UserCreateDTO userCreateDTO = new UserCreateDTO(null, "newPassword", "new@email.com");
        UserResponseDTO expectedResult = new UserResponseDTO(1L, "old", "new@email.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userMapper.toResponseDto(existingUser)).thenReturn(expectedResult);

        // Act
        UserResponseDTO actualResult = userApplicationService.updatePartial(userId, userCreateDTO);

        // Assert
        assertNotNull(actualResult);
        assertEquals("old", actualResult.getUsername());
        assertEquals("new@email.com", actualResult.getEmail());
        verify(userRepository).findById(userId);
        verify(userMapper).toResponseDto(existingUser);
    }
    @Test
    void updatePartial_shouldThrowException_WhenUserDoesNotExist() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userApplicationService.updatePartial(userId, new UserCreateDTO()));

        // Assert
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void addRoleToUser_ShouldReturnActiveUserRole_WhenUserIsFound_AndRoleIsFound() {
        // Arrange
        Long userId = 1L;
        Long roleId = 1L;
        User user = Mockito.mock(User.class);
        Role role = Mockito.mock(Role.class);
        UserRole expectedResult = new UserRole(user, role, UserRoleStatus.ACTIVE);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(user.addRole(role)).thenReturn(expectedResult);

        // Act
        UserRole actualResult = userApplicationService.addRoleToUser(userId, roleId);

        // Assert
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
        assertEquals(UserRoleStatus.ACTIVE, actualResult.getStatus());

        verify(userRepository).findById(userId);
        verify(roleRepository).findById(roleId);
        verify(user).addRole(role);
    }
    @Test
    void addRoleToUser_ShouldThrowException_WhenUserIsNotFound() {
        // Arrange
        Long userId = 1L;
        Long roleId = 1L;

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

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userApplicationService.addRoleToUser(userId, roleId));

        // Assert
        assertEquals("Role not found", exception.getMessage());
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

    @Test
    void addAddressToUserAccount_ShouldReturnAddedAddress_WhenUserIsFound_AndUserDoesNotHaveAddress() {
        // Arrange
        User user = new User();
        UserStoredAddress userStoredAddress = new UserStoredAddress("123 Main St", "city", "state", "zip", "country");
        UserAddressCreateDTO addressCreateDTO = new UserAddressCreateDTO("john", userStoredAddress, null, false);
        UserAddress userAddress = new UserAddress(user, "john", userStoredAddress, null, false);
        UserAddressResponseDTO addressResponseDTO = new UserAddressResponseDTO("john", userStoredAddress, null, false);
        user.setUserAddressSet(new HashSet<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userAddressMapper.toUserAddress(addressCreateDTO)).thenReturn(userAddress);
        when(userAddressMapper.toUserAddressResponseDTO(userAddress)).thenReturn(addressResponseDTO);

        // Act
        UserAddressResponseDTO result = userApplicationService.addAddressToUserAccount(1L, addressCreateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userStoredAddress, result.getAddress());
        verify(userRepository).findById(1L);
        verify(userAddressMapper).toUserAddress(addressCreateDTO);
        verify(userAddressMapper).toUserAddressResponseDTO(userAddress);
    }

    @Test
    void addAddressToUserAccount_ShouldThrowException_WhenUserIsFound_AndUserAlreadyHasAddress() {
        // Arrange
        User user = new User();
        UserAddressCreateDTO addressCreateDTO = new UserAddressCreateDTO();
        UserAddress userAddress = new UserAddress();
        user.setUserAddressSet(new HashSet<>());

        user.addAddress(userAddress);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userAddressMapper.toUserAddress(addressCreateDTO)).thenReturn(userAddress);

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userApplicationService.addAddressToUserAccount(1L, addressCreateDTO);
        });

        // Assert
        assertEquals("Address already exists", exception.getMessage());
    }

    @Test
    void addAddressToUserAccount_ShouldThrowException_WhenUserIsNotFound() {
        // Arrange
        UserAddressCreateDTO addressCreateDTO = new UserAddressCreateDTO();

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userApplicationService.addAddressToUserAccount(1L, addressCreateDTO);
        });

        // Assert
        assertEquals("User not found", exception.getMessage());
    }
}
