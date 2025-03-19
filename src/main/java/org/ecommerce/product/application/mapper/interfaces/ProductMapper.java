package org.ecommerce.product.application.mapper.interfaces;

import org.ecommerce.product.application.dto.ListProductDetailsDTO;
import org.ecommerce.product.application.dto.ProductDetailsDTO;
import org.ecommerce.product.application.dto.ProductVariantDetailsDTO;
import org.ecommerce.product.domain.model.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "productVariantName", target = "name")
    @Mapping(source = "productVariantDescription", target = "description")
    ProductVariantDetailsDTO toProductVariantDetailsDTO(ProductVariant productVariant);

    @Mapping(source = "product.name", target = "name")
    @Mapping(source = "product.description", target = "description")
    ProductDetailsDTO toProductDetailsDTO(ProductVariant productVariant);

    @Mapping(source = "productVariantName", target = "name")
    ListProductDetailsDTO toListProductResponseDTO(ProductVariant productVariant);
}
