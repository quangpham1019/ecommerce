package org.ecommerce.user.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.ecommerce.user.domain.model.User;
import org.ecommerce.user.infrastructure.repository.jpa.UserRepository;
import org.ecommerce.user.infrastructure.service.interfaces.UserPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserPersistenceService userPersistenceService;

    @Autowired
    public UserController(UserPersistenceService userPersistenceService) {
        this.userPersistenceService = userPersistenceService;
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

        List<User> users = userPersistenceService.findAll();

        return ResponseEntity.ok(users);
    }


    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {

        userPersistenceService.save(user);
        
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@RequestParam int userId) {
        userPersistenceService.deleteById((long)userId);

        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id,
                                           @RequestBody User user) {
        userPersistenceService.updatePartial((long)id, user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/saveAll")
    public ResponseEntity<List<User>> saveAllUsers(@RequestBody List<User> users) {
        List<User> savedUsers = userPersistenceService.saveAll(users);
        return ResponseEntity.ok(savedUsers);
    }
}
