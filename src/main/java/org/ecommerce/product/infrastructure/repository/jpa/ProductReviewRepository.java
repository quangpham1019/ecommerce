package org.ecommerce.product.infrastructure.repository.jpa;

import org.ecommerce.product.domain.model.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {}
