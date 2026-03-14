package com.lunchbox.lunch_box.modules.restaurant.dto.request;

import com.lunchbox.lunch_box.modules.restaurant.enums.RestaurantRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStaffRoleRequest {
    @NotNull(message = "Role is required")
    private RestaurantRole role;
}
