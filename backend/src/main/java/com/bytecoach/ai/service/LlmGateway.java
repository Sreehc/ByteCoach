package com.bytecoach.ai.service;

import com.bytecoach.ai.dto.AiChatRequest;
import com.bytecoach.ai.dto.AiChatResponse;

public interface LlmGateway {
    AiChatResponse chatCompletion(AiChatRequest request);
}

