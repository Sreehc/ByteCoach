package com.bytecoach.ai.service.impl;

import com.bytecoach.ai.dto.AiChatRequest;
import com.bytecoach.ai.dto.AiChatResponse;
import com.bytecoach.ai.service.AiOrchestratorService;
import com.bytecoach.ai.service.LlmGateway;
import com.bytecoach.ai.service.PromptTemplateService;
import com.bytecoach.chat.dto.ChatSendRequest;
import com.bytecoach.chat.vo.ChatMessageReferenceVO;
import com.bytecoach.chat.vo.ChatSendVO;
import com.bytecoach.interview.dto.InterviewAnswerRequest;
import com.bytecoach.interview.vo.InterviewAnswerVO;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultAiOrchestratorService implements AiOrchestratorService {

    private final LlmGateway llmGateway;
    private final PromptTemplateService promptTemplateService;

    @Override
    public ChatSendVO answerChat(ChatSendRequest request) {
        AiChatResponse response = llmGateway.chatCompletion(AiChatRequest.builder()
                .systemPrompt(promptTemplateService.chatPrompt())
                .userPrompt(request.getMessage())
                .references(List.of("Spring 核心笔记: AOP 常见实现方式是 JDK 动态代理和 CGLIB。"))
                .build());
        return ChatSendVO.builder()
                .sessionId(request.getSessionId() == null ? 1L : request.getSessionId())
                .answer(response.getContent())
                .references(List.of(ChatMessageReferenceVO.builder()
                        .docId(1L)
                        .docTitle("Spring 核心笔记")
                        .chunkId(101L)
                        .snippet("AOP 常见实现方式是 JDK 动态代理和 CGLIB。")
                        .build()))
                .build();
    }

    @Override
    public InterviewAnswerVO scoreInterviewAnswer(InterviewAnswerRequest request) {
        AiChatResponse scoreResponse = llmGateway.chatCompletion(AiChatRequest.builder()
                .systemPrompt(promptTemplateService.interviewScorePrompt())
                .userPrompt("QuestionId=" + request.getQuestionId() + "\nAnswer=" + request.getAnswer())
                .references(List.of())
                .build());
        String comment = scoreResponse.getContent().isBlank()
                ? "评分结果占位：后续在此处解析结构化输出。"
                : scoreResponse.getContent();
        return InterviewAnswerVO.builder()
                .score(new BigDecimal("78"))
                .comment(comment)
                .standardAnswer("标准答案占位：后续回填题库标准答案。")
                .followUp("如果使用 CGLIB，它与 JDK 动态代理的适用边界是什么？")
                .addedToWrongBook(true)
                .hasNextQuestion(true)
                .build();
    }
}

