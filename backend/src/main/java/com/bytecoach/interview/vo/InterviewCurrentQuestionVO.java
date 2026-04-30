package com.bytecoach.interview.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InterviewCurrentQuestionVO {
    private Long sessionId;
    private Integer currentIndex;
    private Integer questionCount;
    private Long questionId;
    private String questionTitle;
}

