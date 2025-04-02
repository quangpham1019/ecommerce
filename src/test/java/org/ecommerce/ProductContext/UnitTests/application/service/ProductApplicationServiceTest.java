package org.ecommerce.ProductContext.UnitTests.application.service;

import org.ecommerce.product.application.dto.ProductCreateDTO;
import org.ecommerce.product.application.dto.ProductDetailsDTO;
import org.ecommerce.product.application.dto.ProductVariantCreateDTO;
import org.ecommerce.product.application.dto.ProductVariantDetailsDTO;
import org.ecommerce.product.application.mapper.interfaces.ProductMapper;
import org.ecommerce.product.application.service.ProductApplicationService;
import org.ecommerce.product.domain.model.Category;
import org.ecommerce.product.domain.model.Product;
import org.ecommerce.product.domain.model.ProductVariant;
import org.ecommerce.product.domain.model.value_objects.Price;
import org.ecommerce.product.infrastructure.repository.jpa.CategoryRepository;
import org.ecommerce.product.infrastructure.repository.jpa.ProductRepository;
import org.ecommerce.product.infrastructure.repository.jpa.ProductVariantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductApplicationServiceTest {

    @Mock
    private ProductVariantRepository productVariantRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductApplicationService productApplicationService;

    @BeforeEach
    public void setUp() {}

    @Test
    void getProducts_ShouldReturnAllProducts() {}

    @Test
    void getProductsBySellerId_ShouldReturnProductList_WhenUserIdExists() {}
    @Test
    void getProductsBySellerId_ShouldReturnEmptyList_WhenUserIdDoesNotExist() {}

    @Test
    void getProductDetailsById_ShouldReturnProductDetails_WhenProductExists() {}
    @Test
    void getProductsBySellerId_ShouldThrowException_WhenProductDoesNotExist() {}

    @Test
    void createProduct_ShouldThrowException_WhenProductIsDuplicateByNameAndSellerIdAndCategories() {
        // Arrange
        ProductCreateDTO productCreateDTO = new ProductCreateDTO(
                1L,
                "Duplicate Product",
                "description",
                null,
                Set.of(1L, 2L)
        );

        when(productRepository.existsByNameAndSellerIdAndCategoryIds(
                productCreateDTO.getName(),
                productCreateDTO.getSellerId(),
                productCreateDTO.getCategories()
        )).thenReturn(true);

        // Act
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> productApplicationService.createProduct(productCreateDTO)
        );

        // Assert
        assertEquals("Product already exists", exception.getMessage());
        verify(productMapper, never()).toProduct(productCreateDTO);
        verify(categoryRepository, never()).findAll();
        verify(productRepository, never()).save(any(Product.class));
        verify(productMapper, never()).toProductDetailsDTO(any(Product.class));
    }

    // Need refactoring for better readability and maintainability
    @Test
    void createProduct_ShouldReturnProductDetailsDTO_WhenProductIsValid() {
        // Arrange
        BigDecimal price = BigDecimal.valueOf(100);
        String currency = "USD";
        Product savingProduct = null;
        Category expectedCategory = new Category("Test Category", "test category description");

        ProductVariantCreateDTO productVariantCreateDTO = new ProductVariantCreateDTO(
                "primary variant name",
                "variant description",
                price,
                currency,
                "image URL",
                null
        );
        ProductCreateDTO productCreateDTO = new ProductCreateDTO(
                1L,
                "Not a duplicate",
                "description",
                productVariantCreateDTO,
                Set.of(1L)
        );

        Price expectedPrice = new Price(
                price,
                currency
        );
        ProductVariant expectedPrimaryVariant = new ProductVariant(
                productVariantCreateDTO.getProductVariantName(),
                productVariantCreateDTO.getProductVariantDescription(),
                expectedPrice,
                productVariantCreateDTO.getImageUrl(),
                savingProduct
        );
        savingProduct = new Product(
                productCreateDTO.getSellerId(),
                productCreateDTO.getName(),
                productCreateDTO.getDescription(),
                expectedPrimaryVariant,
                null
        );

        ProductVariantDetailsDTO expectedVariantDetails = new ProductVariantDetailsDTO(
                productVariantCreateDTO.getProductVariantName(),
                productVariantCreateDTO.getProductVariantDescription(),
                productVariantCreateDTO.getImageUrl(),
                expectedPrice
        );
        ProductDetailsDTO expectedProduct = new ProductDetailsDTO(
                productCreateDTO.getName(),
                productCreateDTO.getDescription(),
                Set.of(expectedVariantDetails),
                Set.of(expectedCategory)
        );

        when(productRepository.existsByNameAndSellerIdAndCategoryIds(
                productCreateDTO.getName(),
                productCreateDTO.getSellerId(),
                productCreateDTO.getCategories()
        )).thenReturn(false);
        when(productMapper.toProduct(productCreateDTO)).thenReturn(savingProduct);
        when(categoryRepository.findAllById(Set.of(1L))).thenReturn(List.of(expectedCategory));
        when(productRepository.save(savingProduct)).thenReturn(savingProduct);
        when(productMapper.toProductDetailsDTO(savingProduct)).thenReturn(expectedProduct);

        // Act
        ProductDetailsDTO actual = productApplicationService.createProduct(productCreateDTO);

        // Assert
        assertNotNull(actual);
        assertEquals(expectedProduct, actual);
    }
}
