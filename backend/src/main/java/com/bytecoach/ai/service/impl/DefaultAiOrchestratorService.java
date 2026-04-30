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
import com.bytecoach.knowledge.service.KnowledgeRetrievalService;
import com.bytecoach.knowledge.vo.KnowledgeSearchVO;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultAiOrchestratorService implements AiOrchestratorService {

    private final LlmGateway llmGateway;
    private final PromptTemplateService promptTemplateService;
    private final KnowledgeRetrievalService knowledgeRetrievalService;

    @Override
    public ChatSendVO answerChat(ChatSendRequest request) {
        List<KnowledgeSearchVO.Reference> references = "rag".equalsIgnoreCase(request.getMode())
                ? knowledgeRetrievalService.searchReferences(request.getMessage(), 5)
                : List.of();
        String systemPrompt = references.isEmpty()
                ? promptTemplateService.chatPrompt()
                : promptTemplateService.knowledgeChatPrompt() + "\n" + promptTemplateService.referenceConstraintPrompt();
        AiChatResponse response = callModel(systemPrompt, request.getMessage(), references);
        String answer = response.getContent().isBlank() ? fallbackAnswer(request, references) : response.getContent();
        return ChatSendVO.builder()
                .sessionId(request.getSessionId())
                .answer(answer)
                .references(references.stream().map(reference -> ChatMessageReferenceVO.builder()
                        .docId(reference.getDocId())
                        .docTitle(reference.getDocTitle())
                        .chunkId(reference.getChunkId())
                        .snippet(reference.getSnippet())
                        .build()).toList())
                .build();
    }

    @Override
    public InterviewAnswerVO scoreInterviewAnswer(InterviewAnswerRequest request) {
        AiChatResponse scoreResponse;
        try {
            scoreResponse = llmGateway.chatCompletion(AiChatRequest.builder()
                    .systemPrompt(promptTemplateService.interviewScorePrompt())
                    .userPrompt("QuestionId=" + request.getQuestionId() + "\nAnswer=" + request.getAnswer())
                    .references(List.of())
                    .build());
        } catch (RuntimeException exception) {
            scoreResponse = AiChatResponse.builder().content("").raw("fallback").build();
        }
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

    private AiChatResponse callModel(String systemPrompt, String userPrompt, List<KnowledgeSearchVO.Reference> references) {
        try {
            return llmGateway.chatCompletion(AiChatRequest.builder()
                    .systemPrompt(systemPrompt)
                    .userPrompt(userPrompt)
                    .references(references.stream()
                            .map(reference -> reference.getDocTitle() + ": " + reference.getSnippet())
                            .toList())
                    .build());
        } catch (RuntimeException exception) {
            return AiChatResponse.builder().content("").raw("fallback").build();
        }
    }

    private String fallbackAnswer(ChatSendRequest request, List<KnowledgeSearchVO.Reference> references) {
        if (!references.isEmpty()) {
            String snippets = references.stream()
                    .map(KnowledgeSearchVO.Reference::getSnippet)
                    .limit(2)
                    .reduce((left, right) -> left + "\n\n" + right)
                    .orElse("");
            return "根据当前知识库命中的片段，先给出一个可复述版本：\n\n" + snippets;
        }
        return "这是普通问答模式的兜底回答：当前未命中知识库上下文，你可以继续追问更具体的 Java 后端问题。";
    }
}
