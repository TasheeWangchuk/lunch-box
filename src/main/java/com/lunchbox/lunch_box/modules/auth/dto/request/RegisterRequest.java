package com.lunchbox.lunch_box.modules.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    @Size(max = 80, message = "Username must not exceed 80 characters")
    private String username;

    private String role;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 180, message = "Email must not exceed 180 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 225, message = "Password must be between 6 and 225 characters")
    private String password;

    @NotBlank(message = "Confirm Password is required")
    @Size(min = 6, max = 225, message = "Confirm Password must be between 6 and 225 characters")
    private String confirmPassword;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;
}
