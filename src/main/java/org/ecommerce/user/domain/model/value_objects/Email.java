package org.ecommerce.user.domain.model.value_objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.ecommerce.user.domain.exception.InvalidEmailException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Email {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

    @Column(name = "email_address", unique = true)
    private String address;

    public Email(String address) {
        isValidEmail(address);
        this.address = address;
    }

    public static void isValidEmail(String address) {

        if (address == null || address.trim().isEmpty()) {
            throw new InvalidEmailException("Email cannot be null or empty");
        }

        if (!EMAIL_PATTERN.matcher(address).matches()) {
            throw new InvalidEmailException("Invalid email format");
        }
    }
}
