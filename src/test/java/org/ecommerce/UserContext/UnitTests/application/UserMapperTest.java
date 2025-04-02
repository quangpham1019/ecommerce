package org.ecommerce.UserContext.UnitTests.application;

import org.ecommerce.user.application.dto.userDTO.UserCreateDTO;
import org.ecommerce.user.application.dto.userDTO.UserProfileDTO;
import org.ecommerce.user.application.dto.userDTO.UserResponseDTO;
import org.ecommerce.user.application.mapper.interfaces.UserMapper;
import org.ecommerce.user.domain.model.Role;
import org.ecommerce.user.domain.model.User;
import org.ecommerce.user.domain.model.UserRole;
import org.ecommerce.user.domain.model.enums.UserRoleStatus;
import org.ecommerce.user.domain.model.value_objects.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        // Initialize the userMapper instance by creating the Mapper
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void toResponseDto_ShouldMapUserToUserResponseDto() {
        // Arrange
        User expected = new User(
                "john_doe",
                "password123",
                new Email("john.doe@example.com"));

        // Act
        UserResponseDTO actual = userMapper.toResponseDto(expected);

        // Assert
        assertNotNull(actual);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getEmail().getAddress(), actual.getEmail());
    }

    @Test
    void toEntity_ShouldMapUserCreateDTOToUserEntity() {
        // Arrange
        UserCreateDTO dto = new UserCreateDTO(
                "john_doe",
                "password123",
                "john.doe@example.com");

        // Act
        User user = userMapper.toEntity(dto);

        // Assert
        assertNotNull(user);
        assertEquals(dto.getUsername(), user.getUsername());
        assertEquals(dto.getEmail().getAddress(), user.getEmail().getAddress());
    }

    @Test
    void toUserProfileDTO_ShouldMapUserToUserProfileDTO() {
        // Arrange
        User user = new User(
                "john_doe",
                "password123",
                new Email("john.doe@example.com"));
        Role role = new Role(
                "ADMIN",
                "Admin tasks");

        Set<UserRole> userRoles = new HashSet<>();
        UserRole existingRoles = new UserRole(user, role, UserRoleStatus.ACTIVE);
        userRoles.add(existingRoles);
        user.setUserRoles(userRoles);

        // Act
        UserProfileDTO profileDTO = userMapper.toUserProfileDTO(user);

        // Assert
        assertNotNull(profileDTO);
        assertTrue(profileDTO.getRoleNames().contains("ADMIN"));
    }

    @Test
    void mapUserRolesToRoleNames_ShouldMapUserRolesToRoleNames() {
        // Arrange
        Set<UserRole> roles = Set.of(new UserRole(
                new User(
                        "john_doe",
                        "password123",
                        new Email("john.doe@example.com")
                ),
                new Role(
                        "ADMIN",
                        "Admin tasks"
                ),
                UserRoleStatus.ACTIVE)
        );

        // Act
        List<String> roleNames = userMapper.mapUserRolesToRoleNames(roles);

        // Assert
        assertNotNull(roleNames);
        assertTrue(roleNames.contains("ADMIN"));
    }

    @Test
    void mapStringToEmail_ShouldMapStringToEmail() {
        // Arrange
        String emailString = "john.doe@example.com";

        // Act
        Email email = userMapper.mapStringToEmail(emailString);

        // Assert
        assertNotNull(email);
        assertEquals(emailString, email.getAddress());
    }

    @Test
    void mapEmailToString_ShouldMapEmailToString() {
        // Arrange
        Email email = new Email("john.doe@example.com");

        // Act
        String emailString = userMapper.mapEmailToString(email);

        // Assert
        assertNotNull(emailString);
        assertEquals("john.doe@example.com", emailString);
    }
}

