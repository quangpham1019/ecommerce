package org.ecommerce.user.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ecommerce.user.domain.model.Role;
import org.ecommerce.user.domain.model.User;
import org.ecommerce.user.domain.model.UserRole;
import org.ecommerce.user.domain.model.UserRoleStatus;
import org.ecommerce.user.domain.service.UserDomainService;
import org.ecommerce.user.infrastructure.repository.jpa.RoleRepository;
import org.ecommerce.user.infrastructure.repository.jpa.UserRepository;
import org.ecommerce.user.infrastructure.repository.jpa.UserRoleRepository;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.ecommerce.common.utils.ObjectUtils.convertToMap;

@Service
//@RequiredArgsConstructor
public class UserApplicationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserDomainService userDomainService;

    public UserApplicationService(UserRepository userRepository, RoleRepository roleRepository,
                                  UserRoleRepository userRoleRepository, UserDomainService userDomainService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.userDomainService = userDomainService;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a list of all users
     */
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Persists a new user in the database.
     *
     * @param user the user entity to be persisted
     * @return the persisted user
     * @throws IllegalArgumentException if the provided email already exists
     */
    public User registerUser(User user) {
        userDomainService.validateUniqueEmail(user.getEmail());
        return userRepository.save(user);
    }

    /**
     * Persists a list of users in the database.
     *
     * @param users the list of users to persist
     * @return the persisted list of users
     * @throws IllegalArgumentException if any email in the list is a duplicate (either within the list or in the database)
     */
    public List<User> registerUsers(List<User> users) {
        userDomainService.validateUniqueEmail(users);
        return userRepository.saveAll(users);
    }

    /**
     * Deletes a user by ID.
     *
     * @param userId the ID of the user to delete
     * @throws IllegalArgumentException if no user exists with the given ID
     */
    public void deleteById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(userId);
    }

    /**
     * Updates specific fields of a user entity.
     * <p>
     * Only the non-null fields in the provided {@code User} object are updated.
     * </p>
     *
     * @param id the ID of the user to update
     * @param user the user object containing updated fields
     * @return the updated user entity
     * @throws IllegalArgumentException if the user does not exist
     */
    @Transactional
    public User updatePartial(Long id, User user) {
        User existingEntity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Convert the user object into a map of non-null fields
        Map<String, Object> updates = convertToMap(user);

        // Apply updates dynamically
        updates.forEach((field, value) -> {
            BeanWrapper wrapper = new BeanWrapperImpl(existingEntity);
            if (wrapper.isWritableProperty(field)) {
                wrapper.setPropertyValue(field, value);
            }
        });

        return userRepository.save(existingEntity);
    }

    /**
     * Assigns a role to a user.
     * <p>
     * If a {@code UserRole} with the given {@code userId} and {@code roleId} already exists:
     * <ul>
     *   <li>If the current status is {@code ACTIVE}, an error is thrown.</li>
     *   <li>If the current status is {@code INACTIVE}, it is updated to {@code ACTIVE}.</li>
     * </ul>
     * If no existing {@code UserRole} is found:
     * <ul>
     *   <li>Verifies that both the user and role exist.</li>
     *   <li>If either does not exist, an error is thrown.</li>
     *   <li>Creates and saves a new {@code UserRole} with an {@code ACTIVE} status.</li>
     * </ul>
     *
     * @param userId the ID of the user to whom the role is assigned
     * @param roleId the ID of the role being assigned
     * @return the created or reactivated {@code UserRole}
     * @throws IllegalArgumentException if the user or role does not exist, or if the role is already assigned and active
     */
    public UserRole addRoleToUser(Long userId, Long roleId) {

        Optional<UserRole> userRole = userRoleRepository.findByUser_IdAndRole_Id(userId, roleId);

        if (userRole.isPresent()) {
            UserRole currentUserRole = userRole.get();

            if (currentUserRole.getStatus().equals(UserRoleStatus.ACTIVE)) throw new IllegalArgumentException("User already has this role");

            currentUserRole.activate();
            return userRoleRepository.save(currentUserRole);
        } else {

            User user = userRepository.findById(userId).orElseThrow(
                    () -> new IllegalArgumentException("User not found")
            );
            Role role = roleRepository.findById(roleId).orElseThrow(
                    () -> new IllegalArgumentException("Role not found")
            );

            return userRoleRepository.save(new UserRole(user, role, UserRoleStatus.ACTIVE));
        }
    }

    /**
     * Removes a role from a user by updating the {@code UserRole} status to {@code INACTIVE}.
     * <p>
     * If the {@code UserRole} does not exist, or is already {@code INACTIVE}, an exception is thrown.
     * Otherwise, the status is changed to {@code INACTIVE} and the update is persisted.
     * </p>
     * @param userId the ID of the user from whom the role is removed
     * @param roleId the ID of the role being removed
     * @throws IllegalArgumentException if the UserRole does not exist, or the UserRole's status is INACTIVE.
     */
    @Transactional
    public void removeRoleFromUser(Long userId, Long roleId) {
        Optional<UserRole> userRole = userRoleRepository.findByUser_IdAndRole_Id(userId, roleId);

        if (userRole.isEmpty()) {
            throw new IllegalArgumentException("UserRole not found");
        }

        UserRole currentUserRole = userRole.get();

        if (currentUserRole.getStatus().equals(UserRoleStatus.INACTIVE)) throw new IllegalArgumentException("The role is already inactive and cannot be removed.");

        userRole.get().deactivate();
        userRoleRepository.save(userRole.get());
    }

    /**
     * Retrieves all user-role associations from the database.
     *
     * @return a list of all {@code UserRole} entities
     */
    public List<UserRole> getUserRoles() {
        return userRoleRepository.findAll();
    }

    /**
     * Retrieves all roles associated with a specific user.
     *
     * @param userId the ID of the user
     * @return a list of {@code UserRole} entities associated with the given user ID
     * @throws IllegalArgumentException if no user exists with the given ID
     */
    public List<UserRole> getUserRolesByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        return userRoleRepository.findByUser_Id(userId);
    }
}
