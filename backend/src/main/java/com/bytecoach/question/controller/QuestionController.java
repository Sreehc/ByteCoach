package com.bytecoach.question.controller;

import com.bytecoach.common.api.Result;
import com.bytecoach.question.dto.QuestionQuery;
import com.bytecoach.question.vo.QuestionVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {

    private final com.bytecoach.question.service.QuestionService questionService;

    @GetMapping("/list")
    public Result<List<QuestionVO>> list(@ModelAttribute QuestionQuery query) {
        return Result.success(questionService.listQuestions(query));
    }

    @GetMapping("/{id}")
    public Result<QuestionVO> detail(@PathVariable Long id) {
        return Result.success(questionService.getQuestionDetail(id));
    }
}
