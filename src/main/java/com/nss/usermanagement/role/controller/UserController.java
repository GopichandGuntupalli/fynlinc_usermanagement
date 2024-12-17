package com.nss.usermanagement.role.controller;

import com.nss.usermanagement.role.constants.UsermangementConstants;
import com.nss.usermanagement.role.request.UserRequest;
import com.nss.usermanagement.role.model.UserDTO;
import com.nss.usermanagement.role.responce.UserResponse;
import com.nss.usermanagement.role.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UsermangementConstants.USER_BASE_URL)
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserRequest userRequest) {
        UserDTO createdUser = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping(UsermangementConstants.GET_ALL_USERS)
    public ResponseEntity<UserResponse> getAllUsers(@RequestBody UserRequest userRequest ) {
        UserResponse userResponse = userService.getAllUsers(userRequest);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping(UsermangementConstants.GET_USER_BY_ID)
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        try {
            UserDTO userDTO = userService.getUserById(id);
            return ResponseEntity.ok(userDTO);
        } catch (RuntimeException e) {
            // Inactive user or user not found
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null);
        }
    }

    @PutMapping(UsermangementConstants.UPDATE_USER)
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        try {
            UserDTO updatedUser = userService.updateUser(id, userRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            // User not found or other issue
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @DeleteMapping(UsermangementConstants.DELETE_USER)
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User status updated to inactive successfully.");
        } catch (RuntimeException e) {
            // User not found or other issue
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Failed to update status for User with ID " + id);
        }
    }
}
