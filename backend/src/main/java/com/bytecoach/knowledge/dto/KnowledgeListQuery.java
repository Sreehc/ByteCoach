package com.bytecoach.knowledge.dto;

import lombok.Data;

@Data
public class KnowledgeListQuery {
    private Long categoryId;
    private String keyword;
    private String status;
}

