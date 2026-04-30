package com.bytecoach.category.controller;

import com.bytecoach.category.dto.CategoryQuery;
import com.bytecoach.category.vo.CategoryVO;
import com.bytecoach.common.api.Result;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final com.bytecoach.category.service.CategoryService categoryService;

    @GetMapping("/list")
    public Result<List<CategoryVO>> list(@ModelAttribute CategoryQuery query) {
        return Result.success(categoryService.listCategories(query));
    }
}
