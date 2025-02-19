package org.ecommerce.user.application.service;

import jakarta.transaction.Transactional;
import org.ecommerce.user.domain.model.Role;
import org.ecommerce.user.domain.service.RoleDomainService;
import org.ecommerce.user.infrastructure.repository.jpa.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleApplicationService {

    private final RoleRepository roleRepository;
    private final RoleDomainService roleDomainService;

    public RoleApplicationService(RoleRepository roleRepository, RoleDomainService roleDomainService) {
        this.roleRepository = roleRepository;
        this.roleDomainService = roleDomainService;
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public Role addRole(Role role) {
        if (roleRepository.existsByRoleName(role.getRoleName())) {
            throw new IllegalArgumentException("Role already exists");
        }
        roleDomainService.validateRoleName(role.getRoleName());
        return roleRepository.save(role);
    }

    @Transactional
    public void deleteRoleById(long id) {
        Role role = roleRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Role not found"));

        roleRepository.delete(role);
    }
}
