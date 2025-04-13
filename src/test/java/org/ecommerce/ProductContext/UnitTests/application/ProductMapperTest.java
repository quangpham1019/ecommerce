package org.ecommerce.ProductContext.UnitTests.application;

import org.ecommerce.product.application.mapper.interfaces.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class ProductMapperTest {

    private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    @BeforeEach
    public void setUp() {
    }

    @Test
    void toProductVariantDetailsDTO_ShouldMapAllFields_FromProductVariant() {}

    @Test
    void toProductDetailsDTO_ShouldMapAllFields_FromProduct() {}

    @Test
    void toListProductResponseDTO_ShouldMapAllFields_FromProductVariant() {}

    @Test
    void toProduct_ShouldIgnoreCategoriesAndProductVariantsInProduct_FromProductCreateDTO() {}

    @Test
    void toProductVariant_ShouldMapBigDecimalToPrice_FromProductVariantCreateDTO() {}
}
