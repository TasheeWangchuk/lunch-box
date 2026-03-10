package com.lunchbox.lunch_box.modules.restaurant.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class RestaurantResponse {
    private Long id;
    private String name;
    private String address;
    private String description;
    private String contactNumber;
    private List<String> cuisineTypes;
    private String openingHours;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
    private Boolean active;
    private OffsetDateTime createdAt;
}
