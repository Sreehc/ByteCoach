package com.bytecoach.chat.vo;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatSendVO {
    private Long sessionId;
    private String answer;
    private List<ChatMessageReferenceVO> references;
}

