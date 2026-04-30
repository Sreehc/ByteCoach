package com.bytecoach.category.controller;

import com.bytecoach.category.dto.CategoryUpsertRequest;
import com.bytecoach.category.vo.CategoryVO;
import com.bytecoach.common.api.Result;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/category")
public class AdminCategoryController {

    @PostMapping("/add")
    public Result<CategoryVO> add(@Valid @RequestBody CategoryUpsertRequest request) {
        return Result.success(CategoryVO.builder().id(11L).name(request.getName()).type(request.getType()).build());
    }

    @PutMapping("/update")
    public Result<CategoryVO> update(@Valid @RequestBody CategoryUpsertRequest request) {
        return Result.success(CategoryVO.builder()
                .id(request.getId() == null ? 11L : request.getId())
                .name(request.getName())
                .type(request.getType())
                .build());
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return Result.success();
    }
}

