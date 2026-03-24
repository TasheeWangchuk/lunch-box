package com.lunchbox.lunch_box.modules.user.service;

import com.lunchbox.lunch_box.modules.user.dto.request.ChangePasswordRequest;
import com.lunchbox.lunch_box.modules.user.dto.request.UpdateUserRequest;
import com.lunchbox.lunch_box.modules.user.dto.response.UserResponse;

import com.lunchbox.lunch_box.modules.restaurant.dto.response.UserRestaurantResponse;

import java.util.List;

public interface UserService {
    UserResponse getCurrentUserProfile();

    UserResponse updateUserProfile(UpdateUserRequest request);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    void deleteUser(Long id);

    void changePassword(ChangePasswordRequest request);

    List<UserRestaurantResponse> getRestaurantsForCurrentUser();
}
