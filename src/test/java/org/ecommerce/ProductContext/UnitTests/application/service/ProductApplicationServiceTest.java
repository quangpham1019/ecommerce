package org.ecommerce.ProductContext.UnitTests.application.service;

import org.ecommerce.product.application.dto.ProductCreateDTO;
import org.ecommerce.product.application.dto.ProductDetailsDTO;
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
import java.util.Optional;
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
    public void setUp() {
    }

    @Test
    void getProducts_ShouldReturnAllProducts() {}

    @Test
    void getProductsBySellerId_ShouldReturnProductList_WhenUserIdExists() {}
    @Test
    void getProductsBySellerId_ShouldReturnEmptyList_WhenUserIdDoesNotExist() {}

    @Test
    void getProductDetailsById_ShouldReturnProductDetails_WhenProductExists() {
        // Arrange
        Long productId = 1L;
        ProductVariant primaryVariant = null;

        Product product = new Product(
                1L,
                "product name",
                "product description",
                primaryVariant,
                Set.of(new Category(
                        "Category name",
                        "Category description"
                ))
        );
        primaryVariant = new ProductVariant(
                "primary variant","",null,"", product
        );
        ProductVariant anotherVariant = new ProductVariant(
                "another variant","", null,"", product
        );
        product.setProductVariants(Set.of(primaryVariant, anotherVariant));

        ProductVariantDetailsDTO primaryVariantDetailsDTO = new ProductVariantDetailsDTO(
                "primary variant","","",null
        );
        ProductVariantDetailsDTO anotherVariantDetailsDTO = new ProductVariantDetailsDTO(
                "another variant","","",null
        );

        ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO(
                product.getName(),
                product.getDescription(),
                null,
                product.getCategories()
        );

        ProductDetailsDTO expected = new ProductDetailsDTO(
                "product name",
                "product description",
                Set.of(
                        new ProductVariantDetailsDTO(
                        "primary variant","","",null
                        ),
                        new ProductVariantDetailsDTO(
                        "another variant","","",null
                        )
                ),
                Set.of(new Category(
                        "Category name",
                        "Category description"
                ))
        );
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.toProductDetailsDTO(product)).thenReturn(productDetailsDTO);
        when(productMapper.toProductVariantDetailsDTO(primaryVariant)).thenReturn(primaryVariantDetailsDTO);
        when(productMapper.toProductVariantDetailsDTO(anotherVariant)).thenReturn(anotherVariantDetailsDTO);

        // Act
        ProductDetailsDTO actual = productApplicationService.getProductDetailsById(productId);

        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(productRepository).findById(productId);
        verify(productMapper).toProductDetailsDTO(product);
        verify(productMapper).toProductVariantDetailsDTO(primaryVariant);
        verify(productMapper).toProductVariantDetailsDTO(anotherVariant);
        verifyNoMoreInteractions(productRepository, productMapper);

    }
    @Test
    void getProductDetailsById_ShouldThrowException_WhenProductDoesNotExist() {
        // Arrange
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> productApplicationService.getProductDetailsById(productId));

        // Assert
        assertEquals("Product not found", exception.getMessage());
        verify(productRepository, times(1)).findById(productId);
        verifyNoMoreInteractions(productMapper);
        verifyNoMoreInteractions(productVariantRepository);
    }

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
    @Test
    void createProduct_ShouldReturnProductDetailsDTO_WhenProductIsValid() {
        // Arrange
        Price price = new Price(
                BigDecimal.valueOf(100),
                "USD"
        );
        Category category = new Category(
                "Test Category",
                "test category description");

        ProductCreateDTO productCreateDTO = new ProductCreateDTO();
        productCreateDTO.setCategories(Set.of(1L));

        Product newProduct = new Product(
                1L,
                "new product",
                "new product description",
                new ProductVariant(
                        "primary variant name",
                        "primary variant description",
                        price,
                        "image URL",
                        null
                ),
                null
        );

        ProductDetailsDTO expected = new ProductDetailsDTO(
                "new product",
                "new product description",
                Set.of(new ProductVariantDetailsDTO(
                                "primary variant name",
                                "primary variant description",
                                "image URL",
                                price)),
                Set.of(category)
        );

        when(productRepository.existsByNameAndSellerIdAndCategoryIds(any(),any(),any()))
                .thenReturn(false);
        when(productMapper.toProduct(productCreateDTO))
                .thenReturn(newProduct);
        when(categoryRepository.findAllById(Set.of(1L)))
                .thenReturn(List.of(category));
        when(productRepository.save(newProduct))
                .thenReturn(newProduct);
        when(productMapper.toProductDetailsDTO(newProduct))
                .thenReturn(expected);

        // Act
        ProductDetailsDTO actual = productApplicationService.createProduct(productCreateDTO);

        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(newProduct.getPrimaryVariant().getProduct(), newProduct);
        assertEquals(newProduct.getProductVariants().size(), 1);
        assertNotNull(newProduct.getCategories());
    }
}
