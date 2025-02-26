package org.ecommerce.user.application.dto.userDTO;

import jakarta.validation.constraints.*;
import lombok.*;
import org.ecommerce.user.domain.model.value_objects.Email;
import org.ecommerce.user.infrastructure.validation.ValidEmail;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {

    @NotBlank(message = "Username is required")
    @Size(min = 6, max = 20, message = "Username must be between 6 and 20 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    private String password;

    @ValidEmail
    private String email;

    public Email getEmail() {
        return new Email(email);
    }
}
