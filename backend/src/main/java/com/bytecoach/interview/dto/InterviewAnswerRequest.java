package com.bytecoach.interview.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InterviewAnswerRequest {
    @NotNull(message = "cannot be null")
    private Long sessionId;

    @NotNull(message = "cannot be null")
    private Long questionId;

    @NotBlank(message = "cannot be blank")
    private String answer;
}

