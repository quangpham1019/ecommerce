package org.ecommerce.user.api.controller;

import org.ecommerce.user.domain.model.User;
import org.ecommerce.user.infrastructure.repository.jpa.UserRepository;
import org.ecommerce.user.infrastructure.service.interfaces.UserPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserPersistenceService userPersistenceService;

    @Autowired
    public UserController(UserPersistenceService userPersistenceService) {
        this.userPersistenceService = userPersistenceService;
    }

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
}
