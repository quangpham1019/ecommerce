package org.ecommerce.product.application.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ecommerce.product.domain.model.Product;
import org.hibernate.validator.constraints.Currency;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantCreateDTO {

        @NotBlank(message = "Product variant name is required.")
        private String productVariantName;
        private String productVariantDescription;

        @Positive(message = "Amount must be positive.")
        @Min(value = 1, message = "Amount must be greater than or equal to 1.")
        @Max(value = 10000, message = "Amount must be less than or equal to 10000.")
        private BigDecimal price;

        @Currency("Invalid currency.")
        private String currency;

        private String imageUrl;

        @NotNull(message = "Product variant must be associated with a product")
        private Product product;
}
