package com.orbitdynamix.controller;

import com.orbitdynamix.dto.api.ApiResponse;
import com.orbitdynamix.model.User;
import com.orbitdynamix.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    // create user
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
        var createdUser = userService.createUser(user);
        var response = new ApiResponse<User>(true, "User Created Successfully", createdUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update user.
     * Tips: use @PutMapping when entire object is updated. and use @PatchMapping when only specific fields are updated.
     */
    @PatchMapping ("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable UUID id, @RequestBody User user) {
        var updatedUser = userService.updateUser(id, user);
        var response = new ApiResponse<User>(true, "Update Successful", updatedUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // get user by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable UUID id) {
        var user = userService.getUserById(id);
        var response = new ApiResponse<User>(true, "User found", user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // get all users
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        var response = new ApiResponse<List<User>>(true, "User found", users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // delete user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        var response = new ApiResponse<String>(true, "User deleted successfully", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
