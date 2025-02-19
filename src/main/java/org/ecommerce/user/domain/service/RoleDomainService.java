package org.ecommerce.user.domain.service;

import org.ecommerce.user.infrastructure.repository.jpa.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RoleDomainService {

    private RoleRepository roleRepository;

    public RoleDomainService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void validateRoleName(String roleName) {
        if (Objects.isNull(roleName) || roleName.trim().isEmpty()) {
            throw new IllegalArgumentException("Role name cannot be null or empty");
        }
    }
}
