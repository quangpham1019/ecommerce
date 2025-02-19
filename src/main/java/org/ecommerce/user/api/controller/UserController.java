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

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {

        userApplicationService.deleteById(userId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                           @RequestBody User user) {
        return ResponseEntity.ok(userApplicationService.updatePartial(id, user));
    }

    @PostMapping("/saveAll")
    public ResponseEntity<List<User>> registerUsers(@RequestBody List<User> users) {
        return ResponseEntity.ok(userApplicationService.registerUsers(users));
    }

}
