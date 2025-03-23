package org.ecommerce.product.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ecommerce.product.domain.model.Category;
import org.ecommerce.product.domain.model.ProductVariant;
import org.ecommerce.product.domain.model.value_objects.Price;

import java.util.List;
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
}
