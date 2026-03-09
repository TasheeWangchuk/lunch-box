package com.lunchbox.lunch_box.modules.user.mapper;

import com.lunchbox.lunch_box.modules.user.dto.response.UserResponse;
import com.lunchbox.lunch_box.modules.user.entity.User;
import com.lunchbox.lunch_box.modules.user.entity.Role;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .phone(user.getPhone())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .active(user.getActive())
                .provider(user.getProvider())
                .emailVerified(user.getEmailVerified())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }
}
