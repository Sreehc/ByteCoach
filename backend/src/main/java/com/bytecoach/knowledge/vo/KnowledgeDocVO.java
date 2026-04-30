package com.bytecoach.knowledge.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KnowledgeDocVO {
    private Long id;
    private String title;
    private String status;
    private String summary;
}

