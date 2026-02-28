package com.lunchbox.lunch_box.modules.restaurant.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRestaurantRequest {
    @NotBlank(message = "Restaurant name is required")
    private String name;

    private String address;

    private String contactNumber;
}
