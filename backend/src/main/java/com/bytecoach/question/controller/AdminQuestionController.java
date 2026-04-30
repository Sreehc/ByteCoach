package com.bytecoach.question.controller;

import com.bytecoach.common.api.Result;
import com.bytecoach.question.dto.QuestionUpsertRequest;
import com.bytecoach.question.vo.QuestionVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/question")
public class AdminQuestionController {

    @PostMapping("/add")
    public Result<QuestionVO> add(@Valid @RequestBody QuestionUpsertRequest request) {
        return Result.success(QuestionVO.builder()
                .id(1003L)
                .title(request.getTitle())
                .difficulty(request.getDifficulty())
                .standardAnswer(request.getStandardAnswer())
                .build());
    }

    @PutMapping("/update")
    public Result<QuestionVO> update(@Valid @RequestBody QuestionUpsertRequest request) {
        return Result.success(QuestionVO.builder()
                .id(request.getId() == null ? 1003L : request.getId())
                .title(request.getTitle())
                .difficulty(request.getDifficulty())
                .standardAnswer(request.getStandardAnswer())
                .build());
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return Result.success();
    }
}

