package com.bytecoach.wrong.controller;

import com.bytecoach.common.api.Result;
import com.bytecoach.wrong.dto.WrongMasteryUpdateRequest;
import com.bytecoach.wrong.vo.WrongQuestionVO;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wrong")
public class WrongController {

    @GetMapping("/list")
    public Result<List<WrongQuestionVO>> list() {
        return Result.success(List.of(
                WrongQuestionVO.builder().id(1L).questionId(1001L).title("解释 Spring AOP 的实现原理").masteryLevel("reviewing").build()
        ));
    }

    @GetMapping("/{id}")
    public Result<WrongQuestionVO> detail(@PathVariable Long id) {
        return Result.success(WrongQuestionVO.builder()
                .id(id)
                .questionId(1001L)
                .title("解释 Spring AOP 的实现原理")
                .masteryLevel("reviewing")
                .standardAnswer("标准答案占位")
                .errorReason("遗漏了 JDK 动态代理和 CGLIB 的区别。")
                .build());
    }

    @PutMapping("/mastery/{id}")
    public Result<Void> updateMastery(@PathVariable Long id, @Valid @RequestBody WrongMasteryUpdateRequest request) {
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return Result.success();
    }
}

