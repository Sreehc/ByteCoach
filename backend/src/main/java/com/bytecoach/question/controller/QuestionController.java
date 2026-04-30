package com.bytecoach.question.controller;

import com.bytecoach.common.api.Result;
import com.bytecoach.question.vo.QuestionVO;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @GetMapping("/list")
    public Result<List<QuestionVO>> list() {
        return Result.success(List.of(
                QuestionVO.builder().id(1001L).title("解释 Spring AOP 的动态代理实现").difficulty("medium").build(),
                QuestionVO.builder().id(1002L).title("JVM CMS 和 G1 的主要差异").difficulty("hard").build()
        ));
    }

    @GetMapping("/{id}")
    public Result<QuestionVO> detail(@PathVariable Long id) {
        return Result.success(QuestionVO.builder()
                .id(id)
                .title("示例题目")
                .difficulty("medium")
                .standardAnswer("这里返回标准答案占位，后续接题库表。")
                .build());
    }
}

