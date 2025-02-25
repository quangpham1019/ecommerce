package org.ecommerce.user.application.mapper.interfaces;

import org.ecommerce.user.application.dto.UserAddressResponseDTO;
import org.ecommerce.user.domain.model.UserAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserAddressMapper
{
    @Mapping(source = "address.street", target = "street")
    @Mapping(source = "address.city", target = "city")
    @Mapping(source = "address.state", target = "state")
    @Mapping(source = "address.zip", target = "zip")
    @Mapping(source = "address.country", target = "country")
    UserAddressResponseDTO toUserAddressResponseDTO(UserAddress userAddress);
}
