package com.bytecoach.knowledge.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KnowledgeImportRequest {
    @NotBlank(message = "cannot be blank")
    private String title;

    @NotBlank(message = "cannot be blank")
    private String sourceType;

    private Long categoryId;
}

