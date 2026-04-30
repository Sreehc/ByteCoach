package com.bytecoach.category.controller;

import com.bytecoach.category.dto.CategoryUpsertRequest;
import com.bytecoach.category.vo.CategoryVO;
import com.bytecoach.common.api.Result;
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
@RequestMapping("/api/admin/category")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final com.bytecoach.category.service.CategoryService categoryService;

    @PostMapping("/add")
    public Result<CategoryVO> add(@Valid @RequestBody CategoryUpsertRequest request) {
        return Result.success(categoryService.createCategory(request));
    }

    @PutMapping("/update")
    public Result<CategoryVO> update(@Valid @RequestBody CategoryUpsertRequest request) {
        return Result.success(categoryService.updateCategory(request));
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }
}
