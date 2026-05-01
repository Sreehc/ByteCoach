package com.bytecoach.interview.service;

import com.bytecoach.interview.dto.InterviewAnswerRequest;
import com.bytecoach.interview.dto.InterviewStartRequest;
import com.bytecoach.interview.vo.InterviewAnswerVO;
import com.bytecoach.interview.vo.InterviewCurrentQuestionVO;
import com.bytecoach.interview.vo.InterviewDetailVO;

public interface InterviewService {

    InterviewCurrentQuestionVO start(Long userId, InterviewStartRequest request);

    InterviewCurrentQuestionVO current(Long userId, Long sessionId);

    InterviewAnswerVO answer(Long userId, InterviewAnswerRequest request);

    InterviewDetailVO detail(Long userId, Long sessionId);
}
