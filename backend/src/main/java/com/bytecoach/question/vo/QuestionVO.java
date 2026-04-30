package com.bytecoach.question.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionVO {
    private Long id;
    private String title;
    private String difficulty;
    private String standardAnswer;
}

