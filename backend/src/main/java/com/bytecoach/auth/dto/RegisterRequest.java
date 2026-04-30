package com.bytecoach.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "cannot be blank")
    @Size(min = 4, max = 32, message = "length must be between 4 and 32")
    private String username;

    @NotBlank(message = "cannot be blank")
    @Size(min = 6, max = 32, message = "length must be between 6 and 32")
    private String password;

    @NotBlank(message = "cannot be blank")
    @Size(max = 32, message = "length must be less than or equal to 32")
    private String nickname;
}

