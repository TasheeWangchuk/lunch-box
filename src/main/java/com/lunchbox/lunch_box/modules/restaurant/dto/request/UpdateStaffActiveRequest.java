package com.lunchbox.lunch_box.modules.restaurant.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStaffActiveRequest {
    @NotNull(message = "Active status is required")
    private Boolean isActive;
}
