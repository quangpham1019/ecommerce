package org.ecommerce.user.infrastructure.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.ecommerce.user.domain.exception.InvalidEmailException;
import org.ecommerce.user.domain.model.value_objects.Email;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        // Validate the email based on your business logic (e.g., using regex, calling the Email value object)

        try {
            Email e = new Email(email);
            return true;
        } catch (InvalidEmailException e) {
            setCustomMessage(context, e.getMessage());
            return false;
        }
    }

    private void setCustomMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
