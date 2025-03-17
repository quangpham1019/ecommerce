package org.ecommerce.product.infrastructure.repository.jpa;

import org.ecommerce.product.domain.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {}
