package org.ecommerce.user.infrastructure.service;

import org.ecommerce.user.domain.model.User;
import org.ecommerce.user.domain.service.UserDomainService;
import org.ecommerce.user.infrastructure.repository.jpa.UserRepository;
import org.ecommerce.user.infrastructure.service.interfaces.UserPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPersistenceService_JpaRepository extends CommonPersistenceService_JpaRepository<User, Long> implements UserPersistenceService {

    private static final Logger log = LoggerFactory.getLogger(UserPersistenceService_JpaRepository.class);
    private final UserRepository repository;
    private final UserDomainService domainService;

    public UserPersistenceService_JpaRepository(UserRepository repository, UserDomainService domainService, UserDomainService userDomainService) {
        super(repository);
        this.repository = repository;
        this.domainService = domainService;
    }

    @Override
    public User save(User user) {
        domainService.validateUniqueEmail(user.getEmail());
        return repository.save(user);
    }

    @Override
    public List<User> saveAll(List<User> users) {
        domainService.validateUniqueEmail(users);
        return repository.saveAll(users);
    }
}
