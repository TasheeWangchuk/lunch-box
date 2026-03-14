package com.lunchbox.lunch_box.modules.restaurant.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lunchbox.lunch_box.modules.restaurant.enums.RestaurantRole;
import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@Builder
public class StaffResponse {
    private Long id;
    private String username;
    private String email;
    private String phone;
    
    @JsonProperty("is_active")
    private Boolean isActive;
    
    private RestaurantRole role;
    
    private Long restaurantId;
    
    @JsonProperty("restaurant_name")
    private String restaurantName;
    
    @JsonProperty("joined_at")
    private OffsetDateTime joinedAt;
}
