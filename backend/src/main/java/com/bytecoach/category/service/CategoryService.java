package com.bytecoach.category.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytecoach.category.dto.CategoryQuery;
import com.bytecoach.category.dto.CategoryUpsertRequest;
import com.bytecoach.category.entity.Category;
import com.bytecoach.category.vo.CategoryVO;
import java.util.List;

public interface CategoryService extends IService<Category> {
    List<CategoryVO> listCategories(CategoryQuery query);

    CategoryVO createCategory(CategoryUpsertRequest request);

    CategoryVO updateCategory(CategoryUpsertRequest request);

    void deleteCategory(Long id);

    Category getRequiredById(Long id);

    Category findOrCreateKnowledgeCategory(String name);
}
