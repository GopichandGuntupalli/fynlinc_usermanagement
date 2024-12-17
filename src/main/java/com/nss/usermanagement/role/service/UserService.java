package com.nss.usermanagement.role.service;

import com.nss.usermanagement.role.responce.UserResponse;
import com.nss.usermanagement.role.entity.User;
import com.nss.usermanagement.role.mapper.UserMapper;
import com.nss.usermanagement.role.model.UserDTO;
import com.nss.usermanagement.role.repository.UserRepository;
import com.nss.usermanagement.role.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserService(@Lazy PasswordEncoder passwordEncoder)    {
        this.passwordEncoder = passwordEncoder;
    }

    // Create or Save a new user
    public UserDTO createUser(UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);


        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    // Update an existing user
    @Transactional
    public UserDTO updateUser(Long userId, UserRequest userRequest) {
        // Validate the userRequest if necessary
        if (userRequest == null) {
            throw new IllegalArgumentException("UserRequest cannot be null");
        }

        // Fetch the existing user
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Update fields using the mapper
        userMapper.updateUserEntity(existingUser, userRequest);

        if (userRequest.getPassword() != null) {
            String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
            existingUser.setPassword(encodedPassword);
        }
//        if (userRequest.getEmail() != null) {
//            String encodedEmail = passwordEncoder.encode(userRequest.getEmail());
//            existingUser.setEmail(encodedEmail);
//        }

        // If the status is specified, update it
        if (userRequest.getStatus() != null) {
            existingUser.setStatus(userRequest.getStatus());
        }

        // Save the updated user entity
        User updatedUser = userRepository.save(existingUser);

        // Return the updated user as DTO
        return userMapper.toDTO(updatedUser);
    }

    // Retrieve a user by ID
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Check if user is active
        if (user.getStatus() == 0) { // Assuming 0 means inactive
            throw new RuntimeException("User is inactive and cannot be accessed.");
        }

        return userMapper.toDTO(user);
    }

    // Mark a user as inactive
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Set the user's status to inactive (0)
        user.setStatus(0);
        userRepository.save(user);
    }

    // Retrieve all users with pagination

    public UserResponse getAllUsers(UserRequest userRequest) {

        // Create pageable object with sorting
        Pageable pageable = PageRequest.of(
                userRequest.getPage(),
                userRequest.getPageSize(),
                Sort.by(userRequest.getDirection(), userRequest.getSortBy())
        );
        // Fetch the paginated data
        Page<User> userPage ;

        // Check if data exists
        if (userRequest.getSearchKeyword()!=null){
            userPage = userRepository.searchUsers(userRequest.getSearchKeyword(),pageable);
        }else {
            userPage = userRepository.findAll(pageable);
        }
        // Map to DTO
        List<UserDTO> userDTOList = userPage.stream()
                //.filter(user -> user.getStatus() == 1) // Uncomment if filtering is needed
                .map(userMapper::toDTO)
                .collect(Collectors.toList());


        UserResponse userResponse = new UserResponse();
        userResponse.setData(userDTOList);

        // Set the total number of users
        userResponse.setTotalItems(userPage.getNumberOfElements());

        // Set the total number of pages
        userResponse.setTotalPages(userPage.getTotalPages());

        // Set the current page number
        userResponse.setCurrentPage(pageable.getPageNumber());

        // Set the current page size
        userResponse.setPageSize(pageable.getPageSize());





        return userResponse;
    }
}
