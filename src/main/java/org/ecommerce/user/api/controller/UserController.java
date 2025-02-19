package org.ecommerce.user.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.ecommerce.user.application.service.UserApplicationService;
import org.ecommerce.user.domain.model.User;
import org.ecommerce.user.domain.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserApplicationService userApplicationService;

    @Autowired
    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @Operation(
            summary = "Get all users",
            description = "Fetch a list of all registered users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = User.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userApplicationService.getUsers());
    }


    @PostMapping
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userApplicationService.registerUser(user));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@RequestParam Long userId) {

        userApplicationService.deleteById(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                           @RequestBody User user) {
        userApplicationService.updatePartial(id, user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/saveAll")
    public ResponseEntity<List<User>> registerUsers(@RequestBody List<User> users) {
        List<User> registeredUsers = userApplicationService.registerUsers(users);
        return ResponseEntity.ok(registeredUsers);
    }


    @GetMapping("/userRoles")
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
    public ResponseEntity<String> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        userApplicationService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.ok("Role deleted successfully");
    }
}
