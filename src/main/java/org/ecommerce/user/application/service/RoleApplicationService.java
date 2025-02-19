package org.ecommerce.user.application.service;

import org.ecommerce.user.domain.model.Role;
import org.ecommerce.user.infrastructure.repository.jpa.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleApplicationService {

    private final RoleRepository roleRepository;

    public RoleApplicationService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public Role addRole(Role role) {
        if (roleRepository.existsByRoleName(role.getRoleName())) {
            throw new IllegalArgumentException("Role already exists");
        }

        return roleRepository.save(role);
    }

    public void deleteRoleById(long id) {
        Role role = roleRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Role not found"));

        roleRepository.delete(role);
    }
}
