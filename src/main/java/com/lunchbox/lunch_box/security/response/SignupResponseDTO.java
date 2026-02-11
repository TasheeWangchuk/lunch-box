package com.lunchbox.lunch_box.security.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponseDTO {
    private String message;
    private String email;
    private boolean requiresVerification;
}
