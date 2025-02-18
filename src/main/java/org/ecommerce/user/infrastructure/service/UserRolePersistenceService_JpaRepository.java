package org.ecommerce.user.infrastructure.service;

import org.ecommerce.user.domain.model.UserRole;
import org.ecommerce.user.infrastructure.repository.jpa.UserRoleRepository;
import org.ecommerce.user.infrastructure.service.interfaces.UserRolePersistenceService;
import org.springframework.stereotype.Service;

@Service
public class UserRolePersistenceService_JpaRepository extends CommonPersistenceService_JpaRepository<UserRole, Long> implements UserRolePersistenceService {

    private final UserRoleRepository repository;

    public UserRolePersistenceService_JpaRepository(UserRoleRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
