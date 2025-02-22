package org.ecommerce.user.application.mapper.interfaces;
import org.ecommerce.user.application.dto.UserCreateDTO;
import org.ecommerce.user.application.dto.UserListDTO;
import org.ecommerce.user.application.dto.UserResponseDTO;
import org.ecommerce.user.domain.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toResponseDto(User user);
    User toEntity(UserCreateDTO dto);
}