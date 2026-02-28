package com.lunchbox.lunch_box.modules.delivery.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@Builder
public class RiderResponse {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private Boolean active;
    private OffsetDateTime createdAt;
}
