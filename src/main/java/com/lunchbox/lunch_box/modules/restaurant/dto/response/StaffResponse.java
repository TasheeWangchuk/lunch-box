package com.lunchbox.lunch_box.modules.restaurant.dto.response;

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
    private Boolean active;
    private Long restaurantId;
    private String restaurantName;
    private OffsetDateTime createdAt;
}
