package org.ecommerce.user.domain.model.value_objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Email {

    @Column(name = "email_address", unique = true)
    private String address;

    public Email(String address) {
        if (address == null || !isValidEmail(address)) {
            throw new IllegalArgumentException("Invalid email address");
        }
        this.address = address;
    }

    private boolean isValidEmail(String address) {

        if (address.trim().isEmpty()) return false;

        // Regex pattern can be updated to better suit business requirements
        Pattern emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher matcher = emailPattern.matcher(address);
        return matcher.find();
    }
}
