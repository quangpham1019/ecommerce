package org.ecommerce.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ecommerce.user.domain.model.Address;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressResponseDTO {

    private String recipientName;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;
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
