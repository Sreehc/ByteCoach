package com.bytecoach.interview.controller;

import com.bytecoach.common.api.Result;
import com.bytecoach.common.api.ResultCode;
import com.bytecoach.common.exception.BusinessException;
import com.bytecoach.interview.dto.InterviewAnswerRequest;
import com.bytecoach.interview.dto.InterviewStartRequest;
import com.bytecoach.interview.service.InterviewService;
import com.bytecoach.interview.vo.InterviewAnswerVO;
import com.bytecoach.interview.vo.InterviewCurrentQuestionVO;
import com.bytecoach.interview.vo.InterviewDetailVO;
import com.bytecoach.security.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interview")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping("/start")
    public Result<InterviewCurrentQuestionVO> start(@Valid @RequestBody InterviewStartRequest request) {
        return Result.success(interviewService.start(currentUserId(), request));
    }

    @GetMapping("/current/{sessionId}")
    public Result<InterviewCurrentQuestionVO> current(@PathVariable Long sessionId) {
        return Result.success(interviewService.current(currentUserId(), sessionId));
    }

    @PostMapping("/answer")
    public Result<InterviewAnswerVO> answer(@Valid @RequestBody InterviewAnswerRequest request) {
        return Result.success(interviewService.answer(currentUserId(), request));
    }

    @GetMapping("/detail/{sessionId}")
    public Result<InterviewDetailVO> detail(@PathVariable Long sessionId) {
        return Result.success(interviewService.detail(currentUserId(), sessionId));
    }

    private Long currentUserId() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "login required");
        }
        return userId;
    }
}
