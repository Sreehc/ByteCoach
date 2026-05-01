package com.bytecoach.plan.controller;

import com.bytecoach.common.api.Result;
import com.bytecoach.common.api.ResultCode;
import com.bytecoach.common.exception.BusinessException;
import com.bytecoach.plan.dto.GeneratePlanRequest;
import com.bytecoach.plan.dto.PlanTaskStatusRequest;
import com.bytecoach.plan.service.PlanService;
import com.bytecoach.plan.vo.StudyPlanTaskVO;
import com.bytecoach.plan.vo.StudyPlanVO;
import com.bytecoach.security.util.SecurityUtils;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plan")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @PostMapping("/generate")
    public Result<StudyPlanVO> generate(@Valid @RequestBody GeneratePlanRequest request) {
        return Result.success(planService.generate(currentUserId(), request));
    }

    @GetMapping("/current")
    public Result<StudyPlanVO> current() {
        return Result.success(planService.current(currentUserId()));
    }

    @GetMapping("/{id}/tasks")
    public Result<List<StudyPlanTaskVO>> tasks(@PathVariable Long id) {
        return Result.success(planService.tasks(currentUserId(), id));
    }

    @PutMapping("/task/{taskId}/status")
    public Result<Void> updateTaskStatus(@PathVariable Long taskId,
                                         @Valid @RequestBody PlanTaskStatusRequest request) {
        planService.updateTaskStatus(currentUserId(), taskId, request);
        return Result.success();
    }

    private Long currentUserId() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "login required");
        }
        return userId;
    }
}
