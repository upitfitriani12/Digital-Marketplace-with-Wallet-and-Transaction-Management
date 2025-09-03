package com.fitmart.app.service.impl;

import com.fitmart.app.entity.Category;
import com.fitmart.app.entity.Product;
import com.fitmart.app.repository.CategoryRepository;
import com.fitmart.app.repository.ProductRepository;
import com.fitmart.app.service.ProductService;
import com.fitmart.app.utils.GeneralSpecification;
import com.fitmart.app.utils.dto.request.ProductRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product create(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .build();

        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
            product.setCategories(categories);
        }

        return productRepository.saveAndFlush(product);
    }

    @Override
    public Page<Product> getAll(Pageable pageable, ProductRequest request) {
        Specification<Product> specification = GeneralSpecification.getSpecification(request);
        return productRepository.findAll(specification, pageable);
    }

    @Override
    public Product getById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND, "Product with id " + id + " is not found"));
    }

    @Override
    public Product update(String id, ProductRequest request) {
        Product existingProduct = getById(id);
        existingProduct.setName(request.getName());
        existingProduct.setPrice(request.getPrice());

        if (request.getCategoryIds() != null) {
            List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
            existingProduct.setCategories(categories);
        }

        return productRepository.save(existingProduct);
    }

    @Override
    public void delete(String id) {
        this.getById(id);
        productRepository.deleteById(id);
    }
}