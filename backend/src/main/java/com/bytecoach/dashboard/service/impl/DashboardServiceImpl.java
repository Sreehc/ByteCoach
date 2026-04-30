package com.bytecoach.dashboard.service.impl;

import com.bytecoach.dashboard.dto.DashboardOverviewVO;
import com.bytecoach.dashboard.service.DashboardService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Override
    public DashboardOverviewVO overview() {
        return DashboardOverviewVO.builder()
                .learningCount(0)
                .averageScore(BigDecimal.ZERO)
                .wrongCount(0)
                .recentInterviewResults(List.of())
                .weakCategories(List.of("Spring", "JVM", "MySQL"))
                .currentPlanCompletionRate(0)
                .build();
    }
}

