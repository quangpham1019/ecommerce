package org.ecommerce.product.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ecommerce.product.domain.model.value_objects.Price;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantDetailsDTO {
    private String name;
    private String description;
    private String imageUrl;
    private Price price;
//    private Inventory inventory;

}
