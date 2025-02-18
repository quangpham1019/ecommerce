package org.ecommerce.user.infrastructure.service;

import org.ecommerce.user.domain.model.Permission;
import org.ecommerce.user.infrastructure.repository.jpa.PermissionRepository;
import org.ecommerce.user.infrastructure.service.interfaces.PermissionPersistenceService;
import org.springframework.stereotype.Service;

@Service
public class PermissionPersistenceService_JpaRepository extends CommonPersistenceService_JpaRepository<Permission, Long> implements PermissionPersistenceService {

    private final PermissionRepository repository;

    public PermissionPersistenceService_JpaRepository(PermissionRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
