package com.lunchbox.lunch_box.modules.user.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @Size(max = 80)
    private String username;

    @Size(max = 255)
    private String avatarUrl;

    @Size(max = 20)
    private String phone;
}
