package com.bytecoach.plan.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PlanTaskStatusRequest {
    @NotBlank(message = "cannot be blank")
    private String status;
}

