package com.bytecoach.chat.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatSessionVO {
    private Long id;
    private String title;
    private String mode;
}

