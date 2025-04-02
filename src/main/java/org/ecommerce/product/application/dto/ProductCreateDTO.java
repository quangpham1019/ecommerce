package org.ecommerce.product.application.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
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

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                ProductCreateDTO that = (ProductCreateDTO) o;
                return Objects.equals(sellerId, that.sellerId) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(primaryVariant, that.primaryVariant) && Objects.equals(categories, that.categories);
        }

        @Override
        public int hashCode() {
                return Objects.hash(sellerId, name, description, primaryVariant, categories);
        }
}
