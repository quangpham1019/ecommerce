package org.ecommerce.product.infrastructure.repository.jpa;

import org.ecommerce.product.domain.model.Category;
import org.ecommerce.product.domain.model.Product;
import org.ecommerce.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String laptop);
    Optional<List<Product>> findBySellerId(Long sellerId);

    @Query("SELECT COUNT(p) > 0 FROM Product p JOIN p.categories c " +
            "WHERE p.name = :name AND p.sellerId = :sellerId AND c.id IN :categoryIds")
    boolean existsByNameAndSellerIdAndCategoryIds(@Param("name") String name,
                                                  @Param("sellerId") Long sellerId,
                                                  @Param("categoryIds") Set<Long> categoryIds);}
