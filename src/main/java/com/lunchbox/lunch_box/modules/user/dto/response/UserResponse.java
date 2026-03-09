package com.lunchbox.lunch_box.modules.user.dto.response;

import com.lunchbox.lunch_box.modules.user.enums.AppRole;
import com.lunchbox.lunch_box.modules.user.enums.AuthProvider;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String avatarUrl;
    private String phone;
    private Set<AppRole> roles;
    private Boolean active;
    private AuthProvider provider;
    private Boolean emailVerified;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastLoginAt;
}
