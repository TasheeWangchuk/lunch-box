package com.lunchbox.lunch_box.modules.restaurant.dto.response;

import com.lunchbox.lunch_box.modules.restaurant.enums.RestaurantRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRestaurantResponse {
    private Long restaurantId;
    private String name;
    private String address;
    private String imageUrl;
    private Boolean isActive;
    private RestaurantRole role;
}
