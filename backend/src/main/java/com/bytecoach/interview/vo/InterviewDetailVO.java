package com.bytecoach.interview.vo;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InterviewDetailVO {
    private Long sessionId;
    private String direction;
    private String status;
    private List<InterviewRecordVO> records;

    @Data
    @Builder
    public static class InterviewRecordVO {
        private Long questionId;
        private String questionTitle;
        private String userAnswer;
        private String score;
    }
}

