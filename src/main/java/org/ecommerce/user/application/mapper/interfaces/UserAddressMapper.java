package org.ecommerce.user.application.mapper.interfaces;

import org.ecommerce.user.application.dto.userAddressDTO.UserAddressCreateDTO;
import org.ecommerce.user.application.dto.userAddressDTO.UserAddressResponseDTO;
import org.ecommerce.user.domain.model.Address;
import org.ecommerce.user.domain.model.UserAddress;
import org.ecommerce.user.domain.model.value_objects.UserStoredAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserAddressMapper
{
    UserAddressResponseDTO toUserAddressResponseDTO(UserAddress userAddress);
    UserAddress toUserAddress(UserAddressCreateDTO addressCreateDTO);

    default UserStoredAddress toUserStoredAddress(Address address) {
        return new UserStoredAddress(
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getZip(),
                address.getCountry());
    };
}
