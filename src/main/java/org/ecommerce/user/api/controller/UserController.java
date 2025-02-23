package org.ecommerce.user.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.ecommerce.user.application.dto.UserCreateDTO;
import org.ecommerce.user.application.dto.UserProfileDTO;
import org.ecommerce.user.application.dto.UserResponseDTO;
import org.ecommerce.user.application.service.UserApplicationService;
import org.ecommerce.user.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

    //region DEV-ONLY methods
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
    @PostMapping("/saveAll")
    public ResponseEntity<List<User>> registerUsers(@RequestBody List<User> users) {
        return ResponseEntity.ok(userApplicationService.registerUsers(users));
    }
    //endregion

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userApplicationService.getUserProfile(userId));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {

        return ResponseEntity.ok(userApplicationService.registerUser(userCreateDTO));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {

        userApplicationService.deleteById(userId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
                                                      @Valid @RequestBody UserCreateDTO userCreateDTO) {
        return ResponseEntity.ok(userApplicationService.updatePartial(id, userCreateDTO));
    }


}
