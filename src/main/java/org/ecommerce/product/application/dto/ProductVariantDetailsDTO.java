package org.ecommerce.product.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantDetailsDTO {
    private String name;
    private String description;
    private String imageUrl;
    private double price;
//    private Inventory inventory;

}
