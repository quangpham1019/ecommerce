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
import org.ecommerce.user.domain.model.Address;
import org.ecommerce.user.domain.model.User;
import org.ecommerce.user.domain.model.UserAddress;
import org.ecommerce.user.infrastructure.repository.jpa.AddressRepository;
import org.ecommerce.user.infrastructure.repository.jpa.UserAddressRepository;
import org.ecommerce.user.infrastructure.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserApplicationService userApplicationService;
    private final AddressRepository addressRepository;
    private final UserAddressRepository userAddressRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserApplicationService userApplicationService, AddressRepository addressRepository, UserAddressRepository userAddressRepository, UserRepository userRepository) {
        this.userApplicationService = userApplicationService;
        this.addressRepository = addressRepository;
        this.userAddressRepository = userAddressRepository;
        this.userRepository = userRepository;
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

    @GetMapping("/{id}/addresses")
    public ResponseEntity<Set<Address>> getUserAddresses(@PathVariable Long id) {
        Set<UserAddress> userAddresses = userRepository.findById(id).orElse(null).getUserAddressSet();
        Set<Address> addresses = userAddresses.stream().map(ua -> ua.getAddress()).collect(Collectors.toSet());
        return ResponseEntity.ok(addresses);
    }

}
