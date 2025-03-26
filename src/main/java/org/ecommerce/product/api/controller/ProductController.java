package org.ecommerce.product.api.controller;

import jakarta.validation.Valid;
import org.ecommerce.product.application.dto.ListProductDetailsDTO;
import org.ecommerce.product.application.dto.ProductCreateDTO;
import org.ecommerce.product.application.dto.ProductDetailsDTO;
import org.ecommerce.product.application.service.ProductApplicationService;
import org.ecommerce.user.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    private String ROLE_BUYER = "ROLE_BUYER";
    private String ROLE_SELLER = "ROLE_SELLER";

    private final ProductApplicationService productApplicationService;

    public ProductController(ProductApplicationService productApplicationService) {
        this.productApplicationService = productApplicationService;
    }

    @GetMapping
    public List<ListProductDetailsDTO> getProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (hasAuthority(ROLE_SELLER, authentication)) {
            Long userId = (Long) authentication.getPrincipal();
            return productApplicationService.getProductsBySellerId(userId);
        }
        return productApplicationService.getProducts();
    }

    @GetMapping("/{productId}")
    public ProductDetailsDTO getProductDetailsById(@PathVariable Long productId) {
        return productApplicationService.getProductDetailsById(productId);
    }

    @PostMapping
    public ResponseEntity<ProductDetailsDTO> createProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO) {
        return ResponseEntity.ok(productApplicationService.createProduct(productCreateDTO));
    }

    boolean hasAuthority(String authority, Authentication authentication) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority(authority));
    }
}
