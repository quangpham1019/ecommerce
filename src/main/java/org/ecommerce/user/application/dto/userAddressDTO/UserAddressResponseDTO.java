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
public class UserAddressResponseDTO {

    private String recipientName;
    private UserStoredAddress address;
    private String phoneNumber;
    private boolean isDefaultShipping;

    // This getter allows MapStruct to
        // map property isDefautShipping from source to this.isDefaultShipping
        // serialize the property name in JSON as "isDefaultShipping"
    // Without this method, MapStruct will serialize this.isDefaultShipping as "defaultShipping" in adherence to JavaBean naming convention
    public Boolean getIsDefaultShipping() {
        return isDefaultShipping;
    }
}
