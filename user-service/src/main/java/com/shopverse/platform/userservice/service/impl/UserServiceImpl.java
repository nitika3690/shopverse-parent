package com.shopverse.platform.userservice.service.impl;

import com.shopverse.platform.userservice.dto.UserRequest;
import com.shopverse.platform.userservice.dto.UserResponse;
import com.shopverse.platform.userservice.entity.User;
import com.shopverse.platform.userservice.exception.ResourceNotFoundException;
import com.shopverse.platform.userservice.repository.UserRepository;
import com.shopverse.platform.userservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse createUser(UserRequest request) {

        logger.info("Creating user with email: {}", request.getEmail());

        User user = new User();

        mapToEntity(request, user);

        user.setActive(true);

        User savedUser = userRepository.save(user);

        logger.info("User created successfully with ID: {}", savedUser.getId());

        return mapToResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(Long id) {

        logger.info("Fetching user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id));

        return mapToResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {

        logger.info("Fetching all users");

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {

        logger.info("Updating user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id));

        mapToEntity(request, user);

        User updatedUser = userRepository.save(user);

        logger.info("User updated successfully with ID: {}", updatedUser.getId());

        return mapToResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {

        logger.info("Deleting user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id));

        userRepository.delete(user);

        logger.info("User deleted successfully with ID: {}", id);
    }

    // ====================== Private Mapper Methods ======================

    private void mapToEntity(UserRequest request, User user) {

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());
        user.setAddress(request.getAddress());
        user.setRole(request.getRole());
    }

    private UserResponse mapToResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .active(user.getActive())
                .role(user.getRole())
                .build();
    }
}