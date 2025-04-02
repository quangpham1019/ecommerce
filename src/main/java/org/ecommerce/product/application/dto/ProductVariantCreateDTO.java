package org.ecommerce.product.application.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ecommerce.product.domain.model.Product;
import org.hibernate.validator.constraints.Currency;

import java.math.BigDecimal;
import java.util.Objects;

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

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                ProductVariantCreateDTO that = (ProductVariantCreateDTO) o;
                return Objects.equals(productVariantName, that.productVariantName) && Objects.equals(productVariantDescription, that.productVariantDescription) && Objects.equals(price, that.price) && Objects.equals(currency, that.currency) && Objects.equals(imageUrl, that.imageUrl) && Objects.equals(product, that.product);
        }

        @Override
        public int hashCode() {
                return Objects.hash(productVariantName, productVariantDescription, price, currency, imageUrl, product);
        }
}
