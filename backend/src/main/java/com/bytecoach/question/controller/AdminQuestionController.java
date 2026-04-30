package com.bytecoach.question.controller;

import com.bytecoach.common.api.Result;
import com.bytecoach.question.dto.QuestionUpsertRequest;
import com.bytecoach.question.vo.QuestionVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/question")
@RequiredArgsConstructor
public class AdminQuestionController {

    private final com.bytecoach.question.service.QuestionService questionService;

    @PostMapping("/add")
    public Result<QuestionVO> add(@Valid @RequestBody QuestionUpsertRequest request) {
        return Result.success(questionService.createQuestion(request));
    }

    @PutMapping("/update")
    public Result<QuestionVO> update(@Valid @RequestBody QuestionUpsertRequest request) {
        return Result.success(questionService.updateQuestion(request));
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return Result.success();
    }
}
