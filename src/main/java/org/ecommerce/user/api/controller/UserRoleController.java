package org.ecommerce.user.api.controller;

import org.ecommerce.user.application.service.UserApplicationService;
import org.ecommerce.user.domain.model.UserRole;
import org.ecommerce.user.infrastructure.repository.jpa.UserRoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userRoles")
public class UserRoleController {

    private final UserApplicationService userApplicationService;

    public UserRoleController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @GetMapping
    public ResponseEntity<List<UserRole>> getUserRoles() {
        return ResponseEntity.ok(userApplicationService.getUserRoles());
    }

    @GetMapping("/{userId}/roles")
    public ResponseEntity<List<UserRole>> getUserRoles(@PathVariable Long userId) {
        return ResponseEntity.ok(userApplicationService.getUserRolesByUserId(userId));
    }

    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<UserRole> addRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        return ResponseEntity.ok(userApplicationService.addRoleToUser(userId, roleId));
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<Void> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        userApplicationService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
