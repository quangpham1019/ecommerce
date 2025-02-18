package org.ecommerce.user.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.ecommerce.user.domain.model.User;
import org.ecommerce.user.infrastructure.repository.jpa.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserDomainService {

    private final UserRepository repository;

    public UserDomainService(UserRepository repository) {
        this.repository = repository;
    }

    public void validateUniqueEmail(String email) {
        if (repository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already in use.");
        }
    }

    public void validateUniqueEmail(List<User> users) {

        // extract emails from current list of users
        Set<String> newEmails = users
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toSet());

        // query the database to identify existing emails within the database
        Set<String> duplicateEmails = new HashSet<>(repository.findEmailsByEmails(newEmails));

        // throw an error with duplicate emails
        if (!duplicateEmails.isEmpty()) {
            throw new IllegalArgumentException("Duplicate emails: " + duplicateEmails);
        }
    }
}
