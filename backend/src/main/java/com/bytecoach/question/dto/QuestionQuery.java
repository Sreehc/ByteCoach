package com.bytecoach.question.dto;

import lombok.Data;

@Data
public class QuestionQuery {
    private Long categoryId;
    private String difficulty;
    private String keyword;
}

