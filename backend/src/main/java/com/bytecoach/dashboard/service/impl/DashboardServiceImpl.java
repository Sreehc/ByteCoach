package com.bytecoach.dashboard.service.impl;

import com.bytecoach.common.api.ResultCode;
import com.bytecoach.common.exception.BusinessException;
import com.bytecoach.dashboard.dto.DashboardOverviewVO;
import com.bytecoach.dashboard.dto.RecentInterviewVO;
import com.bytecoach.dashboard.dto.WeakPointVO;
import com.bytecoach.dashboard.mapper.DashboardMetricsMapper;
import com.bytecoach.dashboard.service.DashboardService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.bytecoach.security.util.SecurityUtils;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardMetricsMapper dashboardMetricsMapper;

    @Override
    public DashboardOverviewVO overview() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "login required");
        }

        long chatCount = defaultLong(dashboardMetricsMapper.countChatSessions(userId));
        long interviewCount = defaultLong(dashboardMetricsMapper.countInterviewSessions(userId));
        int learningCount = (int) (chatCount + interviewCount);
        BigDecimal averageScore = defaultDecimal(dashboardMetricsMapper.averageInterviewScore(userId));
        int wrongCount = (int) defaultLong(dashboardMetricsMapper.countWrongQuestions(userId));
        int planCompletionRate = defaultInt(dashboardMetricsMapper.planCompletionRate(userId));
        List<RecentInterviewVO> recentInterviews = defaultList(dashboardMetricsMapper.selectRecentInterviews(userId));
        List<WeakPointVO> weakPoints = defaultList(dashboardMetricsMapper.selectWeakPoints(userId));

        return DashboardOverviewVO.builder()
                .learningCount(learningCount)
                .averageScore(averageScore)
                .wrongCount(wrongCount)
                .planCompletionRate(planCompletionRate)
                .recentInterviews(recentInterviews)
                .weakPoints(weakPoints)
                .firstVisit(learningCount == 0 && wrongCount == 0)
                .build();
    }

    private long defaultLong(Long value) {
        return value == null ? 0L : value;
    }

    private int defaultInt(Integer value) {
        return value == null ? 0 : value;
    }

    private BigDecimal defaultDecimal(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private <T> List<T> defaultList(List<T> value) {
        return value == null ? List.of() : value;
    }
}
