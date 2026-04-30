package com.bytecoach.ai.service;

import com.bytecoach.chat.dto.ChatSendRequest;
import com.bytecoach.chat.vo.ChatSendVO;
import com.bytecoach.interview.dto.InterviewAnswerRequest;
import com.bytecoach.interview.vo.InterviewAnswerVO;

public interface AiOrchestratorService {
    ChatSendVO answerChat(ChatSendRequest request);

    InterviewAnswerVO scoreInterviewAnswer(InterviewAnswerRequest request);
}

