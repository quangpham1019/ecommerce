package org.ecommerce.product.application.service;

import jakarta.transaction.Transactional;
import org.ecommerce.product.application.dto.ListProductDetailsDTO;
import org.ecommerce.product.application.dto.ProductDetailsDTO;
import org.ecommerce.product.application.dto.ProductVariantDetailsDTO;
import org.ecommerce.product.application.mapper.interfaces.ProductMapper;
import org.ecommerce.product.domain.model.Category;
import org.ecommerce.product.domain.model.Product;
import org.ecommerce.product.domain.model.ProductVariant;
import org.ecommerce.product.infrastructure.repository.jpa.CategoryRepository;
import org.ecommerce.product.infrastructure.repository.jpa.ProductRepository;
import org.ecommerce.product.infrastructure.repository.jpa.ProductVariantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductApplicationService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductApplicationService(ProductVariantRepository productVariantRepository, ProductMapper productMapper, ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productVariantRepository = productVariantRepository;
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }


    public List<ListProductDetailsDTO> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(p -> productMapper.toListProductResponseDTO(p.getPrimaryVariant()))
                .toList();
    }
    public List<ListProductDetailsDTO> getProductsBySellerId(Long sellerId) {
        Optional<List<Product>> products = productRepository.findBySellerId(sellerId);

        if (products.isEmpty())
            throw new IllegalArgumentException("No product found for the provided seller id");

        return products.get()
                .stream()
                .map(p -> productMapper.toListProductResponseDTO(p.getPrimaryVariant()))
                .toList();
    }

    @Transactional
    public ProductDetailsDTO getProductDetailsById(Long productId) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isEmpty()) throw new IllegalArgumentException("Product not found");

        ProductDetailsDTO productDetailsDTO = productMapper.toProductDetailsDTO(product.get().getPrimaryVariant());
        List<ProductVariant> associatedProductVariants = productVariantRepository.findByProductId(productId);
        List<ProductVariantDetailsDTO> productVariantDetailsDTOList = associatedProductVariants
                .stream()
                .map(productMapper::toProductVariantDetailsDTO)
                .toList();

        productDetailsDTO.setProductVariants(productVariantDetailsDTOList);
        productDetailsDTO.setCategories(product.get().getCategories());

        return productDetailsDTO;
    }

    public ProductVariantDetailsDTO getProductVariantDetailsById(Long productVariantId) {
        Optional<ProductVariant> productVariant = productVariantRepository.findById(productVariantId);

        if (productVariant.isEmpty()) throw new IllegalArgumentException("Product not found");

        return productMapper.toProductVariantDetailsDTO(productVariant.get());
    }


}
