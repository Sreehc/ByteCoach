package com.bytecoach.plan.controller;

import com.bytecoach.common.api.Result;
import com.bytecoach.plan.dto.GeneratePlanRequest;
import com.bytecoach.plan.dto.PlanTaskStatusRequest;
import com.bytecoach.plan.vo.StudyPlanTaskVO;
import com.bytecoach.plan.vo.StudyPlanVO;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plan")
public class PlanController {

    @PostMapping("/generate")
    public Result<StudyPlanVO> generate(@Valid @RequestBody GeneratePlanRequest request) {
        return Result.success(buildPlan(request.getDirection()));
    }

    @GetMapping("/current")
    public Result<StudyPlanVO> current() {
        return Result.success(buildPlan("Java 后端"));
    }

    @GetMapping("/{id}/tasks")
    public Result<List<StudyPlanTaskVO>> tasks(@PathVariable Long id) {
        return Result.success(buildTasks());
    }

    @PutMapping("/task/{taskId}/status")
    public Result<Void> updateTaskStatus(@PathVariable Long taskId, @Valid @RequestBody PlanTaskStatusRequest request) {
        return Result.success();
    }

    private StudyPlanVO buildPlan(String direction) {
        return StudyPlanVO.builder()
                .id(1L)
                .title(direction + " 七日冲刺计划")
                .goal("围绕薄弱点和错题完成复习闭环")
                .status("active")
                .tasks(buildTasks())
                .build();
    }

    private List<StudyPlanTaskVO> buildTasks() {
        return List.of(
                StudyPlanTaskVO.builder().id(1L).taskDate(LocalDate.now()).taskType("review").content("复习 Spring AOP 错题").status("todo").build(),
                StudyPlanTaskVO.builder().id(2L).taskDate(LocalDate.now().plusDays(1)).taskType("interview").content("完成 1 场 JVM 模拟面试").status("todo").build()
        );
    }
}

