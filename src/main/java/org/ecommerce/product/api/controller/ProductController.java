package org.ecommerce.product.api.controller;

import jakarta.validation.Valid;
import org.ecommerce.product.application.dto.ListProductDetailsDTO;
import org.ecommerce.product.application.dto.ProductCreateDTO;
import org.ecommerce.product.application.dto.ProductDetailsDTO;
import org.ecommerce.product.application.service.ProductApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    String ROLE_BUYER = "ROLE_BUYER";

    private final ProductApplicationService productApplicationService;

    public ProductController(ProductApplicationService productApplicationService) {
        this.productApplicationService = productApplicationService;
    }

    @GetMapping
    public List<ListProductDetailsDTO> getProducts() {
        if (hasAuthority(ROLE_ANONYMOUS) || hasAuthority(ROLE_BUYER)) {
            return productApplicationService.getProducts();
        }

//        Long sellerId = userApplicationService.getUser
        return productApplicationService.getProductsBySellerId(5L);
    }

    @GetMapping("/{productId}")
    public ProductDetailsDTO getProductDetailsById(@PathVariable Long productId) {
        return productApplicationService.getProductDetailsById(productId);
    }

    @PostMapping
    public ResponseEntity<ProductDetailsDTO> createProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO) {
        return ResponseEntity.ok(productApplicationService.createProduct(productCreateDTO));
    }

    boolean hasAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(authority))) {
            return true;
        }
        return false;
    }
}
