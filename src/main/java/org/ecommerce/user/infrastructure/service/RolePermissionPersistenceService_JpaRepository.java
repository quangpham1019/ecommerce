package org.ecommerce.user.infrastructure.service;

import org.ecommerce.user.domain.model.RolePermission;
import org.ecommerce.user.infrastructure.repository.jpa.RolePermissionRepository;
import org.ecommerce.user.infrastructure.service.interfaces.RolePermissionPersistenceService;
import org.springframework.stereotype.Service;

@Service
public class RolePermissionPersistenceService_JpaRepository extends CommonPersistenceService_JpaRepository<RolePermission, Long> implements RolePermissionPersistenceService {

    private final RolePermissionRepository repository;

    public RolePermissionPersistenceService_JpaRepository(RolePermissionRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
