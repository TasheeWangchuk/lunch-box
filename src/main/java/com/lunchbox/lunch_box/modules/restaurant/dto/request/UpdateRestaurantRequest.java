package com.lunchbox.lunch_box.modules.restaurant.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class UpdateRestaurantRequest {
    @NotBlank(message = "Restaurant name is required")
    private String name;

    private String address;

    private String description;

    private String contactNumber;

    private List<String> cuisineTypes;

    private String openingHours;

    private String imageUrl;

    private String logoUrl;

    private Double latitude;

    private Double longitude;

    private Boolean active;
}
