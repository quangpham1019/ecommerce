package org.ecommerce.user.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user")
    @EqualsAndHashCode.Include
    private User user;

    private String recipientName;

    @ManyToOne
    @JoinColumn(name = "address")
    @EqualsAndHashCode.Include
    private Address address;

    private String phoneNumber;
    private boolean isDefaultShipping;

    public UserAddress(User user, String recipientName, Address address, String phoneNumber, boolean isDefaultShipping) {
        this.user = user;
        this.recipientName = recipientName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.isDefaultShipping = isDefaultShipping;
    }
}
