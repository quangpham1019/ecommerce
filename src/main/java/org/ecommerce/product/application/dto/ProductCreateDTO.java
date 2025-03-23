package org.ecommerce.product.application.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDTO {

        @NotNull(message = "Seller ID is required.")
        @Positive(message = "Seller ID must be positive.")
        private Long sellerId;

        @NotBlank(message = "Product name is required.")
        private String name;
        private String description;

        @NotNull(message = "Product must have at least one primary variant.")
        private ProductVariantCreateDTO primaryVariant;

        @NotNull(message = "Product must have at least one category.")
        private Set<Long> categories;
}
