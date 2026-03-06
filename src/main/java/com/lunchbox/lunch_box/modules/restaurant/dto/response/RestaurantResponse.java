package com.lunchbox.lunch_box.modules.restaurant.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@Builder
public class RestaurantResponse {
    private Long id;
    private String name;
    private String address;
    private String contactNumber;
    private Boolean active;
    private Long ownerId;
    private String ownerName;
    private OffsetDateTime createdAt;
}
