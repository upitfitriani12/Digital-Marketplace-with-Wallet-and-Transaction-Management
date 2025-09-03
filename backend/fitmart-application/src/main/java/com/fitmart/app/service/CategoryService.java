package com.fitmart.app.service;

import com.fitmart.app.entity.Category;
import com.fitmart.app.utils.dto.request.CategoryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Category create(CategoryRequest request);
    Page<Category> getAll(Pageable pageable, CategoryRequest request);
    Category getById(String id);
    Category update(Category request);
    void delete(String id);
}