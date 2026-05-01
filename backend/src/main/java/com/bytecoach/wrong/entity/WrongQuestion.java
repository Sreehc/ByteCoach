package com.bytecoach.wrong.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bytecoach.common.entity.BaseEntity;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("wrong_question")
@EqualsAndHashCode(callSuper = true)
public class WrongQuestion extends BaseEntity {
    private Long userId;
    private Long questionId;
    private String sourceType;
    private String userAnswer;
    private String standardAnswer;
    private String errorReason;
    private String masteryLevel;
    private Integer reviewCount;
    private LocalDateTime lastReviewTime;
}
