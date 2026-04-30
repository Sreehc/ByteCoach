package com.bytecoach.chat.controller;

import com.bytecoach.ai.service.AiOrchestratorService;
import com.bytecoach.chat.dto.ChatSendRequest;
import com.bytecoach.chat.vo.ChatMessageVO;
import com.bytecoach.chat.vo.ChatSendVO;
import com.bytecoach.chat.vo.ChatSessionVO;
import com.bytecoach.common.api.Result;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final AiOrchestratorService aiOrchestratorService;

    @PostMapping("/send")
    public Result<ChatSendVO> send(@Valid @RequestBody ChatSendRequest request) {
        return Result.success(aiOrchestratorService.answerChat(request));
    }

    @GetMapping("/sessions")
    public Result<List<ChatSessionVO>> sessions() {
        return Result.success(List.of(
                ChatSessionVO.builder().id(1L).title("Spring AOP 复习").mode("rag").build(),
                ChatSessionVO.builder().id(2L).title("JVM 高频问答").mode("chat").build()
        ));
    }

    @GetMapping("/messages/{sessionId}")
    public Result<List<ChatMessageVO>> messages(@PathVariable Long sessionId) {
        return Result.success(List.of(
                ChatMessageVO.builder().id(1L).role("user").content("AOP 的底层实现是什么？").createTime(LocalDateTime.now()).build(),
                ChatMessageVO.builder().id(2L).role("assistant").content("常见实现方式是 JDK 动态代理和 CGLIB。").createTime(LocalDateTime.now()).build()
        ));
    }

    @DeleteMapping("/session/{sessionId}")
    public Result<Void> delete(@PathVariable Long sessionId) {
        return Result.success();
    }
}

