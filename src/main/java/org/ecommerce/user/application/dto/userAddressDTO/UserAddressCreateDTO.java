package org.ecommerce.user.application.dto.userAddressDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ecommerce.user.domain.model.value_objects.UserStoredAddress;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressCreateDTO {
    private String recipientName;
    private UserStoredAddress address;
    private String phoneNumber;
    private boolean isDefaultShipping;
}
