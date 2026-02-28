package com.lunchbox.lunch_box.modules.restaurant.service;

import com.lunchbox.lunch_box.modules.restaurant.dto.request.CreateStaffRequest;
import com.lunchbox.lunch_box.modules.restaurant.dto.response.StaffResponse;

import java.util.List;

public interface StaffService {
    StaffResponse createStaff(CreateStaffRequest request);

    StaffResponse getStaffById(Long id);

    List<StaffResponse> getStaffByRestaurant(Long restaurantId);

    void deleteStaff(Long id);
}
