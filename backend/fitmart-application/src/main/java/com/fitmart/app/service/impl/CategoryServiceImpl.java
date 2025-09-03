package com.fitmart.app.service.impl;

import com.fitmart.app.entity.Category;
import com.fitmart.app.repository.CategoryRepository;
import com.fitmart.app.service.CategoryService;
import com.fitmart.app.utils.GeneralSpecification;
import com.fitmart.app.utils.dto.request.CategoryRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public Category create(CategoryRequest request) {
        return categoryRepository.saveAndFlush(request.convert());
    }

    @Override
    public Page<Category> getAll(Pageable pageable, CategoryRequest request) {
        Specification<Category> specification = GeneralSpecification.getSpecification(request);
        return categoryRepository.findAll(specification, pageable);
    }
    @Override
    public Category getById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND, "Category with id " + id + " is not found"));
    }

    @Override
    public Category update(Category request) {
        return categoryRepository.save(request);
    }
    @Override
    public void delete(String id) {
        this.getById(id);
        categoryRepository.deleteById(id);
    }

}
