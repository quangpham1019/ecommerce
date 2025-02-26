package org.ecommerce.user.domain.model.value_objects;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserStoredAddress {
    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;

    public UserStoredAddress(String street, String city, String state, String zip, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
    }
}
