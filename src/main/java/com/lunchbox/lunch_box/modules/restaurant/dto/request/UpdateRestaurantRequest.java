package com.lunchbox.lunch_box.modules.restaurant.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateRestaurantRequest {
    @NotBlank(message = "Restaurant name is required")
    private String name;

    private String address;

    private String contactNumber;

    private Boolean active;
}
