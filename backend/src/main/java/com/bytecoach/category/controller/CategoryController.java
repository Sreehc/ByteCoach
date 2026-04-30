package com.bytecoach.category.controller;

import com.bytecoach.category.vo.CategoryVO;
import com.bytecoach.common.api.Result;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @GetMapping("/list")
    public Result<List<CategoryVO>> list() {
        return Result.success(List.of(
                CategoryVO.builder().id(1L).name("Spring").type("question").build(),
                CategoryVO.builder().id(2L).name("JVM").type("knowledge").build(),
                CategoryVO.builder().id(3L).name("MySQL").type("interview").build()
        ));
    }
}

