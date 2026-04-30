package com.bytecoach.plan.vo;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudyPlanVO {
    private Long id;
    private String title;
    private String goal;
    private String status;
    private List<StudyPlanTaskVO> tasks;
}

