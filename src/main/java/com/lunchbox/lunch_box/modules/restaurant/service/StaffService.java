package com.lunchbox.lunch_box.modules.restaurant.service;

import com.lunchbox.lunch_box.modules.restaurant.dto.request.CreateStaffRequest;
import com.lunchbox.lunch_box.modules.restaurant.dto.response.StaffResponse;
import com.lunchbox.lunch_box.modules.restaurant.enums.RestaurantRole;

import java.util.List;

public interface StaffService {
    StaffResponse createStaff(CreateStaffRequest request);
    StaffResponse getStaffById(Long id);
    List<StaffResponse> getStaffByRestaurant(Long restaurantId);
    void deleteStaff(Long id);
    StaffResponse updateStaffRole(Long staffId, RestaurantRole newRole);
    List<StaffResponse> getAllStaff();
}
