package org.ecommerce.product.application.service;

import jakarta.transaction.Transactional;
import org.ecommerce.product.application.dto.ListProductDetailsDTO;
import org.ecommerce.product.application.dto.ProductCreateDTO;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

        ProductDetailsDTO productDetailsDTO = productMapper.toProductDetailsDTO(product.get());
        List<ProductVariant> associatedProductVariants = productVariantRepository.findByProductId(productId);
        Set<ProductVariantDetailsDTO> productVariantDetailsDTOsSet = associatedProductVariants
                .stream()
                .map(productMapper::toProductVariantDetailsDTO)
                .collect(Collectors.toSet());

        productDetailsDTO.setProductVariants(productVariantDetailsDTOsSet);
        productDetailsDTO.setCategories(product.get().getCategories());

        return productDetailsDTO;
    }

    public ProductVariantDetailsDTO getProductVariantDetailsById(Long productVariantId) {
        Optional<ProductVariant> productVariant = productVariantRepository.findById(productVariantId);

        if (productVariant.isEmpty()) throw new IllegalArgumentException("Product not found");

        return productMapper.toProductVariantDetailsDTO(productVariant.get());
    }

    public ProductDetailsDTO createProduct(ProductCreateDTO productCreateDTO) {

        if (productRepository.existsByNameAndSellerIdAndCategoryIds(
                productCreateDTO.getName(),
                productCreateDTO.getSellerId(),
                productCreateDTO.getCategories())) {
            throw new IllegalArgumentException("Product already exists");
        }

        Product product = productMapper.toProduct(productCreateDTO);

        product.getPrimaryVariant().setProduct(product);
        product.getProductVariants().add(product.getPrimaryVariant());

        Set<Category> categories = new HashSet<>(categoryRepository
                .findAllById(productCreateDTO.getCategories()));

        product.setCategories(categories);

        return productMapper.toProductDetailsDTO(productRepository.save(product));

    }


}
