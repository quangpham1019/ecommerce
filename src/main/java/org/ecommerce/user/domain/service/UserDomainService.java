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

    /**
     * Validates that the given email is unique.
     *
     * @param email The email address to check for uniqueness.
     * @throws IllegalArgumentException if the email is already in use.
     */
    public void validateUniqueEmail(String email) {
        if (repository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already in use.");
        }
    }

    /**
     * Validates that the provided list of users contains only unique email addresses.
     *
     * This method:
     * <ul>
     *   <li>Extracts emails from the given list of users.</li>
     *   <li>Throws an exception if there are duplicate emails within the list.</li>
     *   <li>Queries the database to check if any of the emails already exist.</li>
     *   <li>Throws an exception if duplicate emails are found.</li>
     * </ul>
     *
     * @param users The list of users whose emails need to be validated.
     * @throws IllegalArgumentException if any of the provided emails is duplicate (either within the list or in the database).
     */
    public void validateUniqueEmail(List<User> users) {

        // extract emails from current list of users
        Set<String> newEmails = users
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toSet());

        if (users.size() != newEmails.size()) {
            throw new IllegalArgumentException("There are duplicate emails in list.");
        }

        // query the database to identify existing emails within the database
        Set<String> duplicateEmails = new HashSet<>(repository.findEmailsByEmails(newEmails));

        // throw an error with duplicate emails
        if (!duplicateEmails.isEmpty()) {
            throw new IllegalArgumentException("Duplicate emails: " + duplicateEmails);
        }
    }
}
