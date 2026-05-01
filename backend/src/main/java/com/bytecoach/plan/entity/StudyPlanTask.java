package com.bytecoach.plan.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bytecoach.common.entity.BaseEntity;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("study_plan_task")
@EqualsAndHashCode(callSuper = true)
public class StudyPlanTask extends BaseEntity {
    private Long planId;
    private Long userId;
    private LocalDate taskDate;
    private String taskType;
    private String content;
    private String status;
    private Integer sortOrder;
}
