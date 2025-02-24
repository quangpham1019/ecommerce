package org.ecommerce.user.domain.model.value_objects;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.ecommerce.user.domain.exception.InvalidEmailException;

import java.util.regex.Pattern;

@Embeddable
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Email {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

    @Column(name = "email_address", unique = true)
    // Jackson will treat the getter of this field as the entire JSON representation of the Email object when serializing it.
    @JsonValue
    private String address;

    public Email(String address) {
        isValidEmail(address);
        this.address = address;
    }

    private void isValidEmail(String address) {

        if (address == null || address.trim().isEmpty()) {
            throw new InvalidEmailException("Email cannot be null or empty");
        }

        if (!EMAIL_PATTERN.matcher(address).matches()) {
            throw new InvalidEmailException("Invalid email format");
        }
    }
}
