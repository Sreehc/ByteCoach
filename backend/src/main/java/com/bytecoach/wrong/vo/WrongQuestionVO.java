package com.bytecoach.wrong.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WrongQuestionVO {
    private Long id;
    private Long questionId;
    private String title;
    private String masteryLevel;
    private String standardAnswer;
    private String errorReason;
}

