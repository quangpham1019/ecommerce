package org.ecommerce.product.api.controller;

import org.ecommerce.product.application.dto.ListProductDetailsDTO;
import org.ecommerce.product.application.dto.ProductDetailsDTO;
import org.ecommerce.product.application.service.ProductApplicationService;
import org.ecommerce.product.domain.model.Product;
import org.ecommerce.user.application.service.UserApplicationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductApplicationService productApplicationService;
    private final UserApplicationService userApplicationService;

    public ProductController(ProductApplicationService productApplicationService, UserApplicationService userApplicationService) {
        this.productApplicationService = productApplicationService;
        this.userApplicationService = userApplicationService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('MANAGE_PRODUCTS')")
    @GetMapping
    public List<ListProductDetailsDTO> getProducts(@AuthenticationPrincipal User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return productApplicationService.getProducts();
        }

//        Long sellerId = userApplicationService.getUser
        return productApplicationService.getProductsBySellerId(5L);
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/{productId}")
    public ProductDetailsDTO getProductDetailsById(@PathVariable Long productId) {
        return productApplicationService.getProductDetailsById(productId);
    }
}
