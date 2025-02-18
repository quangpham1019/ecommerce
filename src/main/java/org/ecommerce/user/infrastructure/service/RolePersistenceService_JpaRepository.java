package org.ecommerce.user.infrastructure.service;

import org.ecommerce.user.domain.model.Role;
import org.ecommerce.user.infrastructure.repository.jpa.RoleRepository;
import org.ecommerce.user.infrastructure.service.interfaces.RolePersistenceService;
import org.springframework.stereotype.Service;

@Service
public class RolePersistenceService_JpaRepository extends CommonPersistenceService_JpaRepository<Role, Long> implements RolePersistenceService {

    private final RoleRepository repository;

    public RolePersistenceService_JpaRepository(RoleRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
