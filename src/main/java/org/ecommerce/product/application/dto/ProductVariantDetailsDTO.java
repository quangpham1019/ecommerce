package org.ecommerce.product.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ecommerce.product.domain.model.Product;
import org.ecommerce.product.domain.model.value_objects.Price;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantDetailsDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Price price;
//    private Inventory inventory;

    public ProductVariantDetailsDTO(String name, String description, String imageUrl, Price price) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductVariantDetailsDTO that = (ProductVariantDetailsDTO) o;

        if (id != null && that.getId() != null) {
            return id.equals(that.getId());
        }
        return Objects.equals(name, that.getName())
                && Objects.equals(description, that.getDescription())
                && Objects.equals(imageUrl, that.getImageUrl())
                && Objects.equals(price, that.getPrice());
    }

    @Override
    public int hashCode() {

        if (id != null) return Objects.hash(id);

        return Objects.hash(name, description, imageUrl, price);
    }
}
