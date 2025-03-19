package org.ecommerce.product.infrastructure.repository.jpa;

import org.ecommerce.product.domain.model.Product;
import org.ecommerce.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String laptop);
    Optional<List<Product>> findBySellerId(Long sellerId);
}
