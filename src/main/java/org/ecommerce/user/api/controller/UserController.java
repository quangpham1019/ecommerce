package org.ecommerce.user.api.controller;

import org.ecommerce.user.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {

        List<User> userList = Arrays.asList(
            new User("John"),
            new User("Jim")
        );

        List<User> users = new ArrayList<>(userList);
        return ResponseEntity.ok(users);
    }
}
