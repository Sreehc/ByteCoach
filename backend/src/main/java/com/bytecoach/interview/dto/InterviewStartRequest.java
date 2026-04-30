package com.bytecoach.interview.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InterviewStartRequest {
    @NotBlank(message = "cannot be blank")
    private String direction;

    @Min(value = 3, message = "must be at least 3")
    @Max(value = 5, message = "must be at most 5")
    private Integer questionCount = 3;
}

