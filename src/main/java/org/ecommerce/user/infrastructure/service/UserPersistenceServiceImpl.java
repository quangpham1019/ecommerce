package org.ecommerce.user.infrastructure.service;

import org.ecommerce.user.domain.model.User;
import org.ecommerce.user.infrastructure.repository.jpa.UserRepository;
import org.ecommerce.user.infrastructure.service.interfaces.UserPersistenceService;
import org.springframework.stereotype.Service;

@Service
public class UserPersistenceServiceImpl extends CommonPersistenceServiceImpl<User, Long> implements UserPersistenceService {

    private final UserRepository userJpaRepository;

    public UserPersistenceServiceImpl(UserRepository repository) {
        super(repository);
        this.userJpaRepository = repository;
    }
}
