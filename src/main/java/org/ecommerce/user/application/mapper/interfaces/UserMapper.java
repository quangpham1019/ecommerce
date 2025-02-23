package org.ecommerce.user.application.mapper.interfaces;
import org.ecommerce.user.application.dto.UserCreateDTO;
import org.ecommerce.user.application.dto.UserProfileDTO;
import org.ecommerce.user.application.dto.UserResponseDTO;
import org.ecommerce.user.domain.model.value_objects.Email;
import org.ecommerce.user.domain.model.User;
import org.ecommerce.user.domain.model.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toResponseDto(User user);
    User toEntity(UserCreateDTO dto);
    UserResponseDTO toResponseDto(UserCreateDTO dto);

    @Mapping(source = "userRoles", target = "roleNames", qualifiedByName = "mapUserRolesToRoleNames")
    UserProfileDTO toUserProfileDTO(User user);

    @Named("mapUserRolesToRoleNames")
    default List<String> mapUserRolesToRoleNames(Set<UserRole> userRoles) {
        if (userRoles == null) return Collections.emptyList();
        return userRoles.stream()
                .map(ur -> ur.getRole().getRoleName())
                .collect(Collectors.toList());
    }

    default Email mapStringToEmail(String email) {
        return email != null ? new Email(email) : null;
    }

    default String mapEmailToString(Email email) {
        return email != null ? email.getAddress() : null;
    }
}