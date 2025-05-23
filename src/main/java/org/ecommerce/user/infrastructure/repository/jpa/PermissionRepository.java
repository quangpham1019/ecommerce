package org.ecommerce.user.infrastructure.repository.jpa;

import org.ecommerce.user.domain.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {}
