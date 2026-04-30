package com.bytecoach.dashboard.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardOverviewVO {
    private Integer learningCount;
    private BigDecimal averageScore;
    private Integer wrongCount;
    private List<String> recentInterviewResults;
    private List<String> weakCategories;
    private Integer currentPlanCompletionRate;
}

