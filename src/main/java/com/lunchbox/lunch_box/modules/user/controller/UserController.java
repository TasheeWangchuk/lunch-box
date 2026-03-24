package com.lunchbox.lunch_box.modules.user.controller;

import com.lunchbox.lunch_box.common.dto.response.ApiResponse;
import com.lunchbox.lunch_box.modules.restaurant.dto.response.UserRestaurantResponse;
import com.lunchbox.lunch_box.modules.user.dto.request.UpdateUserRequest;
import com.lunchbox.lunch_box.modules.user.dto.response.UserResponse;
import com.lunchbox.lunch_box.modules.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        UserResponse user = userService.getCurrentUserProfile();
        return ResponseEntity
                .ok(ApiResponse.success(HttpStatus.OK.value(), "User profile retrieved successfully", user));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateCurrentUser(@Valid @RequestBody UpdateUserRequest request) {
        UserResponse user = userService.updateUserProfile(request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "User profile updated successfully", user));
    }

    @GetMapping("/me/restaurants")
    public ResponseEntity<ApiResponse<List<UserRestaurantResponse>>> getCurrentUserRestaurants() {
        List<UserRestaurantResponse> restaurants = userService.getRestaurantsForCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "User restaurants retrieved successfully", restaurants));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "User retrieved successfully", user));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "All users retrieved successfully", users));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "User deleted successfully", null));
    }
}
