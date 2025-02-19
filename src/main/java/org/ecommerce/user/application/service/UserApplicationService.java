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

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User registerUser(User user) {
        userDomainService.validateUniqueEmail(user.getEmail());
        return userRepository.save(user);
    }

    public List<User> registerUsers(List<User> users) {
        userDomainService.validateUniqueEmail(users);
        return userRepository.saveAll(users);
    }


    public void deleteById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public User updatePartial(Long id, User user) {
        User existingEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

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

    public UserRole addRoleToUser(Long userId, Long roleId) {
        // check if userId and roleId exists in userRole table
        // if yes,
            // if status is ACTIVE, return error
            // if status is INACTIVE, change status to ACTIVE
        // else
            // check if userId and roleId exist in their respective tables
            // if either does not exist, throw errors
            // save the new userRole with status ACTIVE

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

    public List<UserRole> getUserRoles() {
        return userRoleRepository.findAll();
    }

    public List<UserRole> getUserRolesByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        return userRoleRepository.findByUser_Id(userId);
    }
}
