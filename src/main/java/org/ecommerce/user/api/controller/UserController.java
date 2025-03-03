package org.ecommerce.user.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.ecommerce.user.application.dto.userAddressDTO.UserAddressCreateDTO;
import org.ecommerce.user.application.dto.userAddressDTO.UserAddressResponseDTO;
import org.ecommerce.user.application.dto.userDTO.UserCreateDTO;
import org.ecommerce.user.application.dto.userDTO.UserProfileDTO;
import org.ecommerce.user.application.dto.userDTO.UserResponseDTO;
import org.ecommerce.user.application.service.UserApplicationService;
import org.ecommerce.user.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    private final UserApplicationService userApplicationService;

    //region DEV-ONLY methods
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('MANAGE_USERS')")
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
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok(userApplicationService.getUsers());
    }
    @PostMapping("/saveAll")
    public ResponseEntity<List<User>> registerUsers(@RequestBody List<User> users) {
        return ResponseEntity.ok(userApplicationService.registerUsers(users));
    }
    //endregion

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userApplicationService.getUserProfile(userId));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {

        return ResponseEntity.ok(userApplicationService.registerUser(userCreateDTO));
    }

    @PreAuthorize("hasAnyRole('MODERATOR') or hasAnyAuthority('MANAGE_USERS')")
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

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{id}/addresses")
    public ResponseEntity<List<UserAddressResponseDTO>> getUserAddressesByUserId(@PathVariable Long id) {

        return ResponseEntity.ok(userApplicationService.getUserAddressesByUserId(id));
    }

//    @PostMapping("/{userId}/addresses/{addressId}")
//    public ResponseEntity<UserAddress> setDefaultShippingAddress(@PathVariable Long userId, @PathVariable Long addressId) {
//        return ResponseEntity.ok(userApplicationService.setDefaultShippingAddress(userId, addressId));
//    }

    @PostMapping("/{userId}/addresses")
    public ResponseEntity<UserAddressResponseDTO> addAddressToUserAccount(
            @PathVariable Long userId,
            @RequestBody UserAddressCreateDTO userAddressCreateDTO) {
        return ResponseEntity.ok(userApplicationService.addAddressToUserAccount(userId, userAddressCreateDTO));
    }
}
