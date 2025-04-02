package org.ecommerce.product.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ecommerce.product.domain.model.Category;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailsDTO {
    private String name;
    private String description;
    private Set<ProductVariantDetailsDTO> productVariants;
    private Set<Category> categories;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDetailsDTO that = (ProductDetailsDTO) o;
        return Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(productVariants, that.productVariants)
                && Objects.equals(categories, that.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, productVariants, categories);
    }
}
