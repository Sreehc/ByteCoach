package com.bytecoach.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "cannot be blank")
    private String username;

    @NotBlank(message = "cannot be blank")
    private String password;
}

