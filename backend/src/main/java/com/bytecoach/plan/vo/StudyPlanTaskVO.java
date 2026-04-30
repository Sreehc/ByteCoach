package com.bytecoach.plan.vo;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudyPlanTaskVO {
    private Long id;
    private LocalDate taskDate;
    private String taskType;
    private String content;
    private String status;
}

