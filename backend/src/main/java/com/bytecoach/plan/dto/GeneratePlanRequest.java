package com.bytecoach.plan.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GeneratePlanRequest {
    @NotBlank(message = "cannot be blank")
    private String direction;

    @Min(value = 1, message = "must be at least 1")
    @Max(value = 30, message = "must be at most 30")
    private Integer days;

    @Min(value = 15, message = "must be at least 15")
    @Max(value = 240, message = "must be at most 240")
    private Integer dailyMinutes;
}

