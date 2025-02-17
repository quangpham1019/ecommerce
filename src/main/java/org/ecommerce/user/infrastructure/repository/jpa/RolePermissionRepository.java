package org.ecommerce.user.infrastructure.repository.jpa;

import org.ecommerce.user.domain.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {}
