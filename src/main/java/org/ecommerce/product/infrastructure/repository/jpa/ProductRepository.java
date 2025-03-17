package org.ecommerce.product.infrastructure.repository.jpa;

import org.ecommerce.product.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String laptop);
}
