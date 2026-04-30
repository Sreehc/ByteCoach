package com.bytecoach.dashboard.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RecentInterviewVO {
    private Long sessionId;
    private String direction;
    private BigDecimal totalScore;
    private String status;
    private LocalDateTime finishedAt;
}

