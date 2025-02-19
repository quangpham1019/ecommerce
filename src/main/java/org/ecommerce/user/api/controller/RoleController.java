package org.ecommerce.user.api.controller;

import jakarta.annotation.PostConstruct;
import org.ecommerce.user.application.service.RoleApplicationService;
import org.ecommerce.user.domain.model.Role;
import org.ecommerce.user.domain.model.UserRole;
import org.ecommerce.user.infrastructure.repository.jpa.RoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleApplicationService roleApplicationService;

    public RoleController(RoleApplicationService roleApplicationService) {
        this.roleApplicationService = roleApplicationService;
    }

    @GetMapping
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(roleApplicationService.getRoles());
    }

    @PostMapping
    public ResponseEntity<Role> addRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleApplicationService.addRole(role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  deleteRoleById(@RequestParam("id") Long id) {
        roleApplicationService.deleteRoleById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
