package com.fitmart.app.service;

import com.fitmart.app.entity.Product;
import com.fitmart.app.utils.dto.request.ProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product create(ProductRequest request);
    Page<Product> getAll(Pageable pageable, ProductRequest request);
    Product getById(String id);
    Product update(Product request);
    void delete(String id);
}
