package com.lunchbox.lunch_box.modules.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> AuthResponse<T> success(String message, T data) {
        return new AuthResponse<>(true, message, data);
    }

    public static <T> AuthResponse<T> error(String message) {
        return new AuthResponse<>(false, message, null);
    }
}
