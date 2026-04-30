package com.bytecoach.interview.controller;

import com.bytecoach.ai.service.AiOrchestratorService;
import com.bytecoach.common.api.Result;
import com.bytecoach.interview.dto.InterviewAnswerRequest;
import com.bytecoach.interview.dto.InterviewStartRequest;
import com.bytecoach.interview.vo.InterviewAnswerVO;
import com.bytecoach.interview.vo.InterviewCurrentQuestionVO;
import com.bytecoach.interview.vo.InterviewDetailVO;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interview")
public class InterviewController {

    private final AiOrchestratorService aiOrchestratorService;

    public InterviewController(AiOrchestratorService aiOrchestratorService) {
        this.aiOrchestratorService = aiOrchestratorService;
    }

    @PostMapping("/start")
    public Result<InterviewCurrentQuestionVO> start(@Valid @RequestBody InterviewStartRequest request) {
        return Result.success(InterviewCurrentQuestionVO.builder()
                .sessionId(1L)
                .currentIndex(1)
                .questionCount(request.getQuestionCount())
                .questionId(1001L)
                .questionTitle(request.getDirection() + " 面试题：解释 Spring AOP 的实现原理")
                .build());
    }

    @GetMapping("/current/{sessionId}")
    public Result<InterviewCurrentQuestionVO> current(@PathVariable Long sessionId) {
        return Result.success(InterviewCurrentQuestionVO.builder()
                .sessionId(sessionId)
                .currentIndex(1)
                .questionCount(3)
                .questionId(1001L)
                .questionTitle("解释 Spring AOP 的实现原理")
                .build());
    }

    @PostMapping("/answer")
    public Result<InterviewAnswerVO> answer(@Valid @RequestBody InterviewAnswerRequest request) {
        return Result.success(aiOrchestratorService.scoreInterviewAnswer(request));
    }

    @GetMapping("/detail/{sessionId}")
    public Result<InterviewDetailVO> detail(@PathVariable Long sessionId) {
        return Result.success(InterviewDetailVO.builder()
                .sessionId(sessionId)
                .direction("Spring")
                .status("finished")
                .records(List.of(
                        InterviewDetailVO.InterviewRecordVO.builder()
                                .questionId(1001L)
                                .questionTitle("解释 Spring AOP 的实现原理")
                                .userAnswer("示例答案")
                                .score("78")
                                .build()
                ))
                .build());
    }
}

