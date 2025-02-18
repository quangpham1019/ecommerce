package org.ecommerce.user.infrastructure.service;

import org.ecommerce.user.domain.model.User;
import org.ecommerce.user.infrastructure.repository.jpa.UserRepository;
import org.ecommerce.user.infrastructure.service.interfaces.UserPersistenceService;
import org.springframework.stereotype.Service;

@Service
public class UserPersistenceService_JpaRepository extends CommonPersistenceService_JpaRepository<User, Long> implements UserPersistenceService {

    private final UserRepository repository;

    public UserPersistenceService_JpaRepository(UserRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
