package org.ecommerce.product.api.controller;

import org.ecommerce.product.domain.model.Product;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping
    public List<Product> getProducts() {

        return Arrays.asList(
                new Product("Water gun"),
                new Product("Fire gun"),
                new Product("Air gun")
                );
    }
}
