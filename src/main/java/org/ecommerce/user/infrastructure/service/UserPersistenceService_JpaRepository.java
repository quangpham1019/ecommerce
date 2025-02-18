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

    /**
     * Saves a user, ensuring unique email validation.
     * This method performs additional validation for unique email before saving the user.
     *
     * @param user the User object to be saved.
     * @return the saved User object.
     * @throws IllegalArgumentException If user provides an email that already exists in the database.
     */
    @Override
    public User save(User user) {
        domainService.validateUniqueEmail(user.getEmail());
        return repository.save(user);
    }

    /**
     * Saves a list of users, ensuring unique email validation for each user.
     * This method performs additional validation for unique emails before saving the users.
     *
     * @param users A list of User objects to be saved.
     * @return List of saved User objects.
     * @throws IllegalArgumentException If any user has an invalid email.
     */
    @Override
    public List<User> saveAll(List<User> users) {
        domainService.validateUniqueEmail(users);
        return repository.saveAll(users);
    }
}
