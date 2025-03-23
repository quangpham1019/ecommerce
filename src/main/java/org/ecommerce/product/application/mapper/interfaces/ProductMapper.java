package org.ecommerce.product.application.mapper.interfaces;

import org.ecommerce.product.application.dto.*;
import org.ecommerce.product.domain.model.Product;
import org.ecommerce.product.domain.model.ProductVariant;
import org.ecommerce.product.domain.model.value_objects.Price;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "productVariantName", target = "name")
    @Mapping(source = "productVariantDescription", target = "description")
    ProductVariantDetailsDTO toProductVariantDetailsDTO(ProductVariant productVariant);

    ProductDetailsDTO toProductDetailsDTO(Product product);

    @Mapping(source = "productVariantName", target = "name")
    ListProductDetailsDTO toListProductResponseDTO(ProductVariant productVariant);

    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "productVariants", ignore = true)
    Product toProduct(ProductCreateDTO productCreateDTO);

    @Mapping(target = "price", source = ".")
    ProductVariant toProductVariant(ProductVariantCreateDTO productVariantCreateDTO);

    default Price mapPrice(ProductVariantCreateDTO productVariantCreateDTO) {
        return new Price(
                productVariantCreateDTO.getPrice(),
                productVariantCreateDTO.getCurrency());
    }
}
